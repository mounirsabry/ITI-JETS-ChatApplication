package jets.projects.normal_user_controller_helpers;

import jets.projects.api.ClientAPI;
import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.NormalUser;
import jets.projects.online_listeners.NotificationCallback;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;

public class AuthenticationManager {
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();

    public AuthenticationManager() {
    }

    public RequestResult<ClientSessionData> login(String phoneNumber,
            String password, ClientAPI impl) {
        if (OnlineTracker.isOnline(false)) {
            return new RequestResult<>(null,
                    ExceptionMessages.ALREADY_LOGGED_IN);
        }
        
        var result = usersDao.clientLogin(phoneNumber, password);
        if (result.getErrorMessage() != null) {
            return result;
        }

        int userID = result.getResponseData().getUserID();
        OnlineTracker.track(userID, impl);
        NotificationCallback.userWentOnline(userID);
        return result;
    }

    public RequestResult<ClientSessionData> adminAccountCreatedFirstLogin(
            String phoneNumber,
            String oldPassword, String newPassword,
            ClientAPI impl) {
        if (OnlineTracker.isOnline(false)) {
            return new RequestResult<>(null,
                    ExceptionMessages.ALREADY_LOGGED_IN);
        }

        var result = usersDao.adminAccountCreatedFirstLogin(phoneNumber,
                oldPassword, newPassword);
        if (result.getErrorMessage() != null) {
            return result;
        }

        int userID = result.getResponseData().getUserID();
        OnlineTracker.track(userID, impl);
        NotificationCallback.userWentOnline(userID);
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
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(true, null);
        }
        
        var result = usersDao.clientLogout(token.getUserID());
        if (result.getErrorMessage() != null) {
            return result;
        }
        
        NotificationCallback.userWentOffline(token.getUserID());
        return result;
    }
}
