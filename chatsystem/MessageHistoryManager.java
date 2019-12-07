
package chatsystem;

import java.util.ArrayList;

public class MessageHistoryManager {

	private String messageHistoryPath;

	private ArrayList<MessageHistory> messageHistoryList;

	public MessageHistoryManager(String messageHistoryPath) {
		this.messageHistoryPath = messageHistoryPath;
		this.messageHistoryList = new ArrayList<MessageHistory>();
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

	}

	public synchronized void updateMessageHistory() {

	}
}
