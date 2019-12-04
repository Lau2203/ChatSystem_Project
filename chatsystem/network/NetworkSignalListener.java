package chatsystem.network;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.io.IOException;

import java.lang.Thread;

import chatsystem.MainUser;
import chatsystem.NotifyInformation;
import chatsystem.util.Logs;

import chatsystem.network.NetworkManager;

public class NetworkSignalListener extends Thread {

	private NetworkManager master;

	private MainUser mainUser;

	private DatagramSocket ds;
	private int listeningPort;

	private NotifyInformation info;

	private final String instanceName = "NetworkSignalListener";

	private int activeClientsResponseToWaitFor;

	public NetworkSignalListener(NetworkManager master, MainUser mainUser, int listeningPort) {
		this.master = master;
		this.mainUser = mainUser;
		this.listeningPort = listeningPort;

		this.activeClientsResponseToWaitFor = -1;
	}	

	private void prepare() {
		synchronized(this.master) {
			try {
				this.ds = new DatagramSocket(this.listeningPort);	
			} catch (SocketException se) {
				Logs.printerro(this.instanceName, "Could not create datagram socket, aborted");
				System.exit(1);
				/* return statement to avoid compiler warning : "ds variable might not be initialized" */
				return;
			}

			this.master.wakeUp();
		}
	}

	private void tellOthersWeAreActive() {

		String request = this.mainUser.getFingerprint() + ":" + NetworkManagerInformation.NEW_ACTIVE_CLIENT_STRING;

		DatagramPacket dp = null;

		try {
			dp = new DatagramPacket(request.getBytes(), request.length(), InetAddress.getByName("archlab"), this.listeningPort);
		} catch (UnknownHostException uhe) { uhe.printStackTrace(); }

		try { this.ds.send(dp); } catch (IOException ioe) { ioe.printStackTrace(); }
	}

	/* UDP Packets will at least have this shape : fingerprint:signal */
	private void getInformation(String input, InetAddress remoteAddress, int remotePort) {

		int fingerprintSize = this.mainUser.getFingerprint().length();		

		String[] information = input.split(":");

		String fingerprint = information[0];
		String signal = information[1];

		if (signal.equals(NetworkManagerInformation.NEW_ACTIVE_CLIENT_STRING)) {
			/* Example of built packet : fingerprint:WE:243:USERNAME */

			int currentActiveClientsNumber;

			synchronized(this.master) {
				currentActiveClientsNumber = this.master.getActiveClientsNumber();
			}

			String response = this.mainUser.getFingerprint() + ":" +
						NetworkManagerInformation.WELCOME_STRING + ":" +
						currentActiveClientsNumber + ":" +
						this.mainUser.getUsername();

			DatagramPacket dp = new DatagramPacket(response.getBytes(), response.length(), remoteAddress, remotePort);

			try { this.ds.send(dp); } catch (IOException ioe) {ioe.printStackTrace();}

			synchronized(this.master) {
				this.master.notifyNewActiveClient(fingerprint, remoteAddress);
			}

		} else if (signal.equals(NetworkManagerInformation.END_OF_ACTIVE_CLIENT_STRING)) {

		} else if (signal.equals(NetworkManagerInformation.USERNAME_MODIFICATION_STRING)) {

			String new_username = information[3];

			synchronized(this.master) {
				this.master.notifyNewUsername(fingerprint, remoteAddress, new_username);
			}
		}
		/* No problem when we are the first and only one to connect to the network. Since we also receive the broadcast signal,
		 * this.activeClientsResponseToWaitFor will first be set to 0 and unlock the whole system by notifying
		 * READY_TO_CHECK_USERNAME */
		else if (signal.equals(NetworkManagerInformation.WELCOME_STRING)) {

			int activeClientsTotal = Integer.parseInt(information[2]);
			String username;
			try { username = information[3]; } catch (Exception e) { username = "undefined"; }

			if (this.activeClientsResponseToWaitFor == -1) {
				this.activeClientsResponseToWaitFor = activeClientsTotal;
				if (activeClientsTotal != 0)
					this.activeClientsResponseToWaitFor--;
			} else {
				this.activeClientsResponseToWaitFor--;
			}

			synchronized(this.master) {
				this.master.notifyNewUsername(fingerprint, remoteAddress, username);
			}

			if (this.activeClientsResponseToWaitFor == 0) {
				synchronized(this.master) {
					this.master.notifyReadyToCheckUsername();
				}
			}
		} else {

			System.out.println("UNKNOWN SIGNAL");
		}
	}

	public void run() {

		DatagramPacket dp;
		
		byte[] buffer = new byte[256];
		
		String input;
		String output;

		InetAddress remoteAddress;
		int remotePort;

		this.prepare();

		this.tellOthersWeAreActive();

		dp = new DatagramPacket(buffer, buffer.length);
	
		System.out.println("Signal listener listening on port " + this.listeningPort);

		while (true) {

			try { this.ds.receive(dp); } catch (IOException ioe) {ioe.printStackTrace();}

			remoteAddress 	= dp.getAddress();
			remotePort 	= dp.getPort();

			input = new String(dp.getData(), 0, dp.getLength());	

			System.out.println("RECEIVED UDP : " + input);

			this.getInformation(input, remoteAddress, remotePort);	
		}
	}
}

