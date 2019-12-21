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
    
    @Override
    public String getContent(){
        return this.filePath;
    }
    
    @Override
    public void setContent(String filePath) {
        this.filePath = filePath;
        this.content = new File(this.filePath);
    }
    
    @Override
    public String toString() {
        return this.filePath;
    }

}
