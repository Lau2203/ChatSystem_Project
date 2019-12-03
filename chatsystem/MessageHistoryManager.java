
package chatsystem;

import java.util.ArrayList;

public class MessageHistoryManager {

	private static String MESSAGE_HISTORY_PATH;

	private ArrayList<MessageHistory> messageHistoryList;

	public MessageHistoryManager(String messageHistoryPath) {
		MessageHistoryManager.MESSAGE_HISTORY_PATH = messageHistoryPath;
		this.messageHistoryList = new ArrayList<MessageHistory>();
	}

	private void createMessageHistory() {

	}

	public MessageHistory getMessageHistory(User recipientUser) {
		return null;
	}

	public void addMessage(User recipientUser, Message msg) {

	}

	public void fetchMessageHistory() {

	}

	public void updateMessageHistory() {

	}
}
