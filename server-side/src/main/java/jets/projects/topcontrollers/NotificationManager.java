package jets.projects.topcontrollers;

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
import jets.projects.onlinelisteners.NotificatonCallback;
import jets.projects.session.ClientToken;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class NotificationManager {
    ContactDao contactsDao = new ContactDao();
    NotificationDao notificationDao = new NotificationDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    NotificatonCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public NotificationManager(){
        this.notificatonCallback = new NotificatonCallback(notificationDao , contactsDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }
    
    public RequestResult<List<Notification>> getNotifications(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.getAllNotifications(token.getUserID()); 
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<Notification>> getUnReadNotifications(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.getUnreadNotifications(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.markNotificationsAsRead(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> deleteNotification(ClientToken token , int notificationID) throws RemoteException{
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.deleteNotification(token.getUserID() , notificationID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);

    }
}
