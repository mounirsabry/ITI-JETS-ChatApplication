package jets.projects.top_controllers;

import jets.projects.classes.ServerCommand;
import java.util.List;
import java.util.Map;
import jets.projects.ServerManager;
import jets.projects.ServiceManager;
import jets.projects.classes.ExceptionMessages;

import jets.projects.online_listeners.AnnouncementCallback;
import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;
import jets.projects.classes.RequestResult;
import jets.projects.dao.AdminDao;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.StatsDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersManipulationDao;
import jets.projects.dao.UsersQueryDao;
import jets.projects.entities.Announcement;
import jets.projects.entities.Country;
import jets.projects.entities.NormalUser;

public class AdminController {
    private static ServerManager serverManager;
    
    private final TokenValidatorDao validatorDao;
    private final UsersQueryDao usersQueryDao;
    private final UsersManipulationDao usersManipulationDao;
    private final AdminDao adminDao;
    private final AnnouncementDao announcementDao;
    private final StatsDao statsDao;
    
    public AdminController() {
        validatorDao = new TokenValidatorDao();
        usersQueryDao = new UsersQueryDao();
        usersManipulationDao = new UsersManipulationDao();
        adminDao = new AdminDao();
        announcementDao = new AnnouncementDao();
        statsDao = new StatsDao();
    }
    
    public static void setServerManager(ServerManager manager) {
        serverManager = manager;
    }

    public RequestResult<AdminSessionData> login(int userID,
            String password) {
        return adminDao.adminLogin(userID, password);

    }

    public RequestResult<Boolean> logout(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        return new RequestResult<>(true, null);
    }
    
    public RequestResult<Boolean> getNormalUserServiceStatus(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        return new RequestResult<>(
                ServiceManager.getIsNormalUserServiceOnline(),
                null);
    }

    public RequestResult<Boolean> startNormalUserService(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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
    
    public RequestResult<NormalUser> getNormalUserByPhoneNumber(AdminToken token,
            String phoneNumber) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        return usersQueryDao.getNormalUserByPhoneNumber(phoneNumber);
    }
    
    public RequestResult<Boolean> addNormalUser(AdminToken token, NormalUser user) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        RequestResult<Boolean> isUsersExists = 
                usersQueryDao.isNormalUserExistsByID(userID);
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

    public RequestResult<List<Announcement>> getAllAnnouncements(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        return announcementDao.getAllAnnouncements();
    }

    public RequestResult<Boolean> submitNewAnnouncement(AdminToken token,
                                                                           Announcement newAnnouncement) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }

        var result = announcementDao.submitNewAnnouncement(newAnnouncement);
        if(result.getErrorMessage() != null){
            return new RequestResult<>(null, result.getErrorMessage());
        }
        AnnouncementCallback.newAnnouncementAdded(result.getResponseData());
        return new RequestResult<>(true, null);
    }

    public RequestResult<List<Integer>> getOnlineOfflineStats(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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

    public RequestResult<Map<Country,Integer>> getTopCountries(AdminToken token) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
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

    public RequestResult<Integer> getCountryUsers(AdminToken token, Country country) {
        RequestResult<Boolean> isValidTokenResult = 
                validatorDao.checkAdminToken(token);
        if (isValidTokenResult.getErrorMessage() != null) {
            return new RequestResult<>(null,
                    isValidTokenResult.getErrorMessage());
        }
        if (isValidTokenResult.getResponseData() == false) {
            return new RequestResult<>(null,
                    ExceptionMessages.INVALID_TOKEN);
        }
        
        return statsDao.getCountryUsers(country);
    }
}
