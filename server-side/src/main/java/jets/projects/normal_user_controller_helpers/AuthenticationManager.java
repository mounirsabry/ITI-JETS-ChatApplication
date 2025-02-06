package jets.projects.normal_user_controller_helpers;

import java.util.Map;

import jets.projects.api.ClientAPI;
import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.NormalUser;
import jets.projects.online_listeners.NotificatonCallback;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class AuthenticationManager {

    UsersDao usersDao = new UsersDao();
    ContactDao contactsDao = new ContactDao();
    NotificationDao notificationDao = new NotificationDao();
    
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    NotificatonCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public AuthenticationManager() {
        this.notificatonCallback = new NotificatonCallback(notificationDao, contactsDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<ClientSessionData> login(String phoneNumber,
            String password, ClientAPI impl) {
        var result = usersDao.clientLogin(phoneNumber, password);
        if (result.getErrorMessage() != null) {
            return new RequestResult<>(null, result.getErrorMessage());
        }

        notificatonCallback.userWentOnline(
                result.getResponseData().getUserID());   //callback for contacts
        return result;
    }

    public RequestResult<ClientSessionData> adminAccountCreatedFirstLogin(
            String phoneNumber,
            String oldPassword, String newPassword,
            ClientAPI impl) {

        var result = usersDao.adminAccountCreatedFirstLogin(phoneNumber,
                oldPassword, newPassword);
        if (result.getErrorMessage() != null) {
            return new RequestResult<>(null, result.getErrorMessage());
        }

        notificatonCallback.userWentOnline(
                result.getResponseData().getUserID());   //callback for contacts
        return result;
    }

    public RequestResult<Boolean> registerNormalUser(NormalUser user) {
        var result = usersDao.registerNormalUser(user);
        if (result.getErrorMessage() != null) {
            return new RequestResult(null, result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> logout(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.clientLogout(token.getUserID()); //update database
        notificatonCallback.userWentOffline(token.getUserID());//callback for contacts
        if (result.getErrorMessage() != null) {
            return new RequestResult(null, result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
