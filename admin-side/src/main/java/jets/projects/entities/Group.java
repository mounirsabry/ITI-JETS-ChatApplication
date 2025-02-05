package jets.projects.entities;

import java.util.Date;

public class Group {
    private int groupID;
    private int groupAdminID;
    private String name;
    private Date createdAt;
    private String pic;
    
    public Group() {
        groupID = -1;
        groupAdminID = -1;
        name = null;
        createdAt = null;
        pic = null;
    }

    public Group(int groupID, int groupAdminID, String name, Date createdAt, String pic) {
        this.groupID = groupID;
        this.groupAdminID = groupAdminID;
        this.name = name;
        this.createdAt = createdAt;
        this.pic = pic;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getGroupAdminID() {
        return groupAdminID;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getPic() {
        return pic;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setGroupAdminID(int groupAdminID) {
        this.groupAdminID = groupAdminID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(Group.class.getName());
        builder.append('{');

        builder.append("groupID=");
        builder.append(groupID);
        
        builder.append(", groupAdminID=");
        builder.append(groupAdminID);
        
        builder.append(", name=");
        builder.append(name);
        
        builder.append(", createdAt=");
        builder.append(createdAt);
        
        builder.append(", pic=");
        builder.append(pic == null ? "null" : "cannot be displayed here.");

        builder.append('}');
        return builder.toString();
    }
}
