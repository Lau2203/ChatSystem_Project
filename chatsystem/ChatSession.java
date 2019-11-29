package chatsystem;

import java.util.ArrayList;

public class ChatSession {
    
    private User recipientUser;
    private MessageHistory messageHistory;
    
    public ChatSession(User recipientUser, MessageHistory msgHistory) {
        this.recipientUser = recipientUser;
        this.messageHistory = msgHistory;
    }
    
    public User getRecipientUser() {
        return this.recipientUser;
    }
    
    public ArrayList<Message> getMessages() {
        return this.messageHistory.getMessageList();
    }
}
