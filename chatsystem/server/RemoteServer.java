import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import java.net.InetAddress;
import java.net.Socket;

class User {

	private String fingerprint;
	private String username;
	private InetAddress address;

	private boolean isConnected = true;

	public User(String fingerprint, String username, InetAddress address) {
		this.fingerprint 	= fingerprint;
		this.username 		= username;
		this.address 		= address;
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

	public boolean isConnected() {
		return this.isConnected;
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

	public void setConnected() {
		this.isConnected = true;
	}

	public void setDisconnected() {
		this.isConnected = false;
	}
}

@SuppressWarnings("serial")
public class RemoteServer extends HttpServlet {

	private ArrayList<User> users = new ArrayList<User>();

	public RemoteServer() {
	}

	public void init() {
		this.users = new ArrayList<User>();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		PrintWriter pw;
		
		res.setContentType("text/plain");
		pw = res.getWriter();

		for (User u: users) {
			if (u.isConnected()) {
				pw.println(u.getFingerprint() + ":" + u.getUsername() + ":" + u.getAddress());
			} else {
				pw.println(u.getFingerprint() + ":" + u.getUsername() + ":" + u.getAddress() + ":disconnected");
			}
		}

		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String cmd		= req.getParameter("cmd");
		String fingerprint 	= req.getParameter("fingerprint");
		String username 	= req.getParameter("username");
		InetAddress address	= InetAddress.getByName(req.getRemoteAddr());

		PrintWriter pw;

		if (cmd.equals("new")) {
			this.update(true, fingerprint, username, address);
		} else {
			this.update(false, fingerprint, username, address);
		}

		res.setContentType("text/plain");
		pw = res.getWriter();

		pw.println("updated");

		pw.close();
	}

	private void update(boolean newConnection, String fingerprint, String username, InetAddress address) {

		for (User u: this.users) {

			if (u.getFingerprint().equals(fingerprint)) {
				if (newConnection) {
					u.setUsername(username);
					u.setAddress(address);
					u.setConnected();
					return;
				} 
				/* Disconnection, we need to remove that user from the list */
				else {
					u.setDisconnected();
					return;
				}
			}
		}

		if (newConnection) {
			this.users.add(new User(fingerprint, username, address));
		}
	}

	public void destroy() {
	}
}

