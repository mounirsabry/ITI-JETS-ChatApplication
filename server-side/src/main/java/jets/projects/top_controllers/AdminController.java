package jets.projects.top_controllers;

import jets.projects.classes.ServerCommand;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import jets.projects.ServerManager;
import jets.projects.classes.ExceptionMessages;

import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AdminDao;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.StatsDao;
import jets.projects.dao.UsersDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersManipulationDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.Announcement;
import jets.projects.entities.NormalUser;

public class AdminController {
    private static ServerManager serverManager;
    
    private final TokenValidatorDao validatorDao = new TokenValidatorDao();
    private final UsersDao usersDao = new UsersDao();
    private final UsersQueryDao usersQueryDao = new UsersQueryDao();
    private final UsersManipulationDao usersManipulationDao =
            new UsersManipulationDao();
    private final AdminDao adminDao = new AdminDao();
    private final AnnouncementDao announcementDao = new AnnouncementDao();
    private final StatsDao statsDao = new StatsDao();
    
    public static void setServerManager(ServerManager manager) {
        serverManager = manager;
    }

    public RequestResult<AdminSessionData> login(int userID, String password) throws RemoteException {
        var result = adminDao.adminLogin(userID, password);
        return result;
    }

    public RequestResult<Boolean> logout(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return adminDao.adminLogout(token.getUserID());
    }

    public RequestResult<Boolean> startNormalUserService(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        serverManager.setNextCommand(
                ServerCommand.START_NORMAL_USER_SERVICE);
        return new RequestResult<>(true, null);
    }

    public RequestResult<Boolean> stopNormalUserService(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        serverManager.setNextCommand(
                ServerCommand.STOP_NORMAL_USER_SERVICE);
        return new RequestResult<>(true, null);
    }
    
    public RequestResult<Boolean> shutDown(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        serverManager.setNextCommand(
                ServerCommand.SHUT_DOWN);
        return new RequestResult<>(true, null);
    }
    
    public RequestResult<List<NormalUser>> getAllNormalUsers(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return usersQueryDao.getAllNormalUsers();
    }
    
    public RequestResult<NormalUser> getNormalUserByID(AdminToken token, int userID) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return usersQueryDao.getNormalUserByID(userID);
    }
    
    public RequestResult<List<NormalUser>> getNormalUserByName(AdminToken token,
            String displayName) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return usersQueryDao.getNormalUserByName(displayName);
    }
    
    public RequestResult<byte[]> getNormalUserPic(AdminToken token, int userID) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        RequestResult<Boolean> isUsersExists = usersQueryDao.isNormalUserExists(userID);
        if (isUsersExists.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUsersExists.getErrorMessage());
        }
        if (isUsersExists.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        
        return usersDao.getNormalUserProfilePic(userID);
    }
    
    public RequestResult<Boolean> addNormalUser(AdminToken token, NormalUser user) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        return usersManipulationDao.addNormalUser(user);
    }
    
    public RequestResult<Boolean> deleteNormalUser(AdminToken token, int userID) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        RequestResult<Boolean> isUsersExists = usersQueryDao.isNormalUserExists(userID);
        if (isUsersExists.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isUsersExists.getErrorMessage());
        }
        if (isUsersExists.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        return usersManipulationDao.deleteNormalUser(userID);
    }

    public RequestResult<Announcement> getLastAnnouncement(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return announcementDao.getLastAnnouncement();
    }

    public RequestResult<List<Announcement>> getAllAnnouncements(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return announcementDao.getAllSubmitedAnnouncements();
    }

    public RequestResult<Boolean> submitNewAnnouncement(AdminToken token,
            Announcement newAnnouncement) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return announcementDao.submitNewAnnouncement(newAnnouncement);
    }

    public RequestResult<List<Integer>> getOnlineOfflineStats(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return statsDao.getOnlineOfflineStats();
    }

    public RequestResult<List<Integer>> getMaleFemaleStats(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return statsDao.getMaleFemaleStats();
    }

    public RequestResult<Map<String,Integer>> getTopCountries(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return statsDao.getTopCountries();
    }

    public RequestResult<Map<String,Integer>> getAllCountries(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return statsDao.getAllCountries();
    }

    public RequestResult<Integer> getCountryUsers(AdminToken token, String countryName) {
        RequestResult<Boolean> isValidTokenResult = validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return statsDao.getCountryUsers(countryName);
    }
}
