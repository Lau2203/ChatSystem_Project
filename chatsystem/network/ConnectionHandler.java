package chatsystem.network;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.InetAddress;

import java.lang.Thread;

import chatsystem.User;

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

	private BufferedReader in;
	private PrintWriter out;

	private InetAddress remoteAddress;
	private int remotePort;

	private boolean isInterrupted;
	/* For logs filling only */
	private String instanceName;

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

	public synchronized PrintWriter getWriter() {
		return this.out;
	}

	private void prepareIO() {
		try {
			this.in = new BufferedReader(new InputStreamReader(this.cs.getInputStream()));
			this.out = new PrintWriter(this.cs.getOutputStream(), true);

		} catch (IOException ioe) {
			System.out.println("IO issue, aborted");
			this.interrupt();
		}
	}

	private String readln() throws IOException {
		return this.in.readLine();
	}

	private void write(String s) {
		this.out.print(s);
		this.out.flush();
	}

	private void writeln(String s) {
		this.out.println(s);
		this.out.flush();
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

		String userInput;
		boolean userShotDownConnection = false;	

		this.prepareIO();

		Logs.printinfo(this.instanceName, "Successfully connected with [" + this.remoteAddress + ":" + this.remotePort + "]");

		while (true) {
			try {
				userInput = this.readln();
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

			ConnectionHandler.master.notifyNewMessage(this, userInput);

			Logs.println(this.instanceName, "Received '" + userInput + "'");

			this.writeln("'userInput' : " + userInput.length());
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
