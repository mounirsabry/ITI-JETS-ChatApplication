package jets.projects.normal_user_controller_helpers;

import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.Notification;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class NotificationsManager {
    private final TokenValidatorDao tokenValidator;
    private final NotificationDao notificationDao;

    public NotificationsManager() {
       tokenValidator = new TokenValidatorDao();
       notificationDao = new NotificationDao();
    }

    public RequestResult<List<Notification>> getNotifications(ClientToken token) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        return notificationDao.getAllNotifications(
                token.getUserID());
    }

    public RequestResult<List<Notification>> getUnReadNotifications(ClientToken token) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        return notificationDao.getUnreadNotifications(
                token.getUserID());
    }

    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token)  {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        return notificationDao.markNotificationsAsRead(
                token.getUserID());
    }

    public RequestResult<Boolean> deleteNotification(ClientToken token, int notificationID) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    validationResult.getErrorMessage());
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        return notificationDao.deleteNotification(token.getUserID(),
                notificationID);
    }
}
