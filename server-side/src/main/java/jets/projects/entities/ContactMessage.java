package jets.projects.entities;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

public class ContactMessage implements Serializable{
    private int ID;
    private int senderID;
    private int receiverID;
    private Date sentAt;
    private String content;
    private Blob fileURL;
    private boolean isRead;
    private boolean isFile;
    
    public ContactMessage() {
        ID = -1;
        senderID = -1;
        receiverID = -1;
        sentAt = null;
        content = null;
        fileURL = null;
        isRead = false;
        isFile = false;
    }

    public ContactMessage(int ID, int senderID, int receiverID, Date sentAt, String content, Blob fileURL, boolean isRead, boolean isFile) {
        this.ID = ID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.sentAt = sentAt;
        this.content = content;
        this.fileURL = fileURL;
        this.isRead = isRead;
        this.isFile = isFile;
    }
    public ContactMessage(int senderID, int receiverID, String content, Blob fileURL, boolean isRead, boolean isFile) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.fileURL = fileURL;
        this.isRead = isRead;
        this.isFile = isFile;
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

    public Date getSentAt() {
        return sentAt;
    }

    public String getContent() {
        return content;
    }

    public Blob getFileURL() {
        return fileURL;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public boolean getIsFile() {
        return isFile;
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

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFileURL(Blob fileURL) {
        this.fileURL = fileURL;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(ContactMessage.class.getName());
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
        
        builder.append(", fileURL=");
        builder.append(fileURL);
        
        builder.append(", isRead=");
        builder.append(isRead);
        
        builder.append(", isFile=");
        builder.append(isFile);

        builder.append('}');
        return builder.toString();
    }
}
