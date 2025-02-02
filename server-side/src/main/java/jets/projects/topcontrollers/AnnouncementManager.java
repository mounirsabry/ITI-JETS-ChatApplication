package jets.projects.topcontrollers;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.Announcement;
import jets.projects.onlinelisteners.AnnouncementCallback;
import jets.projects.session.ClientToken;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;

public class AnnouncementManager {
    AnnouncementDao announcementDao;
    TokenValidatorDao tokenValidator;
    AnnouncementCallback announcementCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public AnnouncementManager(){
        announcementDao = new AnnouncementDao();
        announcementCallback = new AnnouncementCallback(announcementDao);

        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }
    public RequestResult<List<Announcement>> getAnnouncements(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = announcementDao.getAllAnnouncements(token.getUserID()); 
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<Announcement>> getUnReadAnnouncements(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = announcementDao.getUnreadAnnouncements(token.getUserID()); 
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
}
