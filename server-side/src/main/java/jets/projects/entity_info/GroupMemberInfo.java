
package jets.projects.entity_info;

import java.io.Serializable;
import jets.projects.entities.GroupMember;

public class GroupMemberInfo implements Serializable {
    private GroupMember member;
    private String name;
    private byte[] pic;

    public GroupMemberInfo() {
        member = null;
        name = "Not Specified";
        pic = null;
    }

    public GroupMemberInfo(GroupMember member, String name, byte[] pic) {
        this.member = member;
        this.name = name;
        this.pic = pic;
    }

    public GroupMember getMember() {
        return member;
    }

    public String getName() {
        return name;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setMember(GroupMember member) {
        this.member = member;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("GroupMemberInfo");
        builder.append('{');

        builder.append(member);
        
        builder.append(", name=");
        builder.append(name);
        
        builder.append(", pic=");
        builder.append(pic == null ? "null" : "Cannot be displayed here.");

        builder.append('}');
        return builder.toString();
    }
}
