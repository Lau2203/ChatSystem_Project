package chatsystem;

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

	public User getRecipientUser() {
		return this.recipientUser;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public boolean getHasBeenSentByRecipient() {
		return this.hasBeenSentByRecipient;
	}

	public void setHasBeenSentByRecipient(boolean b) {
		this.hasBeenSentByRecipient = b;
	}

	public abstract String getContent();
	public abstract void setContent(String content);

	@Override
	public abstract String toString();
}
