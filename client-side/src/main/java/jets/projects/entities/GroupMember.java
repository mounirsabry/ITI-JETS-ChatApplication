package jets.projects.entities;

import java.io.Serializable;

public class GroupMember implements Serializable {
    private int groupID;
    private int memberID;

    public GroupMember() {
        groupID = -1;
        memberID = -1;
    }

    public GroupMember(int groupID, int memberID) {
        this.groupID = groupID;
        this.memberID = memberID;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupMember");
        builder.append('{');
                
        builder.append("groupID=");
        builder.append(groupID);
        
        builder.append(", memberID=");
        builder.append(memberID);
        
        builder.append('}');
        return builder.toString();
    }
}
