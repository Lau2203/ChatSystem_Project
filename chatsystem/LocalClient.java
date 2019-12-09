package chatsystem;

import java.util.Scanner;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.net.InetAddress;

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
		this.startNetworkManager();
		this.runGUI();
	}

	private void askLogin() {
		/* This window will call Client.login() to check whether the user entered
		 * the correct password */
		ConnectionWindow cw = new ConnectionWindow(this);
		cw.setVisible(true);	

		/* Wait for the connection window to confirm the user is now connected */
		synchronized(this.lock) {
			try {
				this.lock.wait();
			} catch (InterruptedException ie) {ie.printStackTrace();}
		}
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

			/* Wait for the connection window to confirm the user is now connected */
			synchronized(this.lock) {
				try {
					this.lock.wait();
				} catch (InterruptedException ie) {ie.printStackTrace();}
			}

			this.mw.setVisible(true);
		}

		this.mw.notifyNewMainUserUsername();
	}


	public void notifyNewUsernameToBeSent() {
		this.netmanager.notifyNewUsernameToBeSent(this.mainUser.getFingerprint(), this.mainUser.getUsername());
		this.mw.setVisible(true);
		this.mw.notifyNewMainUserUsername();
	}

	public void notifyNewMessageToBeSent(User recipient, Message msg) {
		User user = recipient;

		user.getMessageHistory().addMessage(msg);

		this.netmanager.notifyNewMessageToBeSent(user, msg);

		this.mw.notifyNewMessage(user);
	}

	public void notifyNewMessage(User recipient, Message msg) {
		System.out.println("NEW MESSAGE : '" + this.networkManagerInformation.getMessage().getContent() + "'");

		User recipient = this.getUser(this.networkManagerInformation.getFingerprint());

		recipient.getMessageHistory().addMessage(this.networkManagerInformation.getMessage());

		this.mw.notifyNewMessage(recipient);
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
			this.mw.notifyNewUserUsername(usr);
		}
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
		(new LocalClient()).start();
	}
}
