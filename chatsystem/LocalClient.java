package chatsystem;

import java.util.Scanner;

import java.io.IOException;

import chatsystem.util.Logs;
import chatsystem.util.ConfigParser;

public class LocalClient extends Client {
    
	public LocalClient(int port) {
		super(port);
	}

	public void run() {
		Scanner cli = new Scanner(System.in);
		String cmd;

		this.startNetworkManager();

		while (true) {
			System.out.print(">>> ");
			cmd = cli.nextLine();
			this.command(cmd);
		}
	}
	
	public static void main(String[] args) {

		LocalClient lc;
		String serverPort, logFilePath;	
		boolean isLogEnabled = true;

		try {
			if (args.length == 0) {
				ConfigParser.parse(Client.DEFAULT_CONFIG_FILE_PATH);
			} else {
				ConfigParser.parse(args[0]);
			}
		} catch (IOException ioe) {}

		logFilePath 	= ConfigParser.get("logfile");
		serverPort 	= ConfigParser.get("server-port");

		if (logFilePath != null) {
			try {
				Logs.init(isLogEnabled, logFilePath);
			} catch (IOException ioe) {
				/* Log init failed, logs filling is just abandonned */
				System.out.println("[x] Error while initializing log file, logs filling is abandonned");
			}
		}

		if (serverPort == null)
			lc = new LocalClient(0);
		else
			lc = new LocalClient(Integer.parseInt(serverPort));

		lc.run();
	}
}
