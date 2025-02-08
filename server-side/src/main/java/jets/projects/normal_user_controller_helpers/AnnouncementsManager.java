package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.Announcement;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.online_listeners.OnlineTracker;
import jets.projects.session.ClientToken;

public class AnnouncementsManager {
    private final TokenValidatorDao tokenValidator;
    private final AnnouncementDao announcementDao;
    
    public AnnouncementsManager() {
        tokenValidator = new TokenValidatorDao();
        announcementDao = new AnnouncementDao();
    }

    public RequestResult<List<AnnouncementInfo>> getAllAnnouncements(ClientToken token) {
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
        
        return announcementDao.getAllAnnouncements(token.getUserID());
    }

    public RequestResult<List<Announcement>> getUnReadAnnouncements(ClientToken token) {
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
        
        return announcementDao.getUnreadAnnouncements(token.getUserID());
    }
}
