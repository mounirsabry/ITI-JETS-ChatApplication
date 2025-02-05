package jets.projects.entities;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notification implements Serializable {
    private int notificationID; 
    private int userID;
    private NotificationType type;
    private String content;
    private boolean isRead;
    private Timestamp sentAt;
    
    public Notification() {
        notificationID = -1;
        userID = -1;
        type = NotificationType.NONE;
        content = null;
        isRead = false;
        sentAt = null;
    }

    public Notification(int notificationID, int userID, NotificationType type, String content, boolean isRead, Timestamp sentAt) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public int getUserID() {
        return userID;
    }

    public NotificationType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Notification");
        builder.append('{');

        builder.append("notificationID=");
        builder.append(notificationID);
        
        builder.append(", userID=");
        builder.append(userID);

        builder.append(", type=");
        builder.append(type);

        builder.append(", content=");
        builder.append(content);
        
        builder.append(", isRead=");
        builder.append(isRead);
        
        builder.append(", sentAt=");
        builder.append(sentAt);
        
        builder.append('}');
        return builder.toString();
    }
}
