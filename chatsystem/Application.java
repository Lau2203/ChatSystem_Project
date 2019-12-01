package chatsystem;

import java.util.Scanner;

import java.io.IOException;

import chatsystem.LocalClient;
import chatsystem.util.ConfigParser;
import chatsystem.util.Logs;

public class Application {

	private LocalClient lc;

	public Application() {
		
	}

	private void parseConfigFile(String[] args) {
		try {
			if (args.length == 0) {
				ConfigParser.parse(Client.DEFAULT_CONFIG_FILE_PATH);
			} else {
				ConfigParser.parse(args[0]);
			}
		} catch (IOException ioe) {}
	}

	private void prepareLogs(String logFilePath) {
		boolean isLogEnabled = true;
		if (logFilePath != null) {
			try {
				Logs.init(isLogEnabled, logFilePath);
			} catch (IOException ioe) {
				/* Log init failed, logs filling is just abandonned */
				System.out.println("[x] Error while initializing log file, logs filling is abandonned");
			}
		}
	}

	private void init(String[] args) {
		String serverPort, logFilePath;	

		this.parseConfigFile(args);

		logFilePath 	= ConfigParser.get("logfile");
		serverPort 	= ConfigParser.get("server-port");

		this.prepareLogs(logFilePath);

		if (serverPort == null)
			this.lc = new LocalClient(0);
		else
			this.lc = new LocalClient(Integer.parseInt(serverPort));
	}

	public void run(String[] args) {
		Scanner userInput = new Scanner(System.in);
		String cmd;

		this.init(args);	
		this.lc.start();
		/*
		while (true) {
			System.out.print(">>> ");
			cmd = userInput.nextLine();	
			this.lc.command(cmd);
		}	
		*/
	}

	public static void main(String[] args) {
		Application app = new Application();
		app.run(args);
	}
}

