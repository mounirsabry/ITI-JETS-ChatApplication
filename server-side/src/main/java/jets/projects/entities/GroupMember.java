
package jets.projects.entities;

import java.io.Serializable;

public class GroupMember implements Serializable{
    private int groupID;
    private int memberID;
    private String username;
    private String picture;

    public GroupMember() {
        groupID = -1;
        memberID = -1;
        username="";
        picture="";
    }

    public GroupMember(int groupID, int memberID) {
        this.groupID = groupID;
        this.memberID = memberID;
       
    }

    public GroupMember(int groupID, int memberID,String username,String picture) {
        this.groupID = groupID;
        this.memberID = memberID;
        this.username = username;
        this.picture = picture;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getMemberID() {
        return memberID;
    }
    public String getUsername() {
        return username;
    }
    public String getPic() {
        return picture;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }
    public void setUsername(String username) {
        this.username= username;
    }
    public void setPic(String picture) {
        this.picture = picture;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(GroupMember.class.getName());
        builder.append('{');
        
        builder.append("groupID=");
        builder.append(groupID);
        
        builder.append(", memberID=");
        builder.append(memberID);

        builder.append(", username=");
        builder.append(username);

        builder.append(", picture=");
        builder.append(picture);

        builder.append('}');
        return builder.toString();
    }
}
