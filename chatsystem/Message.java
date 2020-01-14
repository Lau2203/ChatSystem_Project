package chatsystem;

import java.io.File;
import java.sql.Timestamp;

public abstract class Message {

	private User recipientUser;
	private Timestamp timestamp;

	private boolean hasBeenSentByRecipient;

	protected Message(User recipient, Timestamp timestamp) {
		this.recipientUser = recipient;
		this.timestamp = timestamp;
		this.hasBeenSentByRecipient = true;
	}

	protected Message(User recipient, Timestamp timestamp, boolean hasBeenSentByRecipient) {
		this.recipientUser = recipient;
		this.timestamp = timestamp;
		this.hasBeenSentByRecipient = hasBeenSentByRecipient;
	}

	public synchronized User getRecipientUser() {
		return this.recipientUser;
	}

	public synchronized Timestamp getTimestamp() {
		return this.timestamp;
	}

	public synchronized boolean getHasBeenSentByRecipient() {
		return this.hasBeenSentByRecipient;
	}

	public synchronized void setHasBeenSentByRecipient(boolean b) {
		this.hasBeenSentByRecipient = b;
	}

	public File getFile() {
		return null;
	}

	public void setContent(File f) {
	}

	public abstract String getContent();
	public abstract void setContent(String content);

	@Override
	public abstract String toString();
}
