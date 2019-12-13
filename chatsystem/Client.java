package chatsystem;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import java.net.InetAddress;

import java.lang.Thread;

import java.io.IOException;

import java.sql.Timestamp;

import chatsystem.User;
import chatsystem.MainUser;
import chatsystem.MessageString;
import chatsystem.MessageHistoryManager;

import chatsystem.network.NetworkManager;
import chatsystem.network.NetworkManagerInformation;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;
import chatsystem.util.EncryptionHandler;

public abstract class Client extends Thread {

	protected ArrayList<User> userList;

	protected MainUser mainUser;

	protected NetworkManager netmanager;

	protected NetworkManagerInformation networkManagerInformation;

	protected EncryptionHandler 	encryptionHandler;
	protected MessageHistoryManager messageHistoryManager;

	private static final String DEFAULT_MESSAGE_HISTORY_FILE_PATH 	= "../history.mh";
	private static final String DEFAULT_CONFIG_FILE_PATH 		= "../config.cfg";
	private static final String DEFAULT_WITNESS_FILE_PATH 		= "../witness2";

	private static final int DEFAULT_CONNECTION_LISTENER_PORT 	= 5555;
	private static final int DEFAULT_NETWORK_SIGNAL_LISTENER_PORT 	= 54321;

	protected boolean isLogEnabled;

	private String logFilePath;
	private String witnessFilePath;
	private String messageHistoryFilePath;

	private int connectionListenerPort;
	private int networkSignalListenerPort;

	protected String instanceName = "Client";

	/* Lock objects for synchronized operationis */
	protected String lock 		= new String();
	protected String childrenLock 	= new String();

	protected Semaphore semaphore = new Semaphore(0, true);

	protected Client() {

		this.initWithConfigFile();

		this.mainUser 			= new MainUser();

		this.netmanager 		= new NetworkManager(this, this.mainUser, this.connectionListenerPort, this.networkSignalListenerPort);

		this.networkManagerInformation	= new NetworkManagerInformation();

		this.encryptionHandler 		= new EncryptionHandler(this.witnessFilePath);
		this.messageHistoryManager	= new MessageHistoryManager(this, this.messageHistoryFilePath);

		this.userList = new ArrayList<User>();

		this.messageHistoryManager.fetchMessageHistory();
	}

	private void initWithConfigFile() {

		String isLogEnabledString;
		String clp, nslp;

		this.parseConfigFile();

		isLogEnabledString		= ConfigParser.get("log-filling");

		this.logFilePath 		= ConfigParser.get("logfile");	
		this.messageHistoryFilePath 	= ConfigParser.get("message-history-path");	
		this.witnessFilePath 		= ConfigParser.get("witness-file-path");

		/* Fetch the ConnectionListener port and the NetworkSignalListener port */
		clp 	= ConfigParser.get("server-port");
		nslp 	= ConfigParser.get("nsl-port");


		if (this.witnessFilePath == null)
			this.witnessFilePath = DEFAULT_WITNESS_FILE_PATH;

		if (this.messageHistoryFilePath == null)
			this.messageHistoryFilePath = DEFAULT_MESSAGE_HISTORY_FILE_PATH;

		if (clp == null) {
			this.connectionListenerPort = DEFAULT_CONNECTION_LISTENER_PORT;
		} else {
			this.connectionListenerPort = Integer.parseInt(clp);
		}

		if (nslp == null) {
			this.networkSignalListenerPort = DEFAULT_NETWORK_SIGNAL_LISTENER_PORT;
		} else {
			this.networkSignalListenerPort = Integer.parseInt(nslp);
		}

		if (isLogEnabledString == null || !isLogEnabledString.equals("true")) {
			this.isLogEnabled = false;
		} else {
			this.isLogEnabled = true;
		}

		this.prepareLogs();
	}

	protected void startNetworkManager() {
		this.netmanager.run();
	}

	public synchronized void shutdown() {
		this.messageHistoryManager.saveMessageHistory();
		this.netmanager.shutdown();
		System.exit(1);
	}



	public synchronized boolean login(String input) {
		boolean loggedin = false;
		synchronized(this.childrenLock) {
			this.mainUser.setFingerprint(this.encryptionHandler.getFingerprint(input));	
			loggedin = this.encryptionHandler.testWitnessFile(input);
		}
		return loggedin;
	}

	private void parseConfigFile() {
		try {
			ConfigParser.parse(Client.DEFAULT_CONFIG_FILE_PATH);
		} catch (IOException ioe) {
			Logs.printerro(this.instanceName, "Could not retrieve configuration file, aborted");
			System.exit(1);
		}
	}

	private void prepareLogs() {
		if (this.logFilePath != null) {
			try {
				Logs.init(this.isLogEnabled, this.logFilePath);
			} catch (IOException ioe) {
				/* Log init failed, logs filling is just abandonned */
				System.out.println("[x] Error while initializing log file, logs filling is abandonned");
			}
		}
	}

	public ArrayList<User> getActiveUsersList() {
		return this.netmanager.getActiveUsersList();
	}

	public synchronized ArrayList<User> getUserList() {
		return new ArrayList<User>(this.userList);
	}

	public synchronized User getUserByAddress(InetAddress address) {
		/* Find an existing one */
		for (User usr: this.userList) {
			if (usr.getAddress().equals(address)) {
				return usr;
			}	
		}
		/* Otherwise create a new one */	
		/*
		User newUser = new User(fingerprint);
		this.userList.add(newUser);
		return newUser;
		*/
		return null;
	}

	public synchronized User getUser(String fingerprint) {
		/* Find an existing one */
		for (User usr: this.userList) {
			if (usr.getFingerprint().equals(fingerprint)) {
				return usr;
			}	
		}
		/* Otherwise create a new one */	
		User newUser = new User(fingerprint);
		this.userList.add(newUser);
		return newUser;
	}

	public boolean isUsernameAvailable(String username) {
		for (User user: this.getUserList()) {
			if (user.isActive() && user.getUsername() != null && user.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}

	/* Returns whether or not it was able to set that new username */
	public synchronized boolean setNewUsername(String username) {
		if (username == null || username.equals("undefined") || !this.isUsernameAvailable(username)) {
			return false;
		}

		this.mainUser.setUsername(username);
		ConfigParser.updateSetting("username", username);

		return true;
	}

	public synchronized String getMainUserUsername() {
		return this.mainUser.getUsername();
	}

	public synchronized User getMainUser() {
		return this.mainUser;
	}

	public void notifyNewUsername(String fingerprint, String newUsername) {}
	public void notifyReadyToCheckUsername() {}
	public void notifyNewActiveUser(String fingerprint, InetAddress address, String username) {}
	public void notifyNewMessage(User recipient, Message msg) {}
	public void notifyNewUsernameToBeSent() {}
	public void notifyNewMessageToBeSent(String content, User recipient) {}
}
