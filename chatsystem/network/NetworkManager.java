package chatsystem.network;

import java.io.IOException;

import java.util.ArrayList;

import java.net.Socket;

import java.lang.Thread;

import chatsystem.Client;

public class NetworkManager extends Thread {

	private Client master;

	private ConnectionListener colistener;

	private static ArrayList<ConnectionHandler> cohandlers;

	public NetworkManager(Client master, int listeningTCPPort) {

		this.master = master;
		this.colistener = new ConnectionListener(this, listeningTCPPort);
		
		this.cohandlers = new ArrayList<ConnectionHandler>();

	}

	public void startAll() {
		synchronized(this.master) {
			startConnectionListener();

			this.master.notify();
		}
	}

	private void startConnectionListener() {
		this.colistener.start();
		synchronized(this) {
			try { wait(); } catch (InterruptedException ie) {}
		}
	}

	public synchronized void shutdown() {

		ArrayList<ConnectionHandler> subs = new ArrayList<ConnectionHandler>(this.getConnectionHandlers());

		for (ConnectionHandler s: subs) {

			s.shutdown();
		}		

		try {
			this.colistener.shutdown();
		} catch (IOException ioe) {
			System.out.println("[x] Issue while shutting down the connection listener, aborted");
			System.exit(1);
		}
	}

	private static synchronized ArrayList<ConnectionHandler> getConnectionHandlers() {
		return NetworkManager.cohandlers;
	}

	protected static synchronized void notifyDeathOfConnectionHandler(ConnectionHandler sub) {
		NetworkManager.cohandlers.remove(sub);
	}
	
	protected static synchronized void notifyNewConnection(Socket clientConnection) {
		ConnectionHandler ch = new ConnectionHandler(clientConnection);	

		NetworkManager.cohandlers.add(ch);
		ch.start();
	}

	public void run() {

		this.startAll();	
	}
}

