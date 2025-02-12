package jets.projects.entity_info;

import jets.projects.entities.Announcement;

import java.io.Serializable;

public class AnnouncementInfo implements Serializable {
    private Announcement announcement;
    private int userID;
    private boolean isRead;
    
    public AnnouncementInfo() {
        announcement = null;
        userID = -1;
        isRead = false;
    }

    public AnnouncementInfo(Announcement announcement, int userID, boolean isRead) {
        this.announcement = announcement;
        this.userID = userID;
        this.isRead = isRead;
    }

    public Announcement getAnnouncement() {
        return announcement;
    }

    public int getUserID() {
        return userID;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AnnouncementInfo");
        builder.append('{');
        
        builder.append(announcement);
        
        builder.append(", userID=");
        builder.append(userID);
        
        builder.append(", isRead=");
        builder.append(isRead);
        
        builder.append('}');
        return builder.toString();
    }
}
