package chatsystem.network;

import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import java.net.Socket;
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.lang.Thread;
/* For message timestamping */
import java.sql.Timestamp;

/* Creating a new user when there is a new active client */
import chatsystem.User; /* Reference to the main user, only for NetworkSignalListener, in order
 * for him to be able to directly respond to request with the main user's username */
import chatsystem.MainUser;
import chatsystem.Client;
import chatsystem.LocalClient;
import chatsystem.NotifyInformation;
import chatsystem.Message;
import chatsystem.MessageString;

import chatsystem.util.Logs;
/* FOR TESTING ONLY */
import chatsystem.util.ConfigParser;

public class NetworkManager {

	/* The main client process we will wake up once we've been waken up by
	 * several available notifications (for instance a new connection,
	 * new message, new active user on the network). */
	private Client master;
	/* The TCP listener waiting for new connections to arise.
	 * Once it happens, it notify the NetworkManager, which then creates a ConnectionHandler
	 * in order to give it the control of that connection. */
	private ConnectionListener connectionListener;
	private int connectionListenerListeningPort;
	/* All the subconnections detected by the ConnectionListeners are handled
	 * by the ConnectionHandler. Each ConnectionHandler provides one User-to-User TCP connection */
	private ArrayList<ConnectionHandler> connectionHandlers;

	private NetworkSignalListener nsl;
	private int networkSignalListenerListeningPort;

	/* This is the placeholder for notifiers to put their notification information. For instance,
	 * it may contain a new User's information (fingerprint, username, IP address), or a new message.
	 * This is the place the NetworkManager will be looking at when waken up by the ConnectionListener,
	 * ConnectionHandler or NetworkSignalListener. */
	private NetworkManagerInformation networkManagerInformation;

	private ArrayList<User> activeUsersList;

	private String instanceName = "NetworkManager";

	private String lock = new String();
	private String childrenLock = new String();

	private Semaphore semaphore = new Semaphore(0, true);

	private int activeClientNumber;

	private boolean workRemotelyOnly = false;

	private RemoteServerListener remoteServerListener;

	public NetworkManager(Client master, MainUser mainUser, int listeningTCPPort, int listeningUDPPort, String remoteServerURL) {

		this.networkSignalListenerListeningPort = listeningUDPPort;

		this.connectionListenerListeningPort = listeningTCPPort;

		this.master = master;

		this.connectionListener		= new ConnectionListener(this, listeningTCPPort);

		this.connectionHandlers 	= new ArrayList<ConnectionHandler>();

		this.nsl 			= new NetworkSignalListener(this, mainUser, networkSignalListenerListeningPort);

		this.remoteServerListener	= new RemoteServerListener(this, mainUser, remoteServerURL);

		this.networkManagerInformation 	= new NetworkManagerInformation();

		this.activeUsersList 		= new ArrayList<User>();

		this.activeClientNumber = 1;
	}

	/* Should be called only once and before calling 'run' method */
	public void doWorkRemotelyOnly(boolean b) {
		this.workRemotelyOnly = b;
	}

	private User getUser(String fingerprint) {
		for (User usr: this.activeUsersList) {
			if (usr.getFingerprint().equals(fingerprint))
				return usr;
		}

		return null;
	}

	public int getActiveClientNumber() {
		return this.activeClientNumber;
	}

	/* returns a copy of the Active Users List */
	public ArrayList<User> getActiveUsersList() {
		return new ArrayList<User>(this.activeUsersList);
	}

	/* Do not forget to add +1 for us */
	public int getActiveClientsNumber() { 
		return this.activeUsersList.size();
	}

	private ConnectionHandler getConnectionHandler(InetAddress remoteAddress) {
		for (ConnectionHandler ch: this.connectionHandlers) {
			if (ch.getRemoteAddress().equals(remoteAddress)) {	
				return ch;
			}
		}
		return null;
	}

	public ConnectionHandler getConnectionHandler(User user) {
		return this.getConnectionHandler(user.getAddress());
	}

	public void startAll() {
		this.startConnectionListener();

		/* If we are a local client (not a remote client), we need to listen to broadcasted signals */
		if (!workRemotelyOnly) {
			this.startNetworkSignalListener();
		}

		this.startRemoteServerListener();
	}

	private void startConnectionListener() {
		this.connectionListener.start();
	}

	private void startNetworkSignalListener() {
		this.nsl.start();
	}

	private void startRemoteServerListener() {
		this.remoteServerListener.start();
	}

	private void disconnect() {
		String request = this.master.getMainUser().getFingerprint() + ":" + NetworkManagerInformation.END_OF_ACTIVE_CLIENT_STRING;

		DatagramSocket ds;
		DatagramPacket dp = null;

		try {
			ds = new DatagramSocket();
		} catch (SocketException se) {
			ds = null;
			Logs.printerro(this.instanceName, "Could not create datagram socket while trying to notify a username modification to others, aborted");
			System.exit(1);
		}

		try {
			dp = new DatagramPacket(request.getBytes(), request.length(), NetworkManagerInformation.BROADCAST_ADDR, this.networkSignalListenerListeningPort);
		} catch (Exception uhe) { uhe.printStackTrace(); }

		try { ds.send(dp); } catch (IOException ioe) { ioe.printStackTrace(); }

		this.remoteServerListener.notifyDisconnection();
	}

	/* Can only be called by the main client process */
	public void shutdown() {

		ArrayList<ConnectionHandler> subs = new ArrayList<ConnectionHandler>(this.getConnectionHandlers());

		Logs.printinfo(this.instanceName, "Shutting down all the ConnectionHandlers...");
		for (ConnectionHandler s: subs) {

			s.shutdown();
		}		

		Logs.printinfo(this.instanceName, "Shutting down the ConnectionListener...");
		try {
			this.connectionListener.shutdown();
			Logs.printinfo(this.instanceName, "All has been shot down successfully.");
		} catch (IOException ioe) {
			Logs.printerro(this.instanceName, "Issue while shutting down the connection listener, aborted.");
			System.exit(1);
		}

		this.disconnect();
	}

	private ArrayList<ConnectionHandler> getConnectionHandlers() { return this.connectionHandlers; }


	/* Notification methods called either by the ConnectionListener, ConnectionHandlers or the NetworkSignalListener
	 * These notifications refer to the type of information they convey. These different types can be 
	 * seen in the NotifyInformation class */

	/* Can only be called by a ConnectionHandler */
	/* Meaning that a User-to-User connection has ended */
	protected synchronized void notifyDeathOfConnectionHandler(ConnectionHandler ch) {
		this.connectionHandlers.remove(ch);
	}
	/* Can only be called by a ConnectionHandler, since we must be connected to the remote client first */
	protected synchronized void notifyNewMessage(ConnectionHandler ch, Message msg) {

		this.master.notifyNewMessage(ch.getRecipientUser(), msg);
	}

	/* Can only be called by the ConnectionListener */
	protected void notifyNewConnection(Socket clientConnection) {

		User recipient = this.master.getUserByAddress(clientConnection.getInetAddress());
		if (recipient == null) {
			Logs.printerro(this.instanceName, "Received a connection from a unknown user, aborting the connection");
			return;
		}

		ConnectionHandler ch = new ConnectionHandler(recipient, clientConnection);	

		synchronized(this) {
			this.connectionHandlers.add(ch);
		}

		ch.start();
	}

	/* Can only be called by the NetworkSignalListener */
	protected void notifyNewActiveUser(String fingerprint, InetAddress address, String username) {
		this.activeClientNumber++;
		this.master.notifyNewActiveUser(fingerprint, address, username);
	}

	protected void notifyEndOfActiveUser(String fingerprint) {
		this.activeClientNumber--;
		this.master.notifyEndOfActiveUser(fingerprint);
	}

	protected void notifyNewUsername(String fingerprint, String username) {
		this.master.notifyNewUsername(fingerprint, username);
	}

	protected void notifySetNewUsername() {
		this.master.notifySetNewUsername();
	}

	public void notifyNewUsernameToBeSent(String fingerprint, String username) {

		String request = fingerprint + ":" + NetworkManagerInformation.USERNAME_MODIFICATION_STRING + ":" + username;

		DatagramSocket ds;
		DatagramPacket dp = null;

		try {
			ds = new DatagramSocket();
		} catch (SocketException se) {
			ds = null;
			Logs.printerro(this.instanceName, "Could not create datagram socket while trying to notify a username modification to others, aborted");
			System.exit(1);
		}

		try {
			dp = new DatagramPacket(request.getBytes(), request.length(), InetAddress.getByName("255.255.255.255"), this.networkSignalListenerListeningPort);
		} catch (UnknownHostException uhe) { uhe.printStackTrace(); }

		try { ds.send(dp); } catch (IOException ioe) { ioe.printStackTrace(); }

		this.remoteServerListener.notifyRemoteServer();
	}

	public void notifyNewMessageToBeSent(User user, Message msg) {

		OutputStream out = null;
		ConnectionHandler remote = this.getConnectionHandler(user);

		if (remote != null) {
			out = remote.getOutput();
		}
		/* Otherwise we need to instanciate a ConnectionHandler */
		else {
			Socket clientConnection;
			try {
				clientConnection = new Socket(user.getAddress(), this.connectionListenerListeningPort);
			} catch (Exception e) {
				Logs.printerro(this.instanceName, "Could not create socket to send new message");
				return;
			}

			remote = new ConnectionHandler(user, clientConnection);	

			synchronized (this) {
				this.connectionHandlers.add(remote);
			}

			remote.start();
			/* wait for the connectionHandler to start properly and 
			 * correctly estblish IO */
			while (out == null) {
				out = remote.getOutput();
			}
		}

		if (msg instanceof MessageString) {

			String stringPacket;

			stringPacket = "S:" + msg.getContent();

			try {
				out.write(stringPacket.getBytes(), 0, stringPacket.getBytes().length);
				out.flush();
			} catch (Exception e) { e.printStackTrace(); }
		} 
		/* msg instanceof MessageFile */
		else {

			byte[] bytePacket = new byte[(int)msg.getFile().length() + 2 + msg.getFile().getName().length() + 1];
			byte[] byteFile = new byte[(int)msg.getFile().length()];

			try {

				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(msg.getFile()));

				bis.read(byteFile, 0, byteFile.length);

				System.arraycopy(("F:" + msg.getFile().getName() + ":").getBytes(), 0, bytePacket, 0, ("F:" + msg.getFile().getName() + ":").getBytes().length);
				System.arraycopy(byteFile, 0, bytePacket, ("F:" + msg.getFile().getName() + ":").getBytes().length, byteFile.length);

				out.write(bytePacket, 0, bytePacket.length);
				out.flush();
			} catch (Exception e) { e.printStackTrace(); }
		}
	}

	/* The whole process is here */
	public void run() {
		/* So that all the ConnectionHandlers are able to call NetworkManager notification methods */
		ConnectionHandler.setMaster(this);

		Logs.printinfo(this.instanceName, "Starting the Network Manager...");
		/* Start the ConnectionListener (TCP) and the NetworkSignalListener (UDP) */
		this.startAll();
		Logs.printinfo(this.instanceName, "Network Manager successfully started !");

	}
}

