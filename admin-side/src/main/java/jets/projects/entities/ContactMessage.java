package jets.projects.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ContactMessage implements Serializable {
    private int ID;
    private int senderID;
    private int receiverID;
    private LocalDateTime sentAt;
    private String content;
    private boolean isRead;
    private boolean containsFile;
    private byte[] file;
    
    public ContactMessage() {
        ID = -1;
        senderID = -1;
        receiverID = -1;
        sentAt = null;
        content = null;
        isRead = false;
        containsFile = false;
        file = null;
    }

    public ContactMessage(int ID, int senderID, int receiverID,
            LocalDateTime sentAt, String content,
            boolean isRead, boolean containsFile, byte[] file) {
        this.ID = ID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.sentAt = sentAt;
        this.content = content;
        this.isRead = isRead;
        this.containsFile = containsFile;        
        this.file = file;
    }

    public int getID() {
        return ID;
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public boolean getContainsFile() {
        return containsFile;
    }

    public byte[] getFile() {
        return file;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setContainsFile(boolean containsFile) {
        this.containsFile = containsFile;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("ContactMessage");
        builder.append('{');
    
        builder.append("ID=");
        builder.append(ID);
        
        builder.append(", senderID=");
        builder.append(senderID);
        
        builder.append(", receiverID=");
        builder.append(receiverID);
        
        builder.append(", sentAt=");
        builder.append(sentAt);
        
        builder.append(", content=");
        builder.append(content);
        
        builder.append(", isRead=");
        builder.append(isRead);
        
        builder.append(", containsFile=");
        builder.append(containsFile);
        
        builder.append('}');
        return builder.toString();
    }
}
