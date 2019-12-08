package chatsystem;

import java.sql.Timestamp;

public class MessageString extends Message {

	private String content;

	public MessageString (User recipientUser, Timestamp timestamp, String content) {
		super(recipientUser, timestamp);
		this.content = content;
	}

	public MessageString (User recipientUser, Timestamp timestamp) {
		super(recipientUser, timestamp);
	}

	public MessageString (User recipientUser, Timestamp timestamp, boolean hasBeenSentByRecipient) {
		super(recipientUser, timestamp, hasBeenSentByRecipient);
	}

	@Override
	public String getContent() {
		return this.content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return this.content;
	}
}
