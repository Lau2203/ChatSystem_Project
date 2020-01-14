package chatsystem;

import java.io.File;
import java.sql.Timestamp;

public class MessageFile extends Message {

	private String filePath;
	private File content;

	public MessageFile(User recipientUser, Timestamp timestamp, String filePath) {
		super(recipientUser, timestamp);
		this.filePath = filePath;
		this.content = new File(this.filePath);
	}

	public MessageFile(User recipientUser, Timestamp timestamp, File content) {
		super(recipientUser, timestamp);
		this.filePath = content.getName();
		this.content = content;
	}

	public MessageFile(User recipientUser, Timestamp timestamp) {
		super(recipientUser, timestamp);
	}

	public MessageFile(User recipientUser, Timestamp timestamp, boolean hasBeenSentByRecipient) {
		super(recipientUser, timestamp, hasBeenSentByRecipient);
	}

	@Override
	public String getContent(){
		return this.filePath;
	}

	@Override
	public File getFile() {
		return this.content;
	}

	@Override
	public void setContent(String filePath) {
		this.filePath = filePath;
		this.content = new File(this.filePath);
	}

	@Override
	public void setContent(File file) {
		this.filePath = file.getName();
		this.content = file;
	}

	@Override
	public String toString() {
		return "File: " + this.filePath;
	}

}
