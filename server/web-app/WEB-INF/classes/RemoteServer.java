package chatsystem.server;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import java.net.InetAddress;

class User {

	private String fingerprint;
	private String username;
	private InetAddress address;

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

@SuppressWarnings("serial")
public class RemoteServer extends HttpServlet {

	private ArrayList<User> users = new ArrayList<User>();

	public RemoteServer() {
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		PrintWriter pw;
		
		res.setContentType("text/plain");
		pw = res.getWriter();

		for (User u: users) {
			pw.println(u.getFingerprint() + ":" + u.getUsername() + ":" + u.getAddress());
		}

		pw.close();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String fingerprint 	= req.getParameter("fingerprint");
		String username 	= req.getParameter("username");
		InetAddress address	= InetAddress.getByName(req.getRemoteAddr());

		PrintWriter pw;

		this.update(fingerprint, username, address);

		res.setContentType("text/plain");
		pw = res.getWriter();

		pw.println("updated");

		pw.close();
	}

	private void update(String fingerprint, String username, InetAddress address) {

		for (User u: users) {

			if (u.getFingerprint().equals(fingerprint)) {
				u.setUsername(username);
				u.setAddress(address);
				return;
			}
		}

		users.add(new User(fingerprint, username, address));
	}
}

