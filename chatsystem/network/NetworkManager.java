package chatsystem.network;

import java.io.IOException;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import java.net.Socket;
import java.net.InetAddress;

import java.lang.Thread;
/* For message timestamping */
import java.sql.Timestamp;

/* Creating a new user when there is a new active client */
import chatsystem.User;
/* Reference to the main user, only for NetworkSignalListener, in order
 * for him to be able to directly respond to request with the main user's username */
import chatsystem.MainUser;
import chatsystem.Client;
import chatsystem.NotifyInformation;
import chatsystem.MessageString;

import chatsystem.util.Logs;
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

	private NetworkSignalListener nsl;

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

	public NetworkManager(Client master, MainUser mainUser, int listeningTCPPort) {

		/* ====================== CAREFULL ======================= */
		int networkSignalListenerListeningPort 	= Integer.parseInt(ConfigParser.get("nsl-port"));

		this.master = master;

		this.connectionListener		= new ConnectionListener(this, listeningTCPPort);

		this.connectionHandlers 	= new ArrayList<ConnectionHandler>();

		this.nsl 			= new NetworkSignalListener(this, mainUser, networkSignalListenerListeningPort);

		this.networkManagerInformation 	= new NetworkManagerInformation();

		this.activeUsersList 		= new ArrayList<User>();
	}

	private User getUser(String fingerprint) {
		for (User usr: this.activeUsersList) {
			if (usr.getFingerprint().equals(fingerprint))
				return usr;
		}

		return null;
	}

	/* returns a copy of the Active Users List */
	public ArrayList<User> getActiveUsersList() {
		/* Needs coordination with the NetworkSignalListener, ConnectionListener and the ConnectionHandler */
		synchronized(this.childrenLock) {
			/* Needs coordination with the NetworkManager, since it is the one that is modifying the 
			 * activeUsersList */
			synchronized(this.lock) {
				return new ArrayList<User>(this.activeUsersList);
			}
		}
	}

	/* Do not forget to add +1 for us */
	public int getActiveClientsNumber() { 
		/* Needs coordination with the NetworkSignalListener, ConnectionListener and the ConnectionHandler */
		synchronized(this.childrenLock) {
			/* Needs coordination with the NetworkManager, since it is the one that is modifying the 
			 * activeUsersList */
			synchronized(this.lock) {
				return this.activeUsersList.size() + 1;
			}
		}
	}

	public void startAll() {
		/* Once the ConnectionListener and the NetworkSignalListener are started,
		 * We want to notify the main client process */
		synchronized(this.master) {
			this.startConnectionListener();
			this.startNetworkSignalListener();
			this.master.wakeUp();
		}
	}

	private void startConnectionListener() {
		/* We wait for the ConnectionListener to wake us up, meaning that it started successfully */
		synchronized(this.lock) {
			this.connectionListener.start();
			try { this.lock.wait(); } catch (InterruptedException ie) {ie.printStackTrace();}
		}
	}

	private void startNetworkSignalListener() {
		synchronized(this.lock) {
			this.nsl.start();
			try { this.lock.wait(); } catch (InterruptedException ie) {ie.printStackTrace();}
		}
	}

	public void wakeUp() {
		synchronized(this.childrenLock) {
			synchronized(this.lock) {
				this.lock.notify();
			}
		}
	}

	/* Can only be called by the main client process */
	public synchronized void shutdown() {

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
	}

	private synchronized ArrayList<ConnectionHandler> getConnectionHandlers() { return this.connectionHandlers; }

	/* Notification methods called either by the ConnectionListener, ConnectionHandlers or the NetworkSignalListener
	 * These notifications refer to the type of information they convey. These different types can be 
	 * seen in the NotifyInformation class */

	/* Can only be called by a ConnectionHandler */
	/* Meaning that a User-to-User connection has ended */
	protected void notifyDeathOfConnectionHandler(ConnectionHandler ch) {

		synchronized(this.childrenLock) {

			try { this.semaphore.acquire(); } catch (InterruptedException ie) {ie.printStackTrace();}

			this.connectionHandlers.remove(ch);
			/* Put information for the NetworkManager to handle it */
			this.networkManagerInformation.setNotifyInformation(NotifyInformation.END_OF_CONNECTION);
			this.networkManagerInformation.setRecipientUser(ch.getRecipientUser());
			/* Wake up the NetworkManager to handle the death of the connection handler
			 * and tell the client too */
			this.wakeUp();
		}
	}
	/* Can only be called by a ConnectionHandler, since we must be connected to the remote client first */
	protected void notifyNewMessage(ConnectionHandler ch, String content) {

		synchronized(this.childrenLock) {

			try { this.semaphore.acquire(); } catch (InterruptedException ie) {ie.printStackTrace();}

			MessageString msg = new MessageString(ch.getRecipientUser(), new Timestamp(System.currentTimeMillis()));

			msg.setContent(content);

			/* Put information for the NetworkManager to handle it */
			this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_MESSAGE);
			this.networkManagerInformation.setRecipientUser(ch.getRecipientUser());
			this.networkManagerInformation.setMessage(msg);
			/* Wake up the NetworkManager to handle the death of the connection handler
			 * and tell the client too */
			this.wakeUp();
		}
	}
	/* Can only be called by the ConnectionListener */
	protected void notifyNewConnection(Socket clientConnection) {

		synchronized(this.childrenLock) {

			try { this.semaphore.acquire(); } catch (InterruptedException ie) {ie.printStackTrace();}

			ConnectionHandler ch = new ConnectionHandler(clientConnection);	

			this.connectionHandlers.add(ch);
			/* Put information for the NetworkManager to handle it */
			this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_CONNECTION);

			ch.start();
			/* Wake up the NetworkManager to handle the death of the connection handler
			 * and tell the client too */
			this.wakeUp();	
		}
	}
	/* Can only be called by the NetworkSignalListener */
	protected void notifyNewActiveClient(String fingerprint, InetAddress address) {

		synchronized(this.childrenLock) {

			try { this.semaphore.acquire(); } catch (InterruptedException ie) {ie.printStackTrace();}

			this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_ACTIVE_CLIENT);

			this.networkManagerInformation.setFingerprint(fingerprint);
			this.networkManagerInformation.setAddress(address);

			this.wakeUp();
		}
	}
	/* Can only be called by the NetworkSignalListener */
	protected void notifyNewUsername(String fingerprint, InetAddress address, String username) {

		synchronized(this.childrenLock) {

			try { this.semaphore.acquire(); } catch (InterruptedException ie) {ie.printStackTrace();}

			this.networkManagerInformation.setNotifyInformation(NotifyInformation.USERNAME_MODIFICATION);

			this.networkManagerInformation.setFingerprint(fingerprint);
			this.networkManagerInformation.setAddress(address);
			this.networkManagerInformation.setUsername(username);

			this.wakeUp();
		}
	}
	/* Can only be called by the NetworkSignalListener */
	protected void notifyReadyToCheckUsername() {

		synchronized(this.childrenLock) {

			try { this.semaphore.acquire(); } catch (InterruptedException ie) {ie.printStackTrace();}

			this.networkManagerInformation.setNotifyInformation(NotifyInformation.READY_TO_CHECK_USERNAME);

			this.wakeUp();
		}
	}

	/* Once it's been waken up, the NetworkManager needs to understand why
	 * and act consequently */
	private void handleNewInformation() {
		switch (this.networkManagerInformation.getNotifyInformation()) {

			/* Happens when a new client appears on the network.
			 * The NetworkSignalListener welcomes the new client by telling him
			 * our main user's fingerprint and username, the current number of active clients 
			 * and inherently providing our address. With every client doing so, that client 
			 * is now able fill its active users list and thus get a valid representation of
			 * what usernames aren't available. */
			case NEW_ACTIVE_CLIENT:
				System.out.println(this.instanceName + " : CREATION OF NEW USER with fingerprint " + this.networkManagerInformation.getFingerprint());
				System.out.flush();

				User new_usr = new User();
				new_usr.setFingerprint(this.networkManagerInformation.getFingerprint());
				new_usr.setAddress(this.networkManagerInformation.getAddress());

				this.activeUsersList.add(new_usr);	

				break;

			/* 2 Possibilities:
				
			 * - 	As a reaction to NEW_ACTIVE_CLIENT, the new active client received all the welcoming
			 * 	responses from all the current active clients online (he knows that since he got the number of
			 * 	active clients to wait for in each welcoming packet). He can now tell whether or not its username
			 * 	is available by looking at its active users list. He might be forced to ask the user to choose
			 * 	a different username. Once he get an available username, he needs to informa all the clients online,
			 * 	thus intercepting this USERNAME_MODIFICATION from the NetworkSignalListener.

			 * - 	An online user basically changed its username. */
			case USERNAME_MODIFICATION:
				User usr = this.getUser(this.networkManagerInformation.getFingerprint());
				if (usr == null) {
					usr = new User();
					usr.setFingerprint(this.networkManagerInformation.getFingerprint());
					usr.setAddress(this.networkManagerInformation.getAddress());
					usr.setUsername(this.networkManagerInformation.getUsername());

					this.activeUsersList.add(usr);

					this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_ACTIVE_USER);
				} else {
					/* Happens when a client was active but had not a valid username yet, and now it updates
					 * its username */
					if (usr.getUsername().equals("undefined")) {
						this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_ACTIVE_USER);						
					}

					usr.setFingerprint(this.networkManagerInformation.getFingerprint());
					usr.setAddress(this.networkManagerInformation.getAddress());
					usr.setUsername(this.networkManagerInformation.getUsername());
				}	
				/* If a client responded to us without a valid username yet, so we do not notify the main client process,
				 * since we do not consider the remote user to be active when he hasn't a valid username yet */
				if (this.networkManagerInformation.getUsername().equals("undefined")) {
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.NONE);
				}

				break;

			case READY_TO_CHECK_USERNAME:
				break;

			case NEW_CONNECTION:
				break;

			case END_OF_CONNECTION:
				break;

			case USER_LEFT_NETWORK:
				break;

			default: break;
		}

		/* We relay the information to the main client process */
		this.master.notifyFromNetworkManager(this.networkManagerInformation);
	}


	/* The whole process is here */
	public void run() {
		/* So that all the ConnectionHandlers are able to call NetworkManager notification methods */
		ConnectionHandler.setMaster(this);

		Logs.printinfo(this.instanceName, "Starting the Network Manager...");
		/* Start the ConnectionListener (TCP) and the NetworkSignalListener (UDP) */
		this.startAll();
		Logs.printinfo(this.instanceName, "Network Manager successfully started !");

		while (true) {
			/* We constantly wait for a signal from either the ConnectionListener,
			 * the ConnectionHandlers or the NetworkSignalListener */
			synchronized(this.lock) {
				this.semaphore.release();

				try { 
					this.lock.wait();
				} catch (InterruptedException ie) {ie.printStackTrace();}
				//try { this.semaphore.acquire(); } catch(InterruptedException ie) {ie.printStackTrace();}
			}
			/* Once we've been waken up, we now need to know why,
			 * and we need to process that information */
			this.handleNewInformation();
		}
	}
}

