package jets.projects.top_controllers;

import java.rmi.RemoteException;
import java.sql.Blob;
import java.util.Date;
import java.util.Map;

import jets.projects.api.ClientAPI;
import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.Gender;
import jets.projects.online_listeners.NotificatonCallback;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class AuthenticationManager {
    ContactDao contactsDao = new ContactDao();
    NotificationDao notificationDao = new NotificationDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    NotificatonCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;
    
    public AuthenticationManager(){
        this.notificatonCallback = new NotificatonCallback(notificationDao , contactsDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }
    public RequestResult<ClientSessionData> login(String phoneNumber, String password,ClientAPI impl) throws RemoteException {
        var result = usersDao.clientLogin(phoneNumber, password);  //check database
         if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }else{
            notificatonCallback.userWentOnline(result.getResponseData().getUserID());   //callback for contacts
            return result;
        }
    }
    public RequestResult<Boolean> registerNormalUser(
            String displayName, String phoneNumber, String email, Blob pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) throws RemoteException {
        var result = usersDao.registerNormalUser(
                displayName, phoneNumber, email, pic,
                password, gender, country, birthDate, bio);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> logout(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.clientLogout(token.getUserID()); //update database
        notificatonCallback.userWentOffline(token.getUserID());//callback for contacts
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
}
