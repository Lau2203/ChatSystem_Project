package chatsystem;

import java.util.ArrayList;

import java.sql.Timestamp;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import chatsystem.User;
import chatsystem.Message;
import chatsystem.MessageString;
import chatsystem.MessageHistory;

public class MessageHistoryXMLParser extends DefaultHandler {

	private ArrayList<MessageHistory> messageHistoryList;

	private MessageHistory currentMessageHistory;
	private MessageString currentMessage;

	private boolean isInMessage = false;

	public MessageHistoryXMLParser() {
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

			this.currentMessageHistory = new MessageHistory(new User(fingerprint));
			System.out.println("AFTER");
			System.out.flush();

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

		} else if (qName.equalsIgnoreCase("message")) {
			this.currentMessageHistory.addMessage(this.currentMessage);
			System.out.println(this.currentMessage.getContent());
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

