package chatsystem;

import java.util.Iterator;
import java.util.ArrayList;

import chatsystem.User;
import chatsystem.Message;

public class MessageHistory implements Iterable<Message> {

	private User recipientUser;
	private ArrayList<Message> messageList;

	private String lastMessage;

	public MessageHistory() {
		this.recipientUser = new User();
		this.messageList = new ArrayList<Message>();

		this.lastMessage = "";
	}

	public MessageHistory(User user) {
		this.recipientUser = user;
		this.messageList = new ArrayList<Message>();

		this.lastMessage = "";
	}

	public synchronized User getRecipientUser() {
		return this.recipientUser;
	}

	public synchronized ArrayList<Message> getMessageList() {
		return this.messageList;
	}

	@Override
	public synchronized Iterator<Message> iterator() {
		return this.messageList.iterator();
	}

	public synchronized void addMessage(Message msg){
		this.messageList.add(msg);
	}

	public synchronized String getLastMessage() {
		return this.lastMessage;
	}

	public synchronized void setLastMessage(int length) {
		String lastMessage = this.messageList.get(this.messageList.size() - 1).getContent();
		try {
			this.lastMessage = lastMessage.substring(0, length);
		} catch (IndexOutOfBoundsException ioobe) {
			/* If the string is too small we take the whole string */
			this.lastMessage = lastMessage;
		}
	}
}
