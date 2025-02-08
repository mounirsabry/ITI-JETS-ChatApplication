package jets.projects.normal_user_controller_helpers;

import java.rmi.RemoteException;
import java.util.List;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.Announcement;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.session.ClientToken;

public class AnnouncementsManager {
    private final AnnouncementDao announcementDao;
    private final TokenValidatorDao tokenValidator;
    
    public AnnouncementsManager() {
        announcementDao = new AnnouncementDao();
        tokenValidator = new TokenValidatorDao();
    }

    public RequestResult<List<AnnouncementInfo>> getAllAnnouncements(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = announcementDao.getAllAnnouncements(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }

    public RequestResult<List<Announcement>> getUnReadAnnouncements(ClientToken token) {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.INVALID_TOKEN);
        }
        if (!onlineUsers.containsKey(token.getUserID())) {
            return new RequestResult<>(null, ExceptionMessages.USER_TIMEOUT);
        }
        var result = announcementDao.getUnreadAnnouncements(token.getUserID());
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
}
