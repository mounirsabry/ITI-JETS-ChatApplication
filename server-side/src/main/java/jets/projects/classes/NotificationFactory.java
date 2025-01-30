package jets.projects.classes;

import jets.projects.entities.Notification;

public class NotificationFactory {
    @SuppressWarnings("unused")
    private NotificationFactory() {
        throw new UnsupportedOperationException("Do not create object.");
    }
    
    public static Notification createInvitationReceiverNotification(
            String senderName) {
        // ... has send you a contact invitation.
        return null;
    }
    
    public static Notification createInvitationSenderNotification(
            String receiverName) {
        // You have sent ... a contact invitation.
        return null;
    }
    
    public static Notification createAcceptedInvitationReceiverNotification(
            String senderName) {
        // You have accepted ... contact invitation, ... was added to your
        // contacts list.
        return null;
    }
    
    public static Notification createAcceptedInvitationSenderNotification(
            String receiveName) {
        // ... has accepted your contact invitation, ... was added to your
        // contacts list.
        return null;
    }
    
    public static Notification createRejectedInvitationReceiverNotification(
            String senderName) {
        // You have rejected ... contact invitation.
        return null;
    }
    
    public static Notification createRejectedInvitationSenderNotification(
            String receiverName) {
        // ... has rejected your contact invitation.
        return null;
    }
}
