package chatsystem.network;

import java.lang.Thread;

import java.net.InetAddress;

public class RemoteServerListener extends Thread {

	private InetAddress remoteServerAddress;

	public RemoteServerListener(InetAddress remoteServerAddress) {
		this.remoteServerAddress = remoteServerAddress;
	}

	public void run() {
	}
}

