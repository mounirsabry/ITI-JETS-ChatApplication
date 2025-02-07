package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.Notification;
import jets.projects.online_listeners.NotificationCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class NotificationsManager {

    ContactDao contactsDao = new ContactDao();
    NotificationDao notificationDao = new NotificationDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    NotificationCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public NotificationsManager() {
        this.notificatonCallback = new NotificationCallback(notificationDao, contactsDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<List<Notification>> getNotifications(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = notificationDao.getAllNotifications(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<List<Notification>> getUnReadNotifications(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = notificationDao.getUnreadNotifications(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token)  {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = notificationDao.markNotificationsAsRead(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> deleteNotification(ClientToken token, int notificationID) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = notificationDao.deleteNotification(token.getUserID(), notificationID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);

    }
}
