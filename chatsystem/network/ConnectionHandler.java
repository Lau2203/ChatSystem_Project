package chatsystem.network;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.InetAddress;

import java.lang.Thread;

import chatsystem.network.ConnectionListener;

import chatsystem.util.Logs;

public class ConnectionHandler extends Thread {

	Socket cs;

	private BufferedReader in;
	private PrintWriter out;

	private InetAddress remoteAddress;
	private int remotePort;

	private boolean isInterrupted;

	private String instanceName;

	public ConnectionHandler(Socket clientSocket) {
		this.cs = clientSocket;	

		this.remoteAddress = this.cs.getInetAddress();
		this.remotePort = this.cs.getPort();

		this.isInterrupted = false;
		this.instanceName = "ConnectionHandler - " + this.toString();
	}

	private void prepareIO() {
		try {
			this.in = new BufferedReader(new InputStreamReader(this.cs.getInputStream()));
			this.out = new PrintWriter(this.cs.getOutputStream());

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
		NetworkManager.notifyDeathOfConnectionHandler(this);
	}

	private void interruptSubServer() {
		synchronized(this) {
			this.isInterrupted = true;
		}
	}

	private boolean isSubServerInterrupted() {
		synchronized(this) {
			return this.isInterrupted;
		}
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
