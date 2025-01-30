package jets.projects.topcontrollers;

import io.tasks.classes.ServerCommand;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import jets.projects.ServerManager;

import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.StatsDao;
import jets.projects.dao.UsersDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.Announcement;

public class AdminManager {
    private static ServerManager serverManager;
    
    private TokenValidatorDao validatorDao = new TokenValidatorDao();
    private UsersDao usersDao = new UsersDao();
    private AnnouncementDao announcementDao = new AnnouncementDao();
    private StatsDao statsDao = new StatsDao();
    
    public static void setServerManager(ServerManager manager) {
        serverManager = manager;
    }

    public RequestResult<AdminSessionData> login(int userID, String password) {
        var result = usersDao.adminLogin(userID, password);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }

    public RequestResult<Boolean> logout(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        boolean result = usersDao.adminLogout(token.getUserID());
        return new RequestResult<>(true, result);
    }

    public RequestResult<Boolean> startService(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        serverManager.setNextCommand(
                ServerCommand.START_NORMAL_USER_SERVICE);
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> stopService(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        serverManager.setNextCommand(
                ServerCommand.STOP_NORMAL_USER_SERVICE);
        return new RequestResult<>(true, null);
    }
    
    public RequestResult<Boolean> shutDown(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        serverManager.setNextCommand(
                ServerCommand.SHUT_DOWN);
        return new RequestResult<>(true, null);
    }

    public RequestResult<Announcement> getLastAnnouncement(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        Announcement announcement = announcementDao.getLastAnnouncement();
        return new RequestResult<>(true, announcement);
    }

    public RequestResult<List<Announcement>> getAllAnnouncements(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        List<Announcement> announcements = announcementDao.getAllAnnouncements();
        return new RequestResult<>(true, announcements);
    }

    public RequestResult<Boolean> submitNewAnnouncement(AdminToken token,
            Announcement newAnnouncement) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        boolean result = announcementDao.submitNewAnnouncement(newAnnouncement);
        return new RequestResult<>(true, result);
    }

    public RequestResult<List<Integer>> getOnlineOfflineStats(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        List<Integer> list = statsDao.getOnlineOfflineStats();
        return new RequestResult<>(true, list);
    }

    public RequestResult<List<Integer>> getMaleFemaleStats(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        List<Integer> list = statsDao.getMaleFemaleStats();
        return new RequestResult<>(true, list);
    }

    public RequestResult<Map<String,Integer>> getTopCountries(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        Map<String,Integer> map = statsDao.getTopCountries();
        return new RequestResult<>(true, map);
    }

    public RequestResult<Map<String,Integer>> getAllCountries(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        Map<String,Integer> map = statsDao.getAllCountries();
        return new RequestResult<>(true, map);
    }

    public RequestResult<Integer> getCountryUsers(AdminToken token, String countryName) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        Integer countryUsers = statsDao.getCountryUsers(countryName);
        return new RequestResult<>(true, countryUsers);
    }
    
}
