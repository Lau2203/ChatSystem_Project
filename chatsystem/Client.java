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

	protected static final String DEFAULT_CONFIG_FILE_PATH = "/home/cacao/INSA/ChatSystem_Project/config.cfg";
	protected static final String DEFAULT_WITNESS_FILE_PATH = "/home/cacao/INSA/ChatSystem_Project/witness";

	protected ArrayList<ChatSession> activeChatSessionList;

	protected Client(int port) {

		String witnessFilePath;

		try {
			ConfigParser.parse(Client.DEFAULT_CONFIG_FILE_PATH);
		} catch (IOException ioe) {
			System.out.println("Could not retrieve config file, aborted");
			System.exit(1);
		}

		witnessFilePath = ConfigParser.get("witness-file-path");
		if (witnessFilePath == null)
			witnessFilePath = DEFAULT_WITNESS_FILE_PATH;

		this.mainUser = new MainUser();

		this.netmanager = new NetworkManager(this, this.mainUser, port);

		this.activeChatSessionList = new ArrayList<ChatSession>();

		this.encryptionHandler = new EncryptionHandler(witnessFilePath);
	}

	protected void startNetworkManager() {

		this.netmanager.start();
		synchronized(this) {
			try { this.wait(); } catch (InterruptedException ie) {}
		}
	}

	protected void command(String cmd) {
		if 	(cmd.equals("quit")) { this.netmanager.shutdown(); System.exit(0); }
		else if (cmd.equals("logs")) { Logs.readLogs(); }
	}

	public synchronized void notifyFromNetworkManager(NetworkManagerInformation ni) {
		this.networkManagerInformation = ni;
		this.notify();
	}

	public boolean login(String input) {
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
}
