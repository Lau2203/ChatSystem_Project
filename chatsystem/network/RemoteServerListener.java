package chatsystem.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.InetAddress;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import chatsystem.User;

public class RemoteServerListener {

	private NetworkManager master; 

	private User mainUser;

	private final String USER_AGENT = "Mozilla/5.0";

	private String serverURL = "https://srv-gei-tomcat.insa-toulouse.fr/manager/";

	public RemoteServerListener(NetworkManager master, User mainUser, String remoteServerURL) {
		this.master = master;
		this.mainUser = mainUser;
		this.serverURL = remoteServerURL;
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
			os.write(("cmd=new&fingerprint=" + this.mainUser.getFingerprint()  + "&username=" + this.mainUser.getUsername()).getBytes());
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

	public void notifyDisconnection() {

		int responseCode;

		try {

			URL u = new URL(this.serverURL);

			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("User-Agent", USER_AGENT);

			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(("cmd=disconnection&fingerprint=" + this.mainUser.getFingerprint()  + "&username=" + this.mainUser.getUsername()).getBytes());
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
					System.out.println("AFTER SUBSTRING ; " + user[2].substring(1, user[2].length()));
					if (user.length == 3) {
						this.master.notifyNewActiveUser(user[0], InetAddress.getByName(user[2].substring(1, user[2].length())), user[1]);
					}
					/* That means that the user is now disconnected */
					else {
						this.master.notifyEndOfActiveUser(user[0]);
					}
				}

				in.close();
			}
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE); }
	}

	public void start() {
		this.notifyRemoteServer();

		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				System.out.println("FETCHING SERVER INFORMATION...");
				fetchFromRemoteServer();
			}
		};

		t.scheduleAtFixedRate(tt, 0, 3000);
	}
}

