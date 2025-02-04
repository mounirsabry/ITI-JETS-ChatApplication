package jets.projects.entities;

import java.io.Serializable;
import java.util.Date;

public class ContactInvitation implements Serializable{
    private int invitation_ID;
    private int senderID;
    private int receiverID;
    private Date sentAt;
    
    public ContactInvitation() {
        senderID = -1;
        receiverID = -1;
        sentAt = null;
    }

    public ContactInvitation(int senderID, int receiverID, Date sentAt) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.sentAt = sentAt;
    }
    public int getSenderID() {
        return senderID;
    }
    public int getInvitation_ID() {
        return this.invitation_ID;
    }

    public void setInvitation_ID(int invitation_ID) {
        this.invitation_ID = invitation_ID;
    }
    public int getReceiverID() {
        return receiverID;
    }

    public Date getSentAt() {
        return sentAt;
    }
    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(ContactInvitation.class.getName());
        builder.append('{');

        builder.append("senderID=");
        builder.append(senderID);
        
        builder.append(", receiverID=");
        builder.append(receiverID);
        
        builder.append(", sentAt=");
        builder.append(sentAt);

        builder.append('}');
        return builder.toString();
    }
}
