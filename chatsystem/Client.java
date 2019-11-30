package chatsystem;

import java.util.ArrayList;

import java.lang.Thread;

import chatsystem.network.NetworkManager;
import chatsystem.network.NetworkManagerInformation;

import chatsystem.util.Logs;

public abstract class Client extends Thread {

	protected NetworkManager netmanager;

	protected NetworkManagerInformation networkManagerInformation;

	protected static final String DEFAULT_CONFIG_FILE_PATH = "config.cfg";

	protected ArrayList<ChatSession> activeChatSessionList;

	protected Client(int port) {

		this.netmanager = new NetworkManager(this, port);

		this.activeChatSessionList = new ArrayList<ChatSession>();
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
}
