package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.Date;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class ProfilesManager {
    UsersDao usersDao;
    TokenValidatorDao tokenValidator;

    public ProfilesManager() {
        usersDao = new UsersDao();
        tokenValidator = new TokenValidatorDao();
    }

    public RequestResult<NormalUser> getMyProfile(ClientToken token) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }

        return usersDao.getNormalUserProfile(token.getUserID());
    }

    public RequestResult<Boolean> editProfile(ClientToken token,
            String username, Date birthDate,
            String bio, byte[] profilePic) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        return usersDao.editProfile(token.getUserID(), username,
                birthDate, bio, profilePic);
    }

    public RequestResult<Boolean> changePassword(ClientToken token,
            String oldPassword, String newPassword) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }

        var passwordValidation = usersDao.isPasswordValid(
                token.getUserID(), oldPassword);
        if (passwordValidation.getErrorMessage() != null) {
            
        }
        if (!isOldPasswordValid) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        return usersDao.updatePassword(token.getUserID(), oldPassword, newPassword);
    }
    
    public RequestResult<byte[]> getProfilePic(ClientToken token) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        var result = usersDao.getNormalUserProfilePic(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<Boolean> setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) {
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
        
        if (!OnlineTracker.isOnline(true)) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_TIMEOUT);
        }
        
        var result = usersDao.setOnlineStatus(token.getUserID(), newStatus); //update database
        notificatonCallback.userStatusChanged(token.getUserID(), newStatus);//callback for contacts
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(true, null);
    }
}
