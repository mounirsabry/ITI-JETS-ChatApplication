package jets.projects.entities;

import java.util.Date;

public class Notification {
   private int notificationID; 
   private int userID;
   private String content;
   private boolean isRead;
   private Date sentAt;

    public Notification(int notificationID, int userID, String content, boolean isRead, Date sentAt) {
        this.notificationID = notificationID;
        this.userID = userID;
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

    public String getContent() {
        return content;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }
   
    @Override
    public String toString() {
        return null;
    }
}
