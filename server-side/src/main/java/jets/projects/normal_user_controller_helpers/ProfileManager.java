package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.ContactDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.online_listeners.NotificatonCallback;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class ProfileManager {

    ContactDao contactsDao = new ContactDao();
    NotificationDao notificationDao = new NotificationDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    NotificatonCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public ProfileManager() {
        this.notificatonCallback = new NotificatonCallback(notificationDao, contactsDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }

    public RequestResult<byte[]> getProfilePic(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.getNormalUserProfilePic(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if (newStatus == null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = usersDao.setOnlineStatus(token.getUserID(), newStatus); //update database
        notificatonCallback.userStatusChanged(token.getUserID(), newStatus);//callback for contacts
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }

    public RequestResult<NormalUser> getMyProfile(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.getNormalUserProfile(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> editProfile(ClientToken token,
            String username, String bio, byte[] profilePic) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.saveProfileChanges(token.getUserID(), username, bio, profilePic);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> changePassword(ClientToken token,
            String oldPassword, String newPassword) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }

        boolean validOldPassword = usersDao.isPasswordValid(token.getUserID(), oldPassword).getResponseData();
        if (!validOldPassword) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = usersDao.updatePassword(token.getUserID(), oldPassword, newPassword);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
