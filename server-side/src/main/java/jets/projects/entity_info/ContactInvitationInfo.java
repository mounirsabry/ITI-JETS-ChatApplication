package jets.projects.entity_info;

import jets.projects.entities.ContactInvitation;

public class ContactInvitationInfo {
    private ContactInvitation invitation;
    
    private String senderDisplayName;
    private byte[] senderPic;
    
    private String receiverDisplayName;
    private byte[] receiverPic;

    public ContactInvitationInfo() {
        invitation = null;
        
        senderDisplayName = "Not Specified";
        senderPic = null;
        
        receiverDisplayName = "Not Specified";
        receiverPic = null;
    }
    
    public ContactInvitationInfo(ContactInvitation invitation,
            String senderDisplayName,
            byte[] senderPic,
            String receiverDisplayName,
            byte[] receiverPic) {
        this.invitation = invitation;
        
        this.senderDisplayName = senderDisplayName;
        this.senderPic = senderPic;
        
        this.receiverDisplayName = receiverDisplayName;
        this.receiverPic = receiverPic;
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

    public String getReceiverDisplayName() {
        return receiverDisplayName;
    }
    
    public byte[] getReceiverPic() {
        return receiverPic;
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
    
    public void setReceiverDisplayName(String receiverDisplayName) {
        this.receiverDisplayName = receiverDisplayName;
    }

    public void setReceiverPic(byte[] receiverPic) {
        this.receiverPic = receiverPic;
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
        
        builder.append(", receiverDisplayName=");
        builder.append(receiverDisplayName);
        
        builder.append(", receiverPic=");
        builder.append(receiverPic == null ? "null" : "Cannot be displayed here.");
        
        builder.append('}');
        return builder.toString();
    }
}
