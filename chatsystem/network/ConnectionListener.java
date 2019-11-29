package chatsystem.network;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

import java.lang.Thread;

import chatsystem.util.Logs;

public class ConnectionListener extends Thread {

	private NetworkManager master;

	private ServerSocket ssocket;
	private int port;

	private Socket clientSocket;

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

			/* Notify the application that the server is now listening */
			this.master.notify();
		}
	}

	protected void shutdown() throws IOException {
		this.ssocket.close();
	}

	private void listen() {

		Socket clientSocket;

		while (true) {

			try {
				clientSocket = this.ssocket.accept();

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
			Logs.printerro("Could not create server socket, aborted");
			System.exit(1);
		}
		Logs.printinfo(this.instanceName, "Server successfully started");

		this.listen();
	}
}

