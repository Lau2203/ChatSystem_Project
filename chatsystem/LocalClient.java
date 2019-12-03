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

	public LocalClient(int port) {
		super(port);
	}

	public void run() {

		/* Show Login window until successfully logged in */
		this.askLogin();
		/* User logged in successfully */

		System.out.println("Successfully logged in!");

		this.runGUI();

		this.startNetworkManager();

		while (true) {
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException ie) {}
			}

			switch (this.networkManagerInformation.getNotifyInformation()) {

				case NEW_CONNECTION:
					System.out.println("RECEIVED SIGNAL FROM NETWORK MANAGER - NEW CONNECTION");
					break;

				case END_OF_CONNECTION:
					System.out.println("RECEIVED SIGNAL FROM NETWORK MANAGER - END OF CONNECTION");
					break;

				case READY_TO_CHECK_USERNAME:
					this.setUsername();
					break;

				case USERNAME_MODIFICATION:
					break;

				case NEW_ACTIVE_USER:
					break;

				case USER_LEFT_NETWORK:
					break;

				case NEW_MESSAGE:
					System.out.println("NEW MESSAGE : '" + this.networkManagerInformation.getMessage().getContent() + "'");
					break;

				default: break;
			}
		}
	}

	private void askLogin() {
		/* This window will call Client.login() to check whether the user entered
		 * the correct password */
		ConnectionWindow cw = new ConnectionWindow(this);
		cw.setVisible(true);	

		/* Wait for the connection window to confirm the user is now connected */
		synchronized(this) {
			try {
				wait();
			} catch (InterruptedException ie) {}
		}
	}
	/* Called when first time lauching */
	private void setup() {

	}

	private void runGUI() {
		MainWindow mw = new MainWindow();
		mw.setVisible(true);
	}

	private void setUsername() {
		String username = ConfigParser.get("username");

		/* Main user's username needs to be set */
		if (username == null) {
			JFrame frame = new JFrame();
			username = JOptionPane.showInputDialog(frame, "Please enter a username:");
		}

		this.mainUser.setUsername(username);
	}

	public static void main(String[] args) {
		new LocalClient(5555).start();
	}
}
