package chatsystem;

import java.util.Scanner;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

		/* Show Login window until successfully logged in */
		this.askLogin();
		/* User logged in successfully */

		System.out.println("Successfully logged in!");

		synchronized(this.lock) {
			this.startNetworkManager();
		}

		this.runGUI();

		while (true) {
			synchronized(this.lock) {
				this.semaphore.release();
				try {
					this.lock.wait();
				} catch (InterruptedException ie) {}
			}

			switch (this.networkManagerInformation.getNotifyInformation()) {

				case NEW_CONNECTION:
					System.out.println("RECEIVED SIGNAL FROM NETWORK MANAGER - NEW CONNECTION");
					break;

				case END_OF_CONNECTION:
					Logs.println("RECEIVED SIGNAL FROM NETWORK MANAGER - END OF CONNECTION");
					break;

				case READY_TO_CHECK_USERNAME:
					this.mandatorySetUsername();
					break;

				case USERNAME_MODIFICATION:
					User usr = this.getUser(this.networkManagerInformation.getFingerprint());
					String previousUsername = usr.getUsername();
					String newUsername = this.networkManagerInformation.getUsername();

					usr.setUsername(newUsername);
					usr.setAddress(this.networkManagerInformation.getAddress());
	
					/* If the user just got a valid username and didn't have one before */
					if ((previousUsername == null || previousUsername.equals("undefined")) && !newUsername.equals("undefined")) {
						this.mw.notifyNewUserUsername(usr);
					}
					break;

				case NEW_ACTIVE_USER:
					User active = this.getUser(this.networkManagerInformation.getFingerprint());

					active.setActive(true);
					active.setFingerprint(this.networkManagerInformation.getFingerprint());
					active.setUsername(this.networkManagerInformation.getUsername());
					active.setAddress(this.networkManagerInformation.getAddress());

					/* No need to notify the GUI since its username is not defined yet */
					/* Notify the GUI */
					//this.mw.notifyUserActivityModification();
					break;

				case USER_LEFT_NETWORK:
					break;

				case NEW_MESSAGE:
					System.out.println("NEW MESSAGE : '" + this.networkManagerInformation.getMessage().getContent() + "'");

					User recipient = this.getUser(this.networkManagerInformation.getFingerprint());

					recipient.getMessageHistory().addMessage(this.networkManagerInformation.getMessage());

					this.mw.notifyNewMessage(recipient);
					break;

				case NEW_MESSAGE_TO_BE_SENT:
					User user = this.networkManagerInformation.getRecipientUser();
					Message msg = this.networkManagerInformation.getMessage();

					user.getMessageHistory().addMessage(msg);

					this.netmanager.notifyNewMessageToBeSent(user, msg);

					this.mw.notifyNewMessage(user);

					break;

				case NEW_USERNAME_TO_BE_SENT:
					this.netmanager.notifyNewUsernameToBeSent(this.mainUser.getFingerprint(), this.mainUser.getUsername());
					this.mw.setVisible(true);
					this.mw.notifyNewMainUserUsername();
					break;

				default: break;
			}
			Logs.println("LOCAL CLIENT : RECEIVED '" + this.networkManagerInformation.getNotifyInformation() + "' signal");
		}
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
			/*
			synchronized(this.lock) {
				try {
					this.lock.wait();
				} catch (InterruptedException ie) {ie.printStackTrace();}
			}

			this.mw.setVisible(true);
			*/
		}
		/*

		this.mw.notifyNewMainUserUsername();
		*/
	}

	/* Update the GUI */
	public static void main(String[] args) {
		(new LocalClient()).start();
	}
}
