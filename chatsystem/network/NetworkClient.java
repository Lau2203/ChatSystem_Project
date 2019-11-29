package chatsystem.network;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

import java.util.Scanner;

import java.net.Socket;
import java.net.UnknownHostException;

import chatsystem.util.ConfigParser;

public class NetworkClient {

	private Socket socket;

	private String hostname;
	private int port; 
	private BufferedReader in;
	private PrintWriter out;

	private static final String DEFAULT_CONFIG_FILE_PATH = "config.cfg";

	public NetworkClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	} 
	private void connect() throws UnknownHostException, IOException {
		try {
			this.socket = new Socket(hostname, port);

		} catch (IllegalArgumentException iae) {
			System.out.println("Invalid port number");
			System.exit(1);
		}
	}

	private void prepareIO() {
		try {
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.out = new PrintWriter(this.socket.getOutputStream());

		} catch (IOException ioe) {
			System.out.println("IO issue, aborted");
			System.exit(1);
		}
	}

	private void disconnect() {
		try {
			this.socket.close();
		} catch (IOException ioe) {
			System.out.println("Issue in disconnecting, aborted");
			System.exit(1);
		}
	}

	private String readln() throws IOException {
		return this.in.readLine();
	}

	private void write(String s) {
		this.out.print(s);
		this.out.flush();
	}

	private void writeln(String s) {
		this.out.println(s);
		this.out.flush();
	}

	public void run() {

		Scanner userScanner = new Scanner(System.in);
		String userInput = "";
		String serverInput = "";

		try {
			connect();

		} catch (UnknownHostException uhe) {
			System.out.println("Unknown Host");
			System.exit(1);

		} catch (IOException ioe) {
			System.out.println("Could not create socket");
			System.exit(1);
		}

		this.prepareIO();

		while (userInput != null && serverInput != null) {

			System.out.print(">>> ");
			userInput = userScanner.nextLine();

			if (userInput.equals("quit")) {
				break;
			}

			this.writeln(userInput);

			try {
				serverInput = this.readln();
			} catch (IOException ioe) {
				System.out.println("Server shot down connection");
				break;
			}

			if (serverInput == null) {
				System.out.println("Server shot down connection");
				break;
			}

			System.out.println("Received : " + serverInput);
		}

		this.disconnect();
	}

	public static void main(String[] args) {

		NetworkClient c;
		String serverPort;
		String serverHostname;

		try {
			if (args.length == 0) {
				ConfigParser.parse(NetworkClient.DEFAULT_CONFIG_FILE_PATH);
			} else {
				ConfigParser.parse(args[0]);
			}
		} catch (IOException ioe) {}

		serverPort = ConfigParser.get("server-port");
		serverHostname = ConfigParser.get("server-hostname");

		if (serverPort == null || serverHostname == null) {
			System.out.println("Need domain name and port number");
			System.exit(1);
		}

		c = new NetworkClient(serverHostname, Integer.parseInt(serverPort));

		c.run();
	}
}
