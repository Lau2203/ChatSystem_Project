package chatsystem;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import java.net.InetAddress;

import java.lang.Thread;

import java.io.IOException;
import java.io.File;

import java.sql.Timestamp;

import chatsystem.User;
import chatsystem.MainUser;
import chatsystem.MessageString;
import chatsystem.MessageFile;
import chatsystem.MessageHistoryManager;

import chatsystem.network.NetworkManager;
import chatsystem.network.NetworkManagerInformation;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;
import chatsystem.util.EncryptionHandler;

import chatsystem.gui.ConnectionWindow;
import chatsystem.gui.MainWindow;
import chatsystem.gui.NewIDWindow;

public abstract class Client extends Thread {

	protected ArrayList<User> userList;

	protected MainUser mainUser;

	protected MainWindow mw;

	protected NetworkManager netmanager;

	protected NetworkManagerInformation networkManagerInformation;

	protected EncryptionHandler 	encryptionHandler;
	protected MessageHistoryManager messageHistoryManager;

	private static final String DEFAULT_MESSAGE_HISTORY_FILE_PATH 	= "../history.mh";
	private static final String DEFAULT_CONFIG_FILE_PATH 		= "../config.cfg";
	private static final String DEFAULT_WITNESS_FILE_PATH 		= "../witness";

	private static final int DEFAULT_CONNECTION_LISTENER_PORT 	= 5555;
	private static final int DEFAULT_NETWORK_SIGNAL_LISTENER_PORT 	= 54321;

	protected boolean isLogEnabled;

	private String configFilePath;
	private String logFilePath;
	private String witnessFilePath;
	private String messageHistoryFilePath;
	private String remoteServerURL;

	private int connectionListenerPort;
	private int networkSignalListenerPort;

	protected String instanceName = "Client";

	/* Lock objects for synchronized operationis */
	protected String lock 		= new String();
	protected String childrenLock 	= new String();

	protected Semaphore semaphore = new Semaphore(0, true);

	protected Client() {

		this.configFilePath = Client.DEFAULT_CONFIG_FILE_PATH;

		this.initWithConfigFile();

		this.mainUser 			= new MainUser();

		try {
			this.netmanager 		= new NetworkManager(this, this.mainUser, this.connectionListenerPort, this.networkSignalListenerPort, this.remoteServerURL);
		} catch (Exception e) {
			System.out.println("Unable to retrieve remote server address");
			System.exit(1);
		}

		this.networkManagerInformation	= new NetworkManagerInformation();

		this.encryptionHandler 		= new EncryptionHandler(this.witnessFilePath);
		this.messageHistoryManager	= new MessageHistoryManager(this, this.messageHistoryFilePath);

		this.userList = new ArrayList<User>();

		this.messageHistoryManager.fetchMessageHistory();
	}

	protected Client(String configFilePath) {

		this.configFilePath = configFilePath;

		this.initWithConfigFile();

		this.mainUser 			= new MainUser();

		try {
			this.netmanager 		= new NetworkManager(this, this.mainUser, this.connectionListenerPort, this.networkSignalListenerPort, this.remoteServerURL);
		} catch (Exception e) {
			System.out.println("Unable to retrieve remote server address");
			System.exit(1);
		}

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

		this.logFilePath 		= ConfigParser.get("log-file");	
		this.messageHistoryFilePath 	= ConfigParser.get("message-history");	
		this.witnessFilePath 		= ConfigParser.get("witness-file");
		this.remoteServerURL		= ConfigParser.get("remote-server-url");

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

	public void shutdown() {
		this.messageHistoryManager.saveMessageHistory();
		this.netmanager.shutdown();
		System.exit(1);
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
			ConfigParser.parse(this.configFilePath);
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

	public ArrayList<User> getUserList() {
		return new ArrayList<User>(this.userList);
	}

	public User getUserByAddress(InetAddress address) {
		/* Find an existing one */
		for (User usr: this.userList) {
			if (usr.getAddress() != null && usr.getAddress().equals(address)) {
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

	public User getUser(String fingerprint) {
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
	public boolean setNewUsername(String username) {
		if (username == null || username.equals("undefined") || !this.isUsernameAvailable(username)) {
			return false;
		}

		this.mainUser.setUsername(username);
		ConfigParser.updateSetting("username", username);

		return true;
	}

	public String getMainUserUsername() {
		return this.mainUser.getUsername();
	}

	public User getMainUser() {
		return this.mainUser;
	}




/* ============================= CAREFULL COPYING ============================ */







	public void run() {
		this.askLogin();
	}

	public void notifyLoginSuccessful() {
		this.runGUI();
		this.startNetworkManager();
		this.mandatorySetUsername();
	}

	private void askLogin() {
		/* This window will call Client.login() to check whether the user entered
		 * the correct password */
		ConnectionWindow cw = new ConnectionWindow(this);
		cw.setVisible(true);	
	}
	/* Called when first time lauching */
	private void setup() {

	}

	private void runGUI() {
		this.mw = new MainWindow(this);
		this.mw.setVisible(true);
	}

	/* Function for mandatory setting the username
	 * Open JOptionPane until a valid username is entered */
	private void mandatorySetUsername() {

		String username = ConfigParser.get("username");

		/* If the Main User's username need to be set */
		if (!this.setNewUsername(username)) {
			this.mw.dispose();

			NewIDWindow nidw = new NewIDWindow(this);
			nidw.setVisible(true);
			nidw.toFront();

		} else {
			this.notifyNewUsernameToBeSent();
		}
	}


	public void notifyNewUsernameToBeSent() {
		System.out.println("main user username : " + this.mainUser.getUsername() + "END");

		this.mw.setVisible(true);

		this.mw.notifyNewMainUserUsername();

		this.netmanager.notifyNewUsernameToBeSent(this.mainUser.getFingerprint(), this.mainUser.getUsername());
	}

	public void notifyNewMessageToBeSent(String content, User recipient) {

		MessageString msg = new MessageString(recipient, new Timestamp(System.currentTimeMillis()), false);

		msg.setContent(content);

		MessageHistory mh = recipient.getMessageHistory();

		if (mh == null) {
			mh = new MessageHistory(recipient);
			recipient.setMessageHistory(mh);
			this.messageHistoryManager.addMessageHistory(mh);
		}

		mh.addMessage(msg);

		this.mw.notifyNewMessage(recipient);
		this.netmanager.notifyNewMessageToBeSent(recipient, msg);
	}

	public void notifyNewFileToBeSent(File file, User recipient) {

		MessageFile msg = new MessageFile(recipient, new Timestamp(System.currentTimeMillis()), false);

		msg.setContent(file);

		MessageHistory mh = recipient.getMessageHistory();

		if (mh == null) {
			mh = new MessageHistory(recipient);
			recipient.setMessageHistory(mh);
			this.messageHistoryManager.addMessageHistory(mh);
		}

		mh.addMessage(msg);

		this.mw.notifyNewMessage(recipient);
		this.netmanager.notifyNewMessageToBeSent(recipient, msg);
	}

	public void notifyNewMessage(User recipient, Message msg) {
		System.out.println("NEW MESSAGE : '" + msg.getContent() + "'");

		MessageHistory mh = recipient.getMessageHistory();

		if (mh == null) {
			mh = new MessageHistory(recipient);
			recipient.setMessageHistory(mh);
			this.messageHistoryManager.addMessageHistory(mh);
		}
		
		mh.addMessage(msg);

		this.mw.notifyNewMessage(recipient);
	}

	public void notifySetNewUsername() {
		this.mandatorySetUsername();
	}

	public void notifyNewUsername(String fingerprint, String newUsername) {
		User usr = this.getUser(fingerprint);

		usr.setUsername(newUsername);

		/* If the user just got a valid username and didn't have one before */
		/*
		if ((previousUsername == null || previousUsername.equals("undefined")) && !newUsername.equals("undefined")) {
			this.mw.notifyNewUserUsername(usr);
		}
		*/
		this.mw.notifyNewUserUsername(usr);
	}

	public void notifyNewActiveUser(String fingerprint, InetAddress address, String username) {
		User active = this.getUser(fingerprint);

		active.setActive(true);
		active.setFingerprint(fingerprint);
		active.setUsername(username);
		active.setAddress(address);
	}

	public void notifyEndOfActiveUser(String fingerprint) {
		User usr = this.getUser(fingerprint);

		usr.setActive(false);

		this.mw.notifyUserActivityModification(usr);
	}
	




/* ============================= END OF CAREFULL COPYING ============================ */





	/*
	public void notifyNewUsername(String fingerprint, String newUsername) {}
	public void notifySetNewUsername() {}
	public void notifyNewActiveUser(String fingerprint, InetAddress address, String username) {}
	public void notifyEndOfActiveUser(String fingerprint) {}
	public void notifyNewMessage(User recipient, Message msg) {}
	public void notifyNewUsernameToBeSent() {}
	public void notifyNewMessageToBeSent(String content, User recipient) {}
	*/
}
