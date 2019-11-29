package chatsystem.network;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.InetAddress;

import java.lang.Thread;

import chatsystem.util.Logs;

public class NetworkSubServer extends Thread {

	protected static Logs logs;

	Socket cs;

	private BufferedReader in;
	private PrintWriter out;

	private InetAddress remoteAddress;
	private int remotePort;

	public NetworkSubServer(Socket clientSocket) {
		this.cs = clientSocket;	

		this.remoteAddress = this.cs.getInetAddress();
		this.remotePort = this.cs.getPort();
	}

	private void prepareIO() {
		try {
			this.in = new BufferedReader(new InputStreamReader(this.cs.getInputStream()));
			this.out = new PrintWriter(this.cs.getOutputStream());

		} catch (IOException ioe) {
			System.out.println("IO issue, aborted");
			this.interrupt();
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

	private void disconnect() {
		try {
			this.cs.close();
			this.logs.printinfo("Disconnected from [" + this.remoteAddress + ":" + this.remotePort + "]");

		} catch (IOException ioe) {
			this.logs.printerro(this + " : Issue with closing client socket, aborted");
		}
	}

	private void die() {
		NetworkServer.notifyDeathOfNetworkSubServer(this);
	}

	protected void shutdown() {
		this.disconnect();
		this.interrupt();
		this.die();
	}

	public void run() {

		String userInput;

		this.prepareIO();

		this.logs.printinfo("Connected with [" + this.remoteAddress + ":" + this.remotePort + "]");

		while (true) {
			try {
				userInput = this.readln();
			} catch (IOException ioe) {
				this.logs.printinfo(this + " : User shot down connection");
				break;
			}

			if (userInput.equals("quit")) {

				break;
			}

			this.logs.println(this + " : " + userInput);

			this.writeln("'userInput' : " + userInput.length());
		}

		if (!this.interrupted()) { this.shutdown(); }
	}

	public String toString() {
		return "[" + this.remoteAddress + ":" + this.remotePort + "]";
	}
}
