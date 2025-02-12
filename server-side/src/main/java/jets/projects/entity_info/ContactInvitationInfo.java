package jets.projects.entity_info;

import jets.projects.entities.ContactInvitation;

import java.io.Serializable;

public class ContactInvitationInfo implements Serializable {
    private ContactInvitation invitation;
    
    private String senderDisplayName;
    private byte[] senderPic;

    public ContactInvitationInfo() {
        invitation = null;
        senderDisplayName = "Not Specified";
        senderPic = null;
    }
    
    public ContactInvitationInfo(ContactInvitation invitation,
            String senderDisplayName,
            byte[] senderPic) {
        this.invitation = invitation;
        this.senderDisplayName = senderDisplayName;
        this.senderPic = senderPic;
    }

    public ContactInvitation getInvitation() {
        return invitation;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }
    
    public byte[] getSenderPic() {
        return senderPic;
    }

    public void setInvitation(ContactInvitation invitation) {
        this.invitation = invitation;
    }

    public void setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    public void setSenderPic(byte[] senderPic) {
        this.senderPic = senderPic;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ContactInvitationInfo");
        builder.append('{');
        
        builder.append(invitation);
        
        builder.append(", senderDisplayName=");
        builder.append(senderDisplayName);
        
        builder.append(", senderPic=");
        builder.append(senderPic == null ? "null" : "Cannot be displayed here.");
   
        builder.append('}');
        return builder.toString();
    }
}
