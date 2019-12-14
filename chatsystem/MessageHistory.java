package chatsystem;

import java.util.ArrayList;

import chatsystem.User;
import chatsystem.Message;

public class MessageHistory {

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

	public User getRecipientUser() {
		return this.recipientUser;
	}

	public ArrayList<Message> getMessageList() {
		return this.messageList;
	}

	public void addMessage(Message msg){
		this.messageList.add(msg);
	}

	public String getLastMessage() {
		return this.lastMessage;
	}

	public void setLastMessage(int length) {
		String lastMessage = this.messageList.get(this.messageList.size() - 1).getContent();
		try {
			this.lastMessage = lastMessage.substring(0, length);
		} catch (IndexOutOfBoundsException ioobe) {
			/* If the string is too small we take the whole string */
			this.lastMessage = lastMessage;
		}
	}
}
