package jets.projects.dao;

import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.entities.Notification;

public class NotificationDao {

    public RequestResult<List<Notification>> getAllNotifications(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllNotifications'");
    }

    public RequestResult<Boolean> markNotificationsAsRead(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markNotificationsAsRead'");
    }

    public RequestResult<List<Notification>> getUnreadNotifications(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnreadNotifications'");
    }

    public RequestResult<Boolean> deleteNotification(int userID, int notificationID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteNotification'");
    }
    
}
