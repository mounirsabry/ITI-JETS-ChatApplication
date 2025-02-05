package jets.projects.entities;

import java.util.Date;

public class Announcement {
    private int announcementID;
    private String header;
    private String content;
    private Date sentAt;

    public Announcement() {
        announcementID = -1;
        header = "Not Specified";
        content = "Not Specified";
        sentAt = null;
    }

    public Announcement(int announcementID, String header,
            String content, Date sentAt) {
        this.announcementID = announcementID;
        this.header = header;
        this.content = content;
        this.sentAt = sentAt;
    }

    public int getAnnouncementID() {
        return announcementID;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }
    
    public Date getSentAt() {
        return sentAt;
    }

    public void setAnnouncementID(int announcementID) {
        this.announcementID = announcementID;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(Announcement.class.getName());
        builder.append('{');
        
        builder.append("announcementID=");
        builder.append(announcementID);
        
        builder.append(", header=");
        builder.append(header);
        
        builder.append(", content=");
        builder.append(content);
        
        builder.append(", sentAt=");
        builder.append(sentAt);
        
        builder.append('}');
        return builder.toString();
    }
}
