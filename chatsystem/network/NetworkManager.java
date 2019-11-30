package chatsystem.network;

import java.io.IOException;

import java.util.ArrayList;

import java.net.Socket;

import java.lang.Thread;

import java.sql.Timestamp;

import chatsystem.User;
import chatsystem.Client;
import chatsystem.NotifyInformation;
import chatsystem.MessageString;

/* FOR TESTING ONLY */
import chatsystem.util.ConfigParser;

public class NetworkManager extends Thread {

	/* The main client process we will wake up once we've been waken up by
	 * several available notifications (for instance a new connection,
	 * new message, new active user on the network). */
	private Client master;
	/* The TCP listener waiting for new connections to arise.
	 * Once it happens, it notify the NetworkManager, which then creates a ConnectionHandler
	 * in order to give it the control of that connection. */
	private ConnectionListener connectionListener;
	/* All the subconnections detected by the ConnectionListeners are handled
	 * by the ConnectionHandler. Each ConnectionHandler provides one User-to-User TCP connection */
	private ArrayList<ConnectionHandler> connectionHandlers;
	/* This is the placeholder for notifiers to put their notification information. For instance,
	 * it may contain a new User's information (fingerprint, username, IP address), or a new message.
	 * This is the place the NetworkManager will be looking at when waken up by the ConnectionListener,
	 * ConnectionHandler or NetworkSignalListener. */
	private NetworkManagerInformation networkManagerInformation;

	private ArrayList<User> activeUsersList;

	public NetworkManager(Client master, int listeningTCPPort) {

		this.master = master;
		this.connectionListener = new ConnectionListener(this, listeningTCPPort);

		this.connectionHandlers = new ArrayList<ConnectionHandler>();

		this.networkManagerInformation = new NetworkManagerInformation();

		this.activeUsersList = new ArrayList<User>();

		//String hostname = ConfigParser.get("main-user");
		//this.activeUsersList.add(new User("", "main-user", InetAddress.getByName(hostname)));
	}

	public void startAll() {
		/* Once the ConnectionListener and the NetworkSignalListener are started,
		 * We want to notify the main client process */
		synchronized(this.master) {
			startConnectionListener();

			this.master.notify();
		}
	}

	private void startConnectionListener() {
		/* We wait for the ConnectionListener to wake us up, meaning that it started successfully */
		this.connectionListener.start();
		synchronized(this) {
			try { wait(); } catch (InterruptedException ie) {}
		}
	}
	/* Can only be called by the main client process */
	public synchronized void shutdown() {

		ArrayList<ConnectionHandler> subs = new ArrayList<ConnectionHandler>(this.getConnectionHandlers());

		for (ConnectionHandler s: subs) {

			s.shutdown();
		}		

		try {
			this.connectionListener.shutdown();
		} catch (IOException ioe) {
			System.out.println("[x] Issue while shutting down the connection listener, aborted");
			System.exit(1);
		}
	}

	private synchronized ArrayList<ConnectionHandler> getConnectionHandlers() {
		return this.connectionHandlers;
	}

	/* Notification methods called by either the ConnectionListener, ConnectionHandlers or the NetworkSignalListener
	 * These notifications refer to the type of information they convey. These different types can be 
	 * seen in the NotifyInformation class */

	/* Can only be called by a ConnectionHandler */
	/* Meaning that a User-to-User connection has ended */
	protected synchronized void notifyDeathOfConnectionHandler(ConnectionHandler ch) {

		this.connectionHandlers.remove(ch);
		/* Put information for the NetworkManager to handle it */
		this.networkManagerInformation.setToBeNotified(ch);
		this.networkManagerInformation.setNotifyInformation(NotifyInformation.END_OF_CONNECTION);
		this.networkManagerInformation.setRecipientUser(ch.getRecipientUser());
		/* Wake up the NetworkManager to handle the death of the connection handler
		 * and tell the client too */
		this.notify();
	}
	/* Can only be called by a ConnectionHandler, since we must be connected to the remote client first */
	protected synchronized void notifyNewMessage(ConnectionHandler ch, String content) {

		MessageString msg = new MessageString(ch.getRecipientUser(), new Timestamp(System.currentTimeMillis()));

		msg.setContent(content);

		/* Put information for the NetworkManager to handle it */
		this.networkManagerInformation.setToBeNotified(ch);
		this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_MESSAGE);
		this.networkManagerInformation.setRecipientUser(ch.getRecipientUser());
		this.networkManagerInformation.setMessage(msg);
		/* Wake up the NetworkManager to handle the death of the connection handler
		 * and tell the client too */
		this.notify();
	}
	/* Can only be called by the ConnectionListener */
	protected synchronized void notifyNewConnection(Socket clientConnection) {
		ConnectionHandler ch = new ConnectionHandler(clientConnection);	

		this.connectionHandlers.add(ch);
		/* Put information for the NetworkManager to handle it */
		this.networkManagerInformation.setToBeNotified(this.connectionListener);
		this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_CONNECTION);

		ch.start();
		/* Wake up the NetworkManager to handle the death of the connection handler
		 * and tell the client too */
		this.notify();	
	}



	/* Once it's been waken up, the NetworkManager needs to understand why
	 * and act consequently */
	private void handleNewInformation() {
		switch (this.networkManagerInformation.getNotifyInformation()) {

			case NEW_CONNECTION:
				break;

			case END_OF_CONNECTION:
				break;

			case USERNAME_MODIFICATION:
				break;

			case NEW_ACTIVE_USER:
				break;

			case USER_LEFT_NETWORK:
				break;

			default: break;
		}

		/* We relay the information to the main client process */
		this.master.notifyFromNetworkManager(this.networkManagerInformation);
		/* Eventually, we wake up the one that's waken us up */
		synchronized(this.networkManagerInformation.getToBeNotified()) {
			/* Wake the information provider (either ConnectionListener, ConnectionHandler or Client) */
			this.networkManagerInformation.getToBeNotified().notify();
		}
	}


	/* The whole process is here */
	public void run() {
		/* So that all the ConnectionHandlers are able to call NetworkManager notification methods */
		ConnectionHandler.setMaster(this);
		/* Start the ConnectionListener (TCP) and the NetworkSignalListener (UDP) */
		this.startAll();

		while (true) {
			/* We constantly wait for a signal from either the ConnectionListener,
			 * the ConnectionHandlers or the NetworkSignalListener */
			synchronized(this) {
				try { 
					wait();
				} catch (InterruptedException ie) {}
			}
			/* Once we've been waken up, we now need to know why,
			 * and we need to process that information */
			this.handleNewInformation();
		}
	}
}

