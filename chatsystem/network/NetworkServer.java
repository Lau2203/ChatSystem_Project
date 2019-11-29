package chatsystem.network;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;

import java.lang.Thread;

import chatsystem.util.Logs;

public class NetworkServer extends Thread {

	private NetworkServerApplication master;

	private ServerSocket ssocket;
	private int port;

	private Socket clientSocket;

	private static ArrayList<NetworkSubServer> subs;

	private String instanceName = "NetworkServer";

	public NetworkServer(NetworkServerApplication master, int port) {

		this.master = master;

		this.ssocket = null;
		this.port = port;

		this.subs = new ArrayList<NetworkSubServer>();
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

	protected void shutdown() {

		ArrayList<NetworkSubServer> subs = new ArrayList<NetworkSubServer>(this.subs);

		for (NetworkSubServer s: subs) {

			s.shutdown();
		}		

		try {
			this.ssocket.close();
		} catch (IOException ioe) {
			System.out.println("Issue in disconnecting, aborted");
			System.exit(1);
		}
	}

	protected static synchronized void notifyDeathOfNetworkSubServer(NetworkSubServer sub) {
		NetworkServer.subs.remove(sub);
	}

	private void listen() {

		Socket clientSocket;
		NetworkSubServer sub;

		while (true) {

			try {
				clientSocket = this.ssocket.accept();

				sub = new NetworkSubServer(clientSocket);
				this.subs.add(sub);
				sub.start();	

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

	public void readLogs() {
		Logs.readLogs();
	}
}

