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

	public void run() {
		this.askLogin();
	}

	public synchronized void notifyLoginSuccessful() {
		this.runGUI();
		this.startNetworkManager();
	}

	private synchronized void askLogin() {
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

		}

		this.notifyNewUsernameToBeSent();
	}


	public void notifyNewUsernameToBeSent() {
		this.netmanager.notifyNewUsernameToBeSent(this.mainUser.getFingerprint(), this.mainUser.getUsername());
		this.mw.setVisible(true);

		synchronized(this.userList) {
			this.mw.notifyNewMainUserUsername();
		}
	}

	public void notifyNewMessageToBeSent(String content, User recipient) {

		MessageString msg = new MessageString(recipient, new Timestamp(System.currentTimeMillis()), false);

		msg.setContent(content);

		recipient.getMessageHistory().addMessage(msg);

		this.netmanager.notifyNewMessageToBeSent(recipient, msg);

		synchronized(this.userList) {
			this.mw.notifyNewMessage(recipient);
		}
	}

	public void notifyNewMessage(User recipient, Message msg) {
		System.out.println("NEW MESSAGE : '" + msg.getContent() + "'");

		recipient.getMessageHistory().addMessage(msg);

		synchronized(this.userList) {
			this.mw.notifyNewMessage(recipient);
		}
	}

	public void notifyReadyToCheckUsername() {
		this.mandatorySetUsername();
	}

	public void notifyNewUsername(String fingerprint, String newUsername) {
		User usr = this.getUser(fingerprint);
		String previousUsername = usr.getUsername();

		usr.setUsername(newUsername);

		/* If the user just got a valid username and didn't have one before */
		if ((previousUsername == null || previousUsername.equals("undefined")) && !newUsername.equals("undefined")) {
			synchronized(this.userList) {
				this.mw.notifyNewUserUsername(usr);
			}
		}

		System.out.println("NEW USERNAME : " + newUsername);
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
		(new LocalClient()).run();
	}
}
