package chatsystem;

import java.util.Scanner;

import java.io.IOException;

import java.net.InetAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.sql.Timestamp;

import chatsystem.network.NetworkManagerInformation;

import chatsystem.NotifyInformation;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;

import chatsystem.gui.ConnectionWindow;
import chatsystem.gui.MainWindow;
import chatsystem.gui.NewIDWindow;

public class LocalClient extends Client {

	MainWindow mw;

	public LocalClient() {
		super();
	}

	public LocalClient(String configFilePath) {
		super(configFilePath);
	}

	public void run() {
		this.askLogin();
	}

	public void notifyLoginSuccessful() {
		this.runGUI();
		this.startNetworkManager();
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

		boolean usernameWasModified = false;
		String username = ConfigParser.get("username");

		/* If the Main User's username need to be set */
		if (!this.setNewUsername(username)) {
			this.mw.setVisible(false);

			NewIDWindow nidw = new NewIDWindow(this);
			nidw.setVisible(true);

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

	public void notifyNewMessage(User recipient, Message msg) {
		System.out.println("NEW MESSAGE : '" + msg.getContent() + "'");

		recipient.getMessageHistory().addMessage(msg);

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

	/* Update the GUI */
	public static void main(String[] args) {
		if (args.length != 0) {
			(new LocalClient(args[0])).run();
		} else {
			(new LocalClient()).run();
		}
	}
}
