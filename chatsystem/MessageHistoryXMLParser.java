package chatsystem;

import java.util.ArrayList;

import java.sql.Timestamp;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import chatsystem.Client;
import chatsystem.User;
import chatsystem.Message;
import chatsystem.MessageString;
import chatsystem.MessageHistory;

public class MessageHistoryXMLParser extends DefaultHandler {

	private Client master;

	private ArrayList<MessageHistory> messageHistoryList;

	private MessageHistory currentMessageHistory;
	private MessageString currentMessage;
	private User currentRecipient;

	private boolean isInMessage = false;

	public MessageHistoryXMLParser(Client master) {
		this.master = master;
		this.messageHistoryList = new ArrayList<MessageHistory>();
	}

	public ArrayList<MessageHistory> getMessageHistoryList() {
		return this.messageHistoryList;
	}

	/* Overwritten xml parser methods */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("recipient")) {
			String fingerprint = attributes.getValue("fingerprint");
			String username = attributes.getValue("username");

			/* Fetch the user in the maintained user list of the main client process */
			this.currentRecipient = this.master.getUser(fingerprint);
	
			if (!this.currentRecipient.isActive()) {
				this.currentRecipient.setUsername(username);
			}

			this.currentMessageHistory = new MessageHistory(this.currentRecipient);

		} else if (qName.equalsIgnoreCase("message")) {
			Timestamp ts = Timestamp.valueOf(attributes.getValue("timestamp"));
			String hasBeenSentByRecipient = attributes.getValue("from");

			if (hasBeenSentByRecipient.equals("me")) {
				this.currentMessage = new MessageString(this.currentMessageHistory.getRecipientUser(), ts, false);
			} else {
				this.currentMessage = new MessageString(this.currentMessageHistory.getRecipientUser(), ts, true);
			}

			this.isInMessage = true;

		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("recipient")) {
			this.messageHistoryList.add(this.currentMessageHistory);
			this.currentRecipient.setMessageHistory(this.currentMessageHistory);
			this.currentMessageHistory.setLastMessage(29);

		} else if (qName.equalsIgnoreCase("message")) {
			this.currentMessageHistory.addMessage(this.currentMessage);
			this.isInMessage = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (this.isInMessage) {
			this.currentMessage.setContent(new String(ch, start, length));
		}
	}
}

