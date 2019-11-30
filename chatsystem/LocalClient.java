package chatsystem;

import java.util.Scanner;

import java.io.IOException;

import chatsystem.network.NetworkManagerInformation;

import chatsystem.NotifyInformation;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;

public class LocalClient extends Client {

	public LocalClient(int port) {
		super(port);
	}

	public void run() {
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
}
