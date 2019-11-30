package chatsystem;

import java.util.Iterator;
import java.util.ArrayList;

public class MessageHistory implements Iterable<Message> {

	private User recipientUser;
	private ArrayList<Message> messageList;

	public MessageHistory() {
		this.recipientUser = new User();
		this.messageList = new ArrayList<Message>();
	}

	public User getRecipientUser() {
		return this.recipientUser;
	}

	public ArrayList<Message> getMessageList() {
		return this.messageList;
	}

	@Override
	public Iterator<Message> iterator() {
		return this.messageList.iterator();
	}


	public void addMessage(Message msg){
		this.messageList.add(msg);
	}
}
