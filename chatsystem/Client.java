package chatsystem;

import java.util.ArrayList;
import java.util.Scanner;

import java.lang.Thread;

import java.io.IOException;

import chatsystem.MainUser;

import chatsystem.network.NetworkManager;
import chatsystem.network.NetworkManagerInformation;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;
import chatsystem.util.EncryptionHandler;

public abstract class Client extends Thread {

	protected MainUser mainUser;

	protected NetworkManager netmanager;

	protected NetworkManagerInformation networkManagerInformation;

	protected EncryptionHandler encryptionHandler;

	protected static final String DEFAULT_CONFIG_FILE_PATH = "../config.cfg";
	protected static final String DEFAULT_WITNESS_FILE_PATH = "../witness";

	protected ArrayList<ChatSession> activeChatSessionList;

	protected String logFilePath;
	protected String witnessFilePath;
	protected boolean isLogEnabled;

	protected String instanceName = "Client";

	protected String lock = new String();

	protected Client(int connectionListenerPort) {

		this.initWithConfigFile();

		this.mainUser 			= new MainUser();

		this.netmanager 		= new NetworkManager(this, this.mainUser, connectionListenerPort);

		this.networkManagerInformation	= new NetworkManagerInformation();

		this.activeChatSessionList 	= new ArrayList<ChatSession>();

		this.encryptionHandler 		= new EncryptionHandler(this.witnessFilePath);
	}

	private void initWithConfigFile() {

		String isLogEnabledString;

		this.parseConfigFile();

		this.logFilePath 	= ConfigParser.get("logfile");	
		this.witnessFilePath 	= ConfigParser.get("witness-file-path");
		isLogEnabledString	= ConfigParser.get("log-filling");


		if (this.witnessFilePath == null)
			this.witnessFilePath = DEFAULT_WITNESS_FILE_PATH;

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

	public synchronized void notifyFromNetworkManager(NetworkManagerInformation ni) {
		/* Make a copy */
		this.networkManagerInformation.setRecipientUser(ni.getRecipientUser());
		this.networkManagerInformation.setNotifyInformation(ni.getNotifyInformation());
		this.networkManagerInformation.setFingerprint(ni.getFingerprint());
		this.networkManagerInformation.setUsername(ni.getUsername());
		this.networkManagerInformation.setAddress(ni.getAddress());
		this.networkManagerInformation.setMessage(ni.getMessage());
		this.wakeUp();
	}

	public synchronized boolean login(String input) {
		/*
		Scanner userInput = new Scanner(System.in);
		String input_key;

		System.out.println("Key = bonjouraurelienm");
		System.out.println("ENCRYPTED : " + this.encryptionHandler.encrypt("bonjouraurelienm", "THAT IS COOL"));

		while (true) {
			System.out.print("Login key : ");
			input_key = userInput.nextLine();

			if (this.encryptionHandler.testWitnessFile(input_key))
				break;
		}
		*/
		this.mainUser.setFingerprint(this.encryptionHandler.getFingerprint(input));	
		return this.encryptionHandler.testWitnessFile(input);

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
		synchronized(this.netmanager) {
			return this.netmanager.getActiveUsersList();
		}
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
}
