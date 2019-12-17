package chatsystem.network;

import java.lang.Thread;

import java.net.InetAddress;

import chatsystem.User;
import chatsystem.NotifyInformation;

import chatsystem.Message;

public class NetworkManagerInformation {

	private User recipient;

	private NotifyInformation info;
	private String fingerprint;
	private String username;
	private InetAddress address;

	private Message msg;

	public static final int FINGERPRINT_SIZE = 16;
	/* Size in bytes (String character) of the below signal in 
	 * a UDP packet */
	public static final int NETWORK_SIGNAL_SIZE = 2;

	/* for New Active user */
	public static final String NEW_ACTIVE_CLIENT_STRING 	= "NA";
	/* for End of active User */
	public static final String END_OF_ACTIVE_CLIENT_STRING 	= "EU";
	/* for End of active User */
	public static final String USERNAME_MODIFICATION_STRING = "NU";

	public static final String WELCOME_STRING 		= "WE";
	public static final String INVALID_USERNAME_STRING 	= "IU";

	public static InetAddress BROADCAST_ADDR; 

	public NetworkManagerInformation() {
		this.info = null;
		this.fingerprint = null;
		this.username = null;
		this.address = null;

		NetworkManagerInformation.setBroadcastAddress();
	}

	public NetworkManagerInformation(User recipient,
					NotifyInformation info,
					String fingerprint,
					String username,
					InetAddress address,
					Message msg) {

		this.recipient = recipient;

		this.info = info;
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = address;

		this.msg = msg;
	}

	private static void setBroadcastAddress() {
		try {
			NetworkManagerInformation.BROADCAST_ADDR = InetAddress.getByName("255.255.255.255");
		} catch (Exception e) { e.printStackTrace(); }
	}

	/* Getters */
	public synchronized User 			getRecipientUser() 	{ return this.recipient; }

	public synchronized NotifyInformation	getNotifyInformation()		{ return this.info; }

	public synchronized String 			getFingerprint() 	{ return this.fingerprint; }

	public synchronized String 			getUsername() 		{ return this.username; }

	public synchronized InetAddress 		getAddress() 		{ return this.address; }

	public synchronized Message 			getMessage() 		{ return this.msg; }

	/* Setters */
	public synchronized void setRecipientUser(User recipient) 		{ this.recipient = recipient; }

	public synchronized void setNotifyInformation(NotifyInformation info)	{ this.info = info; }

	public synchronized void setFingerprint(String fingerprint)		{ this.fingerprint = fingerprint; }

	public synchronized void setUsername(String userName)			{ this.username = userName; }

	public synchronized void setAddress(InetAddress inetAddress)		{ this.address = inetAddress; }

	public synchronized void setMessage(Message msg)			{ this.msg = msg; }
}

