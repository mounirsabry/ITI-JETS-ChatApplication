package jets.projects.normal_user_controller_helpers;

import jets.projects.api.ClientAPI;
import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.online_listeners.NotificationCallback;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class AuthenticationManager {
    private final UsersQueryDao usersQueryDao;
    private final UsersDao usersDao;
    private final TokenValidatorDao tokenValidator;

    public AuthenticationManager() {
        usersQueryDao = new UsersQueryDao();
        usersDao = new UsersDao();
        tokenValidator = new TokenValidatorDao();
    }

    public RequestResult<ClientSessionData> login(String phoneNumber,
            String password, ClientAPI impl) {
        var getUserIDResult 
                = usersQueryDao.isNormalUserExistsByPhoneNumber(phoneNumber);
        if (getUserIDResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    getUserIDResult.getErrorMessage());
        }
        Integer ID = getUserIDResult.getResponseData();
        if (ID == null) {
            return new RequestResult<>(null, null);
        }
        
        if (OnlineTracker.isOnline(ID)) {
            return new RequestResult<>(null,
                    ExceptionMessages.ALREADY_LOGGED_IN);
        }
        
        var result = usersDao.clientLogin(phoneNumber, password);
        if (result.getErrorMessage() != null) {
            return result;
        }

        int userID = result.getResponseData().getUserID();
        OnlineTracker.track(userID, impl);
        NotificationCallback.userStatusChanged(
                userID, NormalUserStatus.AVAILABLE);
        return result;
    }

    public RequestResult<ClientSessionData> adminAccountCreatedFirstLogin(
            String phoneNumber,
            String oldPassword, String newPassword,
            ClientAPI impl) {
        var getUserIDResult 
                = usersQueryDao.isNormalUserExistsByPhoneNumber(phoneNumber);
        if (getUserIDResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    getUserIDResult.getErrorMessage());
        }
        Integer ID = getUserIDResult.getResponseData();
        if (ID == null) {
            return new RequestResult<>(null, null);
        }
        
        if (OnlineTracker.isOnline(ID)) {
            return new RequestResult<>(null,
                    ExceptionMessages.ALREADY_LOGGED_IN);
        }
        
        var isPasswordValidResult = usersDao.isPasswordValid(
                ID, oldPassword);
        if (isPasswordValidResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                   isPasswordValidResult.getErrorMessage()); 
        }
        boolean isPasswordValid = isPasswordValidResult.getResponseData();
        if (!isPasswordValid) {
            return new RequestResult<>(null, null);
        }
        
        var passwordUpdateResult = usersDao.updatePassword(
                ID, oldPassword, newPassword);
        if (passwordUpdateResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    passwordUpdateResult.getErrorMessage());
        }
        boolean passwordUpdate = passwordUpdateResult.getResponseData();
        if (!passwordUpdate) {
            return new RequestResult<>(null, 
                    "Failed to update the password.");
        }

        var result = usersDao.clientLogin(phoneNumber, newPassword);
        if (result.getErrorMessage() != null) {
            return result;
        }

        int userID = result.getResponseData().getUserID();
        OnlineTracker.track(userID, impl);
        NotificationCallback.userStatusChanged(
                userID, NormalUserStatus.AVAILABLE);
        return result;
    }

    public RequestResult<Boolean> registerNormalUser(NormalUser user) {
        var result = usersDao.registerNormalUser(user);
        return result;
    }

    public RequestResult<Boolean> logout(ClientToken token) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return validationResult;
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(false,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        // The user is already logged out or timeout.
        if (!OnlineTracker.isOnline(token.getUserID())) {
            return new RequestResult<>(true, null);
        }
        
        var result = usersDao.clientLogout(token.getUserID());
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        NotificationCallback.userStatusChanged(token.getUserID(),
                NormalUserStatus.OFFLINE);

        var table = OnlineNormalUserTable.table;
        table.remove(token.getUserID());
        return result;
    }

    public RequestResult<Boolean> registerPulse(ClientToken token) {
        var validationResult = tokenValidator.checkClientToken(token);
        if (validationResult.getErrorMessage() != null) {
            return validationResult;
        }
        boolean isTokenValid = validationResult.getResponseData();
        if (!isTokenValid) {
            return new RequestResult<>(false,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        OnlineTracker.registerPulse(token.getUserID());
        return new RequestResult<>(true, null);
    }
}
