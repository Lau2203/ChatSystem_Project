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

	public String getFingerprint() {
		return this.fingerprint;
	}

	public String getUsername() {
		return this.username;
	}
	
	public InetAddress getAddress() {
		return this.address;
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
}
