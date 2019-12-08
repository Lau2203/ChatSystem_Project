package chatsystem;

import java.net.InetAddress;

import chatsystem.MessageHistory;

public class User {

	private String fingerprint;
	private String username;
	private InetAddress address;

	private MessageHistory messageHistory;

	private boolean isActive;

	public User() {
		this.fingerprint = new String();
		this.username = new String();
		this.address = null;
		this.isActive = false;

		this.messageHistory = null;
	}

	public User(String fingerprint) {
		this.fingerprint = fingerprint;
		this.username = null;
		this.address = null;
		this.isActive = false;

		this.messageHistory = null;
	}

	public User(String fingerprint, String username) {
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = null;
		this.isActive = false;

		this.messageHistory = null;
	}

	public User(String fingerprint, String username, InetAddress address) {
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = address;
		this.isActive = false;

		this.messageHistory = null;
	}

	public synchronized String getFingerprint() {
		return this.fingerprint;
	}

	public synchronized String getUsername() {
		return this.username;
	}

	public synchronized InetAddress getAddress() {
		return this.address;
	}

	public synchronized boolean isActive() {
		return this.isActive;
	}

	public synchronized MessageHistory getMessageHistory() {
		return this.messageHistory;
	}

	public synchronized void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public synchronized void setUsername(String username) {
		this.username = username;
	}

	public synchronized void setAddress(InetAddress address) {
		this.address = address;
	}

	public synchronized void setActive(boolean active) {
		this.isActive = active;
	}

	public synchronized void setMessageHistory(MessageHistory mh) {
		this.messageHistory = mh;
	}
}
