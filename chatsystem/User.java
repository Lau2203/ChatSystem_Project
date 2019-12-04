package chatsystem;

import java.net.InetAddress;

public class User {

	private String fingerprint;
	private String username;
	private InetAddress address;

	public User() {
		this.fingerprint = new String();
		this.username = new String();
		this.address = null;
	}

	public User(String fingerprint, String username) {
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = null;
	}

	public User(String fingerprint, String username, InetAddress address) {
		this.fingerprint = fingerprint;
		this.username = username;
		this.address = address;
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

	public synchronized void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public synchronized void setUsername(String username) {
		this.username = username;
	}

	public synchronized void setAddress(InetAddress address) {
		this.address = address;
	}
}
