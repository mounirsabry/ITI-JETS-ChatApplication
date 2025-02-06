package jets.projects.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GroupMessage implements Serializable {
    private int messageID;
    private int senderID;
    private int groupID;
    private LocalDateTime sentAt;
    private String content;
    private Boolean containsFile;
    private byte[] file;

    public GroupMessage() {
        messageID = -1;
        senderID = -1;
        groupID = -1;
        sentAt = null;
        content = null;
        containsFile = false;
        file = null;
    }

    public GroupMessage(int messageID, int senderID, int groupID, 
            LocalDateTime sentAt, String content,
            Boolean containsFile, byte[] file) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.groupID = groupID;
        this.sentAt = sentAt;
        this.content = content;
        this.containsFile = containsFile;
        this.file = file;
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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public String getContent() {
        return content;
    }

    public Boolean getContainsFile() {
        return containsFile;
    }

    public byte[] getFile() {
        return file;
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

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContainsFile(Boolean containsFile) {
        this.containsFile = containsFile;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("GroupMessage");
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
        
        builder.append(", containsFile=");
        builder.append(containsFile);

        builder.append('}');
        return builder.toString();
    }
}
