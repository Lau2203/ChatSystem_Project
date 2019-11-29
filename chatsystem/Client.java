package chatsystem;

import chatsystem.network.NetworkManager;

import chatsystem.util.Logs;

public abstract class Client {
    
	protected NetworkManager netmanager;

	protected static final String DEFAULT_CONFIG_FILE_PATH = "config.cfg";

	protected Client(int port) {

		this.netmanager = new NetworkManager(this, port);
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
}
