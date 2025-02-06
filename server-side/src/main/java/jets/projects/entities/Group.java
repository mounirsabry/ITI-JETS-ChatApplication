package jets.projects.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Group implements Serializable {
    private int groupID;
    private String groupName;
    private String groupDesc;    
    private int groupAdminID;
    private byte[] pic;
    private LocalDateTime createdAt;
    
    public Group() {
        groupID = -1;
        groupName = null;
        groupDesc = "";        
        groupAdminID = -1;
        pic = null;
        createdAt = null;
    }

    public Group(int groupID, String groupName, String groupDesc,
            int groupAdminID, byte[] pic, LocalDateTime createdAt) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupDesc = groupDesc;
        this.groupAdminID = groupAdminID;
        this.pic = pic;
        this.createdAt = createdAt;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public int getGroupAdminID() {
        return groupAdminID;
    }

    public byte[] getPic() {
        return pic;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public void setGroupAdminID(int groupAdminID) {
        this.groupAdminID = groupAdminID;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
  
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("Group");
        builder.append('{');

        builder.append("groupID=");
        builder.append(groupID);
        
        builder.append(", groupName=");
        builder.append(groupName);
        
        builder.append(", groupDesc=");
        builder.append(groupDesc);
        
        builder.append(", groupAdminID=");
        builder.append(groupAdminID);
       
        builder.append(", pic=");
        builder.append(pic == null ? "null" : "Cannot be displayed here.");
        
        builder.append(", createdAt=");
        builder.append(createdAt);

        builder.append('}');
        return builder.toString();
    }
}
