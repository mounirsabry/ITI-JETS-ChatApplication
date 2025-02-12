package jets.projects.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ContactInvitation implements Serializable {
    private int invitationID;
    private int senderID;
    private int receiverID;
    private LocalDateTime sentAt;
    
    public ContactInvitation() {
        invitationID = -1;
        senderID = -1;
        receiverID = -1;
        sentAt = null;
    }

    public ContactInvitation(int invitationID, int senderID, int receiverID, LocalDateTime sentAt) {
        this.invitationID = invitationID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.sentAt = sentAt;
    }

    public int getInvitationID() {
        return invitationID;
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setInvitationID(int invitationID) {
        this.invitationID = invitationID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("ContactInvitation");
        builder.append('{');
        
        builder.append("invitationID=");
        builder.append(invitationID);

        builder.append(", senderID=");
        builder.append(senderID);
        
        builder.append(", receiverID=");
        builder.append(receiverID);
        
        builder.append(", sentAt=");
        builder.append(sentAt);

        builder.append('}');
        return builder.toString();
    }
}