package chatsystem.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.InetAddress;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLDecoder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import chatsystem.User;

public class RemoteServerListener {

	private NetworkManager master; 

	private User mainUser;

	private final String USER_AGENT = "Mozilla/5.0";

	private String serverURL = "https://srv-gei-tomcat.insa-toulouse.fr/manager/";

	private boolean isServerAvailable;

	private ArrayList<String> fingerprints;

	public RemoteServerListener(NetworkManager master, User mainUser, String remoteServerURL) {
		this.master = master;
		this.mainUser = mainUser;
		this.serverURL = remoteServerURL;

		this.isServerAvailable = true;

		this.fingerprints = new ArrayList<String>();
	}

	public void notifyRemoteServer() {

		int responseCode;

		if (!isServerAvailable) {
			return;
		}

		try {

			URL u = new URL(this.serverURL);

			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("User-Agent", USER_AGENT);

			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(("cmd=new&fingerprint=" + URLEncoder.encode(this.mainUser.getFingerprint(), "UTF-8")  + "&username=" + this.mainUser.getUsername()).getBytes());
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

		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE); this.isServerAvailable = false;}
	}

	public void notifyDisconnection() {

		int responseCode;

		if (!isServerAvailable) {
			return;
		}

		try {

			URL u = new URL(this.serverURL);

			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			con.setRequestMethod("POST");

			con.setRequestProperty("User-Agent", USER_AGENT);

			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(("cmd=disconnection&fingerprint=" + URLEncoder.encode(this.mainUser.getFingerprint(), "UTF-8")  + "&username=" + this.mainUser.getUsername()).getBytes());
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

		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE); this.isServerAvailable = false;}
	}

	private String get(String f) {
		for (String fp : this.fingerprints) {
			if (fp.equals(f)) {
				return fp;
			}
		}
		return null;
	}

	public void fetchFromRemoteServer() {

		int responseCode;

		if (!isServerAvailable) {
			return;
		}

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

					String fingerprint = URLDecoder.decode(user[0], "UTF-8");

					System.out.println("but the fingerprint is : " + fingerprint);

					if (user.length == 3) {

						String f = this.get(fingerprint);
						if (f == null) {
							this.master.notifyNewActiveUser(fingerprint, InetAddress.getByName(user[2].substring(1, user[2].length())), user[1]);
							this.fingerprints.add(fingerprint);
						}
					}
					/* That means that the user is now disconnected */
					else {
						if (user[3].equals("disconnected")) {

							String f = this.get(fingerprint);
							if (f != null) {
								this.master.notifyEndOfActiveUser(user[0]);
								this.fingerprints.remove(f);
							}

						} else {
							String f = this.get(fingerprint);
							if (f == null) {
								this.master.notifyNewActiveUser(user[0], InetAddress.getByName(user[2].substring(1, user[2].length())), user[1]);
							}
						}
					}
				}

				in.close();
			}
		} catch (Exception e) { JOptionPane.showMessageDialog(null, "Error: Could not connect to remote server", "Error from remote server", JOptionPane.ERROR_MESSAGE); this.isServerAvailable = false; }
	}

	public void start() {
		this.notifyRemoteServer();

		if (this.isServerAvailable) {
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
}

