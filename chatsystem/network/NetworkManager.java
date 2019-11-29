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

import chatsystem.util.ConfigParser;

public class NetworkManager extends Thread {

	private Client master;

	private ConnectionListener colistener;

	private ArrayList<ConnectionHandler> cohandlers;

    private NetworkManagerInformation networkManagerInformation;

    private ArrayList<User> activeUsersList;

	public NetworkManager(Client master, int listeningTCPPort) {

		this.master = master;
		this.colistener = new ConnectionListener(this, listeningTCPPort);
		
		this.cohandlers = new ArrayList<ConnectionHandler>();

        this.networkManagerInformation = new NetworkManagerInformation();

        this.activeUsersList = new ArrayList<User>();

        //String hostname = ConfigParser.get("main-user");
        //this.activeUsersList.add(new User("", "main-user", InetAddress.getByName(hostname)));
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

	private synchronized ArrayList<ConnectionHandler> getConnectionHandlers() {
		return this.cohandlers;
	}

	protected synchronized void notifyDeathOfConnectionHandler(ConnectionHandler ch) {
		this.cohandlers.remove(ch);
        /* Handle new information */
        this.networkManagerInformation.setToBeNotified(ch);
        this.networkManagerInformation.setNotifyInformation(NotifyInformation.END_OF_CONNECTION);
        this.networkManagerInformation.setRecipientUser(ch.getRecipientUser());
        /* Wake NetworkManager to handle the death of the connection handler
         * and tell the client too */
        this.notify();
	}

    protected synchronized void notifyNewMessage(ConnectionHandler ch, String content) {

        MessageString msg = new MessageString(ch.getRecipientUser(), new Timestamp(System.currentTimeMillis()));

        msg.setContent(content);

        /* Handle new information */
        this.networkManagerInformation.setToBeNotified(ch);
        this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_MESSAGE);
        this.networkManagerInformation.setRecipientUser(ch.getRecipientUser());
        this.networkManagerInformation.setMessage(msg);
        /* Wake NetworkManager to handle the death of the connection handler
         * and tell the client too */
        this.notify();
    }

	protected synchronized void notifyNewConnection(Socket clientConnection) {
		ConnectionHandler ch = new ConnectionHandler(clientConnection);	

		this.cohandlers.add(ch);
        /* Handle new information */
        this.networkManagerInformation.setToBeNotified(this.colistener);
        this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_CONNECTION);
        
        ch.start();
        /* Wake NetworkManager to handle the death of the connection handler
         * and tell the client too */
        this.notify();	
	}

    

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

            case USER_LEFT:
                break;

            default: break;
        }

        this.master.notifyFromNetworkManager(this.networkManagerInformation);

        synchronized(this.networkManagerInformation.getToBeNotified()) {
            /* Wake the information provider (either ConnectionListener, ConnectionHandler or Client) */
            this.networkManagerInformation.getToBeNotified().notify();
        }
    }

	public void run() {

        ConnectionHandler.setMaster(this);

		this.startAll();

        while (true) {
            synchronized(this) {
                try { 
                    wait();
                } catch (InterruptedException ie) {}
            }

            this.handleNewInformation();
        }
	}
}

