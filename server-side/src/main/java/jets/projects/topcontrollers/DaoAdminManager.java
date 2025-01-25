package jets.projects.topcontrollers;

import java.util.List;
import jets.projects.classes.AdminSessionData;
import jets.projects.classes.AdminToken;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.ServiceDao;
import jets.projects.dao.StatsDao;
import jets.projects.dao.UsersDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.entities.Announcement;

public class DaoAdminManager {
    TokenValidatorDao validatorDao = new TokenValidatorDao();
    UsersDao usersDao = new UsersDao();
    ServiceDao serviceDao = new ServiceDao();
    AnnouncementDao announcementDao = new AnnouncementDao();
    StatsDao statsDao = new StatsDao();

    public RequestResult<AdminSessionData> login(int userID, String password) {
        AdminSessionData sessionData = usersDao.adminLogin(userID, password);
        return new RequestResult<>(true, sessionData);
    }

    public RequestResult<Boolean> logout(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        boolean result = usersDao.logout(token.getUserID());
        return new RequestResult<>(true, result);
    }

    public RequestResult<Boolean> startService(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        boolean result = serviceDao.startService();
        return new RequestResult<>(true, result);
    }

    public RequestResult<Boolean> stopService(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        boolean result = serviceDao.stopService();
        return new RequestResult<>(true, result);
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

    public RequestResult<List<Integer>> getTopCountries(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        List<Integer> list = statsDao.getTopCountries();
        return new RequestResult<>(true, list);
    }

    public RequestResult<List<String>> getAllCountries(AdminToken token) {
        boolean isValidToken = validatorDao.checkAdminToken(token);
        if (!isValidToken) {
            return new RequestResult<>(false, null);
        }
        List<String> list = statsDao.getAllCountries();
        return new RequestResult<>(true, list);
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
