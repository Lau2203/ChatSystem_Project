package chatsystem.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.lang.Thread;

import java.net.InetAddress;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

public class RemoteServerListener extends Thread {

	private NetworkManager master; 

	private final String USER_AGENT = "Mozilla/5.0";

	private String serverURL = "https://srv-gei-tomcat.insa-toulouse.fr/manager/";

	public RemoteServerListener(NetworkManager master, String remoteServerURL) {
		this.master = master;
		this.serverURL = remoteServerURL;
	}

	private void init() {

		this.notifyRemoteServer();
		this.fetchFromRemoteServer();
	}

	private void notifyRemoteServer() {

		int responseCode;

		try {

			URL u = new URL(this.serverURL);

			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("User-Agent", USER_AGENT);

			responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				if (!in.readLine().equals("updated")) {
					JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE);
				}

				in.close();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	private void fetchFromRemoteServer() {

		int responseCode;

		try {
			URL u = new URL(this.serverURL);

			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("GET");

			con.setRequestProperty("User-Agent", USER_AGENT);

			responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				String line;

				while ((line = in.readLine()) != null) {
					String[] user = line.split(":");

					this.master.notifyNewActiveUser(user[0], InetAddress.getByName(user[3]), user[1]);
				}

				in.close();
			}
		} catch (Exception e) { e.printStackTrace(); }
	}

	public void run() {
		this.init();
	}
}

