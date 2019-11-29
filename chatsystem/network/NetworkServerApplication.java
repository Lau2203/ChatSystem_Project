package chatsystem.network;

import java.io.IOException;

import java.util.Scanner;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;

public class NetworkServerApplication {

	private NetworkServer ns;

	private static final String DEFAULT_CONFIG_FILE_PATH = "config.cfg";

	public NetworkServerApplication(int port) {

		this.ns = new NetworkServer(this, port);
	}

	public void command(String cmd) {

		if 	(cmd.equals("quit")) { this.ns.shutdown(); System.exit(0); }
		else if (cmd.equals("logs")) { this.ns.readLogs(); }

	}

	private void startServer() {
		this.ns.start();
		synchronized(this) {
			try {
				this.wait();
			} catch (InterruptedException ie) {}
		}
	}

	public void run() throws InterruptedException {

		Scanner cli = new Scanner(System.in);	
		String cmd;

		this.startServer();

		while (true) {
			System.out.print(">>> ");
			cmd = cli.nextLine();	
			this.command(cmd);
		}
	}

	public static void main(String[] args) {

		NetworkServerApplication nsa;
		String serverPort, logFilePath;
		boolean isLogEnabled = true;

		try {
			if (args.length == 0) {
				ConfigParser.parse(NetworkServerApplication.DEFAULT_CONFIG_FILE_PATH);
			} else {
				ConfigParser.parse(args[0]);
			}
		} catch (IOException ioe) {}

		logFilePath = ConfigParser.get("logfile");
		serverPort = ConfigParser.get("server-port");

		if (logFilePath != null) {
			try {
				Logs.init(isLogEnabled, logFilePath);
			} catch (IOException ioe) {
				/* Log init failed, logs filling is just abandonned */
				System.out.println("[x] ERROR while initializing log file, logs filling is abandonned.");
			}
		}

		if (serverPort == null)
			nsa = new NetworkServerApplication(0);	
		else
			nsa = new NetworkServerApplication(Integer.parseInt(serverPort));	
	

		try {
			nsa.run();
		} catch (InterruptedException ie) {
			System.out.println("[x] Issue with threads between server and application...");
		}
	}
}
