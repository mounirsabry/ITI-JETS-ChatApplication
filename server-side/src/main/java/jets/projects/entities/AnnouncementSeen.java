package jets.projects.entities;

public class AnnouncementSeen {
    private int announcementID;
    private int userID;
    private boolean isRead;
    
    public AnnouncementSeen() {
        announcementID = -1;
        userID = -1;
        isRead = false;
    }

    public AnnouncementSeen(int announcementID, int userID, boolean isRead) {
        this.announcementID = announcementID;
        this.userID = userID;
        this.isRead = isRead;
    }

    public int getAnnouncementID() {
        return announcementID;
    }

    public int getUserID() {
        return userID;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setAnnouncementID(int announcementID) {
        this.announcementID = announcementID;
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
        builder.append("AnnouncementSeen");
        builder.append('{');
        
        builder.append("AnnouncementID=");
        builder.append(announcementID);
        
        builder.append(", userID=");
        builder.append(userID);
        
        builder.append(", isRead=");
        builder.append(isRead);
        
        builder.append('}');
        return builder.toString();
    }
}
