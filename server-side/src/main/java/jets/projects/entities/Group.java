package jets.projects.entities;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

public class Group implements Serializable{
    private int groupID;
    private int groupAdminID;
    private String name;
    private Date createdAt;
    private Blob pic;
    private String description;
    
    public Group() {
        groupID = -1;
        groupAdminID = -1;
        name = null;
        createdAt = null;
        pic = null;
        description = "";
    }

    public Group(int groupID, int groupAdminID, String name, Date createdAt, Blob pic , String description) {
        this.groupID = groupID;
        this.groupAdminID = groupAdminID;
        this.name = name;
        this.createdAt = createdAt;
        this.pic = pic;
        this.description = description;
    }
    public Group(int groupAdminID, String name, Blob pic , String description) {
        this.groupAdminID = groupAdminID;
        this.name = name;
        this.pic = pic;
        this.description = description;
    }
    public Group(int groupAdminID, String name , String description) {
        this.groupAdminID = groupAdminID;
        this.name = name;
        this.description = description;
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

    public Blob getPic() {
        return pic;
    }
    public String getDescription(){
        return description;
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

    public void setPic(Blob pic) {
        this.pic = pic;
    }
    public void setDescription(String description){
        this.description = description;
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
