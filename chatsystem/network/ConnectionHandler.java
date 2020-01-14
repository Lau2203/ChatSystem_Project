package chatsystem.network;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;

import java.net.Socket;
import java.net.InetAddress;

import java.lang.Thread;

import java.sql.Timestamp;

import chatsystem.User;
import chatsystem.Message;
import chatsystem.MessageString;
import chatsystem.MessageFile;

import chatsystem.network.ConnectionListener;

import chatsystem.util.Logs;

/* Handler for one TCP connection */
public class ConnectionHandler extends Thread {
	/* We need a reference to the NetworkManager in order to wake him up
	 * once we received a new message or once the endpoint connection ended */
	private static NetworkManager master;
	/* Since it is a User-to-User connection, a ConnectionHandler is always
	 * associated to one single recipient User */
	private User recipient;

	private Socket cs;

	private InputStream in;
	private OutputStream out;

	private InetAddress remoteAddress;
	private int remotePort;

	private boolean isInterrupted;
	/* For logs filling only */
	private String instanceName;

	public long MAX_SIZE_FILE_UPLOAD = 16777216;

	public ConnectionHandler(Socket clientSocket) {
		this.cs = clientSocket;	

		this.remoteAddress = this.cs.getInetAddress();
		this.remotePort = this.cs.getPort();

		this.isInterrupted = false;
		this.instanceName = "ConnectionHandler - " + this.toString();

		this.in = null;
		this.out = null;
	}

	public ConnectionHandler(User recipient, Socket clientSocket) {
		this.recipient = recipient;

		this.cs = clientSocket;	

		this.remoteAddress = this.cs.getInetAddress();
		this.remotePort = this.cs.getPort();

		this.isInterrupted = false;
		this.instanceName = "ConnectionHandler - " + this.toString();
	}

	public User getRecipientUser() {
		return this.recipient;
	}

	public static void setMaster(NetworkManager master) {
		ConnectionHandler.master = master;
	}

	public synchronized InetAddress getRemoteAddress() {
		return this.remoteAddress;
	}

	public synchronized int getRemotePort() {
		return this.remotePort;
	}

	public synchronized OutputStream getOutput() {
		return this.out;
	}

	private void prepareIO() {
		try {
			this.in = this.cs.getInputStream();
			this.out = this.cs.getOutputStream();

		} catch (IOException ioe) {
			System.out.println("IO issue, aborted");
			this.interrupt();
		}
	}

	private Message read() throws IOException {

		Message msg = null;
		byte[] buffer = new byte[(int)this.MAX_SIZE_FILE_UPLOAD];

		int ret = this.in.read(buffer, 0, (int)this.MAX_SIZE_FILE_UPLOAD);

		if (ret == 0 || ret == -1) {
			return null;
		}

		/* If it's only a string message */
		if ((new String(buffer, 0, 2)).equals("S:")) {
			msg = new MessageString(this.recipient, new Timestamp(System.currentTimeMillis()), true);
			msg.setContent(new String(buffer, 2, ret - 2));
		}
		/* Otherwise it is a file */
		else { 
			msg = new MessageFile(this.recipient, new Timestamp(System.currentTimeMillis()));

			String filename = (new String(buffer)).split(":")[1];

			File file = new File("/home/a_michau/Bureau/" + filename);
			try {
				file.createNewFile();

				OutputStream os = new FileOutputStream(file);
				os.write(buffer, ("F:" + filename + ":").length(), buffer.length - ("F:" + filename + ":").length());
				os.flush();
				os.close();

			} catch (Exception e) { e.printStackTrace(); }

			msg.setContent(file);
		}

		ConnectionHandler.master.notifyNewMessage(this, msg);

		Logs.println(this.instanceName, "Received '" + msg.toString() + "'");

		return msg;
	}

	private void disconnect() {
		try {
			this.cs.close();
			Logs.printinfo(this.instanceName, "Disconnected from [" + this.remoteAddress + ":" + this.remotePort + "]");

		} catch (IOException ioe) {
			Logs.printerro(this.instanceName, "Issue with closing client socket, aborted");
		}
	}

	private void die() {
		ConnectionHandler.master.notifyDeathOfConnectionHandler(this);
	}

	private synchronized void interruptSubServer() {
		this.isInterrupted = true;
	}

	private synchronized boolean isSubServerInterrupted() {
		return this.isInterrupted;
	}

	protected void shutdown() {
		this.interruptSubServer();
		this.disconnect();
	}

	public void run() {

		Message userInput;
		boolean userShotDownConnection = false;	

		this.prepareIO();

		Logs.printinfo(this.instanceName, "Successfully connected with [" + this.remoteAddress + ":" + this.remotePort + "]");

		while (true) {
			try {
				userInput = this.read();
			} catch (IOException ioe) {
				/* If we are actually the one that closed the socket (or the main subserver manager) */
				if (!this.isSubServerInterrupted()) {
					userShotDownConnection = true;
				}
				break;
			}

			if (userInput == null) {
				if (!this.isSubServerInterrupted()) {
					userShotDownConnection = true;
				}
				break;
			}

		}

		/* 2 Possibilities 
		 *  - The endpoint user connection has ended
		 *  - We (or the main sub server manager) shot down the connection */

		/* The endpoint user connection has ended */
		if (userShotDownConnection) {
			Logs.printinfo(this.instanceName, "User shot down connection");
		}

		this.die();
	}

	public String toString() {
		return this.remoteAddress + ":" + this.remotePort;
	}
}
