package chatsystem;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import java.lang.Thread;

import java.io.IOException;

import chatsystem.MainUser;
import chatsystem.MessageHistoryManager;

import chatsystem.network.NetworkManager;
import chatsystem.network.NetworkManagerInformation;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;
import chatsystem.util.EncryptionHandler;

public abstract class Client extends Thread {

	protected MainUser mainUser;

	protected NetworkManager netmanager;

	protected NetworkManagerInformation networkManagerInformation;

	protected EncryptionHandler 	encryptionHandler;
	protected MessageHistoryManager messageHistoryManager;

	private static final String DEFAULT_MESSAGE_HISTORY_FILE_PATH 	= "../history.mh";
	private static final String DEFAULT_CONFIG_FILE_PATH 		= "../config.cfg";
	private static final String DEFAULT_WITNESS_FILE_PATH 		= "../witness";

	private static int DEFAULT_CONNECTION_LISTENER_PORT 	= 5555;
	private static int DEFAULT_NETWORK_SIGNAL_LISTENER_PORT = 54321;

	protected ArrayList<ChatSession> activeChatSessionList;

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

		this.activeChatSessionList 	= new ArrayList<ChatSession>();

		this.encryptionHandler 		= new EncryptionHandler(this.witnessFilePath);
		this.messageHistoryManager	= new MessageHistoryManager(this.messageHistoryFilePath);
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
		synchronized(this.lock) {
			this.netmanager.start();
			try { this.lock.wait(); } catch (InterruptedException ie) {ie.printStackTrace();}
		}
	}

	public synchronized void wakeUp() {
		synchronized(this.lock) {
			this.lock.notify();
		}
	}

	protected void command(String cmd) {
		if 	(cmd.equals("quit")) { this.netmanager.shutdown(); System.exit(0); }
		else if (cmd.equals("logs")) { Logs.readLogs(); }
	}

	public void notifyFromNetworkManager(NetworkManagerInformation ni) {
		synchronized(this.childrenLock) {
			try {this.semaphore.acquire();} catch (InterruptedException ie) {ie.printStackTrace();}
			/* Make a copy */
			switch (ni.getNotifyInformation()) {
				case NEW_CONNECTION:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_CONNECTION);
					break;
				case END_OF_CONNECTION:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.END_OF_CONNECTION);
					break;
				case NEW_ACTIVE_CLIENT:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_ACTIVE_CLIENT);
					break;
				case READY_TO_CHECK_USERNAME:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.READY_TO_CHECK_USERNAME);
					break;
				case USERNAME_MODIFICATION:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.USERNAME_MODIFICATION);
					break;
				case NEW_ACTIVE_USER:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_ACTIVE_USER);
					break;
				case USER_LEFT_NETWORK:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.USER_LEFT_NETWORK);
					break;
				case NEW_MESSAGE:
					this.networkManagerInformation.setNotifyInformation(NotifyInformation.NEW_MESSAGE);
					break;
				default: break;
			}

			this.networkManagerInformation.setRecipientUser(ni.getRecipientUser());
			this.networkManagerInformation.setFingerprint(ni.getFingerprint());
			this.networkManagerInformation.setUsername(ni.getUsername());
			this.networkManagerInformation.setAddress(ni.getAddress());
			this.networkManagerInformation.setMessage(ni.getMessage());
			this.wakeUp();
		}
	}

	public void notifyNewUsernameToBeSent() {
		this.netmanager.notifyNewUsernameToBeSent(this.mainUser.getFingerprint(), this.mainUser.getUsername());
	}

	public boolean login(String input) {
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

	public boolean isUsernameAvailable(String username) {
		ArrayList<User> users = this.getActiveUsersList();
		for (User user: users) {
			System.out.println("\t\tUSER : '" + user.getUsername() + "'");
			if (user.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}

	/* Returns whether or not it was able to set that new username */
	public boolean setNewUsername(String username) {
		if (!this.isUsernameAvailable(username))	
			return false;

		this.mainUser.setUsername(username);
		this.notifyNewUsernameToBeSent();
		ConfigParser.updateSetting("username", username);

		return true;
	}

	public synchronized String getMainUserUsername() {
		return this.mainUser.getUsername();
	}
}
