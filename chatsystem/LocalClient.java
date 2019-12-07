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

public class LocalClient extends Client {

	MainWindow mw;

	public LocalClient(int port) {
		super(port);
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
					this.mw.notifyUserActivityModification();
					break;

				case NEW_ACTIVE_USER:
					this.mw.notifyUserActivityModification();
					break;

				case USER_LEFT_NETWORK:
					break;

				case NEW_MESSAGE:
					System.out.println("NEW MESSAGE : '" + this.networkManagerInformation.getMessage().getContent() + "'");
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

		/* Main user's username needs to be set */
		if (username == null || username.equals("undefined") || !this.isUsernameAvailable(username)) {
			JFrame frame = new JFrame();
			do {
				username = JOptionPane.showInputDialog(frame, "Your current username is not valid\nPlease enter a new username:", "Username not valid", JOptionPane.INFORMATION_MESSAGE);
			} while(username == null || username.equals("") || username.equals(this.mainUser.getFingerprint()) || !this.isUsernameAvailable(username));
		}

		this.mainUser.setUsername(username);
		this.notifyNewUsernameToBeSent();
		ConfigParser.updateSetting("username", username);
		this.mw.notifyNewMainUserUsername(username);
	}

	public static void main(String[] args) {
		(new LocalClient(5555)).start();
	}
}
