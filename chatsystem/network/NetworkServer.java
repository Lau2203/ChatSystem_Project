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

	private static final String logsFilePath = ".server.logs";

	private Logs logs;

	public NetworkServer(NetworkServerApplication master, int port) {

		this.master = master;

		this.ssocket = null;
		this.port = port;

		this.subs = new ArrayList<NetworkSubServer>();

		try {
			this.logs = new Logs(this.logsFilePath);
		} catch (IOException ioe) {
			System.out.println("[x] ERROR: Unable to reach log file");	
			System.exit(1);
		}
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

			this.logs.println("=== NEW INSTANCE OF SERVER ===");
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

		this.logs.writeLogs();
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

		try {
			this.prepareServer();
		} catch (IOException ioe) {
			System.out.println("Could not create server socket, aborted");
			System.exit(1);
		}

		NetworkSubServer.logs = this.logs;

		this.listen();
	}

	public void readLogs() {
		this.logs.readLogs();
	}
}

