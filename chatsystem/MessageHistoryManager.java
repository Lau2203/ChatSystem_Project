
package chatsystem;

import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import chatsystem.Client;
import chatsystem.Message;
import chatsystem.util.Logs;

/* extends DefaultHandler for xml parsing */
public class MessageHistoryManager extends DefaultHandler {

	private Client master;

	private String messageHistoryPath;

	private ArrayList<MessageHistory> messageHistoryList;

	private String instanceName = "MessageHistoryManager";

	public MessageHistoryManager(Client master, String messageHistoryPath) {
		this.master = master;
		this.messageHistoryPath = messageHistoryPath;
	}

	private void createMessageHistory() {

	}

	public synchronized MessageHistory getMessageHistory(User recipientUser) {
		for (MessageHistory mh: this.messageHistoryList) {
			if (mh.getRecipientUser().equals(recipientUser)) {
				return mh;
			}
		}

		return null;
	}

	public synchronized void addMessage(User recipientUser, Message msg) {

	}

	public synchronized void fetchMessageHistory() {

		File xmlFile;
		MessageHistoryXMLParser mhxmlp = new MessageHistoryXMLParser(this.master);

		xmlFile = new File(this.messageHistoryPath);
		
		if (xmlFile.exists()) {

			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				saxParser.parse(xmlFile, mhxmlp);
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.messageHistoryList = mhxmlp.getMessageHistoryList();

		} else {
			this.messageHistoryList = new ArrayList<MessageHistory>();
		}
	}

	public synchronized void addMessageHistory(MessageHistory mh) {
		this.messageHistoryList.add(mh);
	}

	public synchronized void saveMessageHistory() {

		PrintWriter out;

		try {
			out = new PrintWriter(new FileWriter(this.messageHistoryPath));	
		} catch (IOException ioe) {
			Logs.printerro(this.instanceName, "Could not write in file");
			return;
		}

		out.println("<?xml version=\"1.0\"?>");
		out.println("<history>");

		for (MessageHistory mh: this.messageHistoryList) {

			out.println("<recipient fingerprint=\"" + mh.getRecipientUser().getFingerprint() + "\" username=\"" + mh.getRecipientUser().getUsername() + "\">");

			for (Message msg: mh.getMessageList()) {
				if (msg.getHasBeenSentByRecipient()) {
					out.print("<message from=\"him\" timestamp=\"" + msg.getTimestamp() + "\">");
				} else {
					out.print("<message from=\"me\" timestamp=\"" + msg.getTimestamp() + "\">");
				}

				out.print(msg.getContent());
				out.println("</message>");	
			}

			out.println("</recipient>");
		}

		out.println("</history>");

		out.flush();
		out.close();
	}
}
