package jets.projects.entities;

import java.sql.Blob;
import java.io.Serializable;
import java.util.Date;

public class GroupMessage implements Serializable{
    private int messageID;
    private int senderID;
    private int groupID;
    private Date sentAt;
    private String content;
    private Blob fileURL;
    private Boolean isFile;
    
    public GroupMessage() {
        messageID = -1;
        senderID = -1;
        groupID = -1;
        sentAt = null;
        content = null;
        fileURL = null;
        isFile = false;
    }

    public GroupMessage(int messageID, int senderID, int groupID, Date sentAt, String content, Blob fileURL, Boolean isFile) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.groupID = groupID;
        this.sentAt = sentAt;
        this.content = content;
        this.fileURL = fileURL;
        this.isFile = isFile;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getSenderID() {
        return senderID;
    }

    public int getGroupID() {
        return groupID;
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

    public Boolean getIsFile() {
        return isFile;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
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

    public void setIsFile(Boolean isFile) {
        this.isFile = isFile;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(GroupMessage.class.getName());
        builder.append('{');
        
        builder.append("messageID=");
        builder.append(messageID);
        
        builder.append(", senderID=");
        builder.append(senderID);
        
        builder.append(", groupID=");
        builder.append(groupID);
        
        builder.append(", sentAt=");
        builder.append(sentAt);
        
        builder.append(", content=");
        builder.append(content);
        
        builder.append(", fileURL=");
        builder.append(fileURL);
        
        builder.append(", isFile=");
        builder.append(isFile);

        builder.append('}');
        return builder.toString();
    }
}
