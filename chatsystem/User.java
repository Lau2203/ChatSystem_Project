package chatsystem;

import java.net.InetAddress;

import chatsystem.MessageHistory;

public class User {

	private String fingerprint;
	private String username;
	private InetAddress address;

	private MessageHistory messageHistory;

	private boolean isActiveBoolean;

	public User() {
		this.fingerprint = new String();
		this.username = new String();
		this.address = null;
		this.isActiveBoolean = false;

		this.messageHistory = null;
	}

	public User(String fingerprint) {
		this.fingerprint = fingerprint;
		this.username = null;
		this.address = null;
		this.isActiveBoolean = false;

		this.messageHistory = null;
	}

	public User(String fingerprint, String username) {
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = null;
		this.isActiveBoolean = false;

		this.messageHistory = null;
	}

	public User(String fingerprint, String username, InetAddress address) {
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = address;
		this.isActiveBoolean = false;

		this.messageHistory = null;
	}

	public String getFingerprint() {
		return this.fingerprint;
	}

	public String getUsername() {
		return this.username;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public boolean isActive() {
		return this.isActiveBoolean;
	}

	public MessageHistory getMessageHistory() {
		return this.messageHistory;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public void setActive(boolean active) {
		this.isActiveBoolean = active;
	}

	public void setMessageHistory(MessageHistory mh) {
		this.messageHistory = mh;
	}
}
