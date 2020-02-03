package chatsystem.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.lang.Thread;

import java.net.InetAddress;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JOptionPane;

import chatsystem.User;

public class RemoteServerListener extends Thread {

	private NetworkManager master; 

	private User mainUser;

	private final String USER_AGENT = "Mozilla/5.0";

	private String serverURL = "https://srv-gei-tomcat.insa-toulouse.fr/manager/";

	public RemoteServerListener(NetworkManager master, User mainUser, String remoteServerURL) {
		this.master = master;
		this.mainUser = mainUser;
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

			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(("fingerprint=" + this.mainUser.getFingerprint()  + "&username=" + this.mainUser.getUsername()).getBytes());
			os.flush();
			os.close();

			responseCode = con.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

				if (!in.readLine().equals("updated")) {
					JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE);
				}

				in.close();
			} else {
				JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE); }
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
					System.out.println("SERVER ==== " + line);
					String[] user = line.split(":");

					this.master.notifyNewActiveUser(user[0], InetAddress.getByName(user[2].substring(1, user[2].length()-1)), user[1]);
				}

				in.close();
			}
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE); }
	}

	public void run() {
		this.init();
	}
}

