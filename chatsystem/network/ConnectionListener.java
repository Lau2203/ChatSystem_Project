package chatsystem.network;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

import java.lang.Thread;

import chatsystem.util.Logs;

/* Listener for incoming TCP connections */
public class ConnectionListener extends Thread {
	/* We need a reference to the NetworkManager so that we are able to
	 * to wake him up once the server started for instance, or once a new connection arises */
	private NetworkManager master;

	private ServerSocket ssocket;
	private int port;

	private Socket clientSocket;
	/* For logs filling only */
	private String instanceName = "ConnectionListener";

	public ConnectionListener(NetworkManager master, int port) {

		this.master = master;

		this.ssocket = null;
		this.port = port;
	}

	private void prepareServer() throws IOException {
		synchronized (this.master) {
			try {
				this.ssocket = new ServerSocket(this.port);
			} catch (IllegalArgumentException iae) {
				System.out.println("Invalid port number, aborted");
				System.exit(1);
			}

			if (this.port == 0)
				this.port = this.ssocket.getLocalPort();

			System.out.println("Server listening on port " + this.port);

			/* Notify the NetworkManager that the server is now listening */
			this.master.notify();
		}
	}
	/* Can only be called by the NetworkManager when itself is shutting down */
	protected void shutdown() throws IOException {
		this.ssocket.close();
	}

	/* Actual process of waiting for incoming TCP connections */
	private void listen() {

		Socket clientSocket;

		while (true) {

			try {
				clientSocket = this.ssocket.accept();
				/* We now notify the NetworkManager that a new connection arised */
				synchronized(this) {
					try {
						this.master.notifyNewConnection(clientSocket);
						wait();
					} catch (InterruptedException ie) {}
				}


			} catch (IOException ioe) {}
		}
	}

	public void run() {

		Logs.printinfo(this.instanceName, "Starting the server");
		try {
			this.prepareServer();
		} catch (IOException ioe) {
			Logs.printerro(this.instanceName, "Could not create server socket, aborted");
			System.exit(1);
		}
		Logs.printinfo(this.instanceName, "Server successfully started");

		this.listen();
	}
}

