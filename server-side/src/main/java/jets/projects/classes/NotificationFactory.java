package jets.projects.classes;

import jets.projects.entities.NormalUserStatus;
import jets.projects.entities.Notification;
import jets.projects.entities.NotificationType;

public class NotificationFactory {
    @SuppressWarnings("unused")
    private NotificationFactory() {
        throw new UnsupportedOperationException("Do not create object.");
    }
    
    public static Notification createIsOnlineNotification(
            String displayName) {
        Notification notification = new Notification();
        notification.setContent(displayName + " is now Online.");
        notification.setType(NotificationType.USER_STATUS);
        return notification;
    }
    
    public static Notification createIsOfflineNotification(
            String displayName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(displayName);
        contentBuilder.append(" has gone Offline.");
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.USER_STATUS);
        return notification;
    }
    
    public static Notification createStatusChangeNotification(
            String displayName, NormalUserStatus newStatus) {
        if (newStatus == NormalUserStatus.OFFLINE) {
            throw new IllegalArgumentException("New status cannot be offline.");
        }
        
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(displayName);
        contentBuilder.append(" has changed status, ");
        contentBuilder.append("new status is ");
        contentBuilder.append(newStatus.toString());
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.USER_STATUS);
        return notification;
    }
    
    public static Notification createInvitationReceiverNotification(
            String senderName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(senderName);
        contentBuilder.append(" has sent you a contact invitaion.");
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.CONTACT_INVITATION);
        return notification;
    }
    
    public static Notification createInvitationSenderNotification(
            String receiverName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("You have sent ");
        contentBuilder.append(receiverName);
        contentBuilder.append(" a contact invitation.");
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.CONTACT_INVITATION);
        return notification;
    }
    
    public static Notification createAcceptedInvitationReceiverNotification(
            String senderName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("You have accepted ");
        contentBuilder.append(senderName);
        contentBuilder.append(" contact invitation, ");
        contentBuilder.append(senderName);
        contentBuilder.append(" was added to your contacts list.");
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.CONTACT_INVITATION);
        return notification;
    }
    
    public static Notification createAcceptedInvitationSenderNotification(
            String receiverName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(receiverName);
        contentBuilder.append(" has accepted your contact invitation, ");
        contentBuilder.append(receiverName);
        contentBuilder.append(" was added to your contacts list.");
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.CONTACT_INVITATION);
        return notification;
    }
    
    public static Notification createRejectedInvitationReceiverNotification(
            String senderName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("You have rejected ");
        contentBuilder.append(senderName);
        contentBuilder.append(" contact invitation.");
        
        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.CONTACT_INVITATION);
        return notification;
    }
    
    public static Notification createRejectedInvitationSenderNotification(
            String receiverName) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(receiverName);
        contentBuilder.append(" has rejected your contact invitation.");

        Notification notification = new Notification();
        notification.setContent(contentBuilder.toString());
        notification.setType(NotificationType.CONTACT_INVITATION);
        return notification;
    }
}
