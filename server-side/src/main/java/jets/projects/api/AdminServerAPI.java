package jets.projects.api;

import java.util.List;
import jets.projects.classes.AdminSessionData;
import jets.projects.classes.AdminToken;
import jets.projects.classes.RequestResult;
import jets.projects.topcontrollers.DaoAdminManager;
import jets.projects.entities.Announcement;

/** 
 * @author Mounir
 * If the ResultSet is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class AdminServerAPI {
    private final DaoAdminManager controller = new DaoAdminManager();
    
    private boolean validToken(AdminToken token) {
        return !(token == null
            ||  token.getUserID() <= 0);
    }
    
    public RequestResult<AdminSessionData> login(int userID, String password) {
        if (userID <= 0 
        || password == null || password.isBlank()) {
            AdminSessionData invalidLoginData = new AdminSessionData
                (-1, null);
            return new RequestResult<>(true, invalidLoginData);
        }
        return controller.login(userID, password);
    }
    
    public RequestResult<Boolean> logout(AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.logout(token);
    }
    
    public RequestResult<Boolean> startService(AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.startService(token);
    }
            
    public RequestResult<Boolean> stopService(AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.stopService(token);
    }
    
    public RequestResult<Announcement> getLastAnnouncement(
            AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getLastAnnouncement(token);
    }
    
    public RequestResult<List<Announcement>> getAllAnnouncements(
            AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getAllAnnouncements(token);
    }
    
    public RequestResult<Boolean> submitNewAnnouncement(
            AdminToken token, Announcement newAnnouncement) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        if (newAnnouncement == null) {
            return new RequestResult<>(true, false);
        }  
        return controller.submitNewAnnouncement(token, newAnnouncement);
    }
    
    public RequestResult<List<Integer>> getOnlineOfflineStats(
            AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getOnlineOfflineStats(token);
    }
    
    public RequestResult<List<Integer>> getMaleFemaleStats(
            AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getMaleFemaleStats(token);
    }
    
    public RequestResult<List<Integer>> getTopCountries(
            AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getTopCountries(token);
    }
    
    public RequestResult<List<String>> getAllCountries(
            AdminToken token) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getAllCountries(token);
    }
    
    public RequestResult<Integer> getCountryUsers(AdminToken token,
            String countryName) {
        if (!validToken(token)) {
            return new RequestResult<>(false, null);
        }
        return controller.getCountryUsers(token, countryName);
    }
}
