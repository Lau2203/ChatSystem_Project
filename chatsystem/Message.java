package chatsystem;

import java.sql.Timestamp;

public abstract class Message {

	private User recipientUser;
	private Timestamp timestamp;

	protected Message(User recipient, Timestamp timestamp) {
		this.recipientUser = recipient;
		this.timestamp = timestamp;
	}

	public User getRecipientUser() {
		return this.recipientUser;
	}

	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public abstract String getContent();
	public abstract void setContent(String content);

	@Override
	public abstract String toString();
}
