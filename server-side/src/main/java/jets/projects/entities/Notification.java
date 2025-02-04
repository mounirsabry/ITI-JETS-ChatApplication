package jets.projects.entities;

import java.sql.Timestamp;

public class Notification {
    private int notificationID; 
    private int userID;
    private String content;
    private boolean isRead;
    private Timestamp sentAt;
    private NotificationType Type;
    
   
    public Notification() {
        notificationID = -1;
        userID = -1;
        content = null;
        isRead = false;
        sentAt = null;
        Type=NotificationType.NONE;
    }

    public Notification(int notificationID, int userID,NotificationType type, String content, boolean isRead, Timestamp sentAt) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.Type= type;
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

    public NotificationType getNotificationType() {
        return Type;
    }


    public String getContent() {
        return content;
    }

    public boolean getIsRead() {
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

    public void setNotificationType(NotificationType type) {
        this.Type=type;
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
        
        builder.append(Notification.class.getName());
        builder.append('{');
        
        builder.append("notificationID=");
        builder.append(notificationID);
        
        builder.append(", userID=");
        builder.append(userID);

        builder.append(", notificationtype=");
        builder.append(Type);

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
