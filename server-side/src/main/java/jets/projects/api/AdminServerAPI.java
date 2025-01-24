package jets.projects.api;

import java.util.List;
import java.util.Optional;
import jets.projects.classes.AdminSessionData;
import jets.projects.classes.AdminToken;
import jets.projects.classes.RequestResult;
import jets.projects.dao.DaoAdminController;
import jets.projects.entities.Announcement;

/** 
 * @author Mounir
 * If the optional is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class AdminServerAPI {
    private DaoAdminController controller = new DaoAdminController();
    
    public Optional<AdminSessionData> login(int userID, String password) {
        return null;
    }
    
    public Optional<RequestResult<Boolean>> logout(AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<Boolean>> startService(AdminToken token) {
        return null;
    }
            
    public Optional<RequestResult<Boolean>> stopService(AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<Announcement>> getLastAnnouncement(
            AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<List<Announcement>>> getAnnouncements(
            AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<Boolean>> submitNewAnnouncement(
            AdminToken token, Announcement newAnnouncement) {
        return null;
    }
    
    public Optional<RequestResult<List<Integer>>> getOnlineOfflineStats(
            AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<List<Integer>>> getMaleFemaleStats(
            AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<List<Integer>>> getTopCountries(
            AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<List<String>>> getAllCountries(
            AdminToken token) {
        return null;
    }
    
    public Optional<RequestResult<Integer>> getCountryUsers(AdminToken token,
            String countryName) {
        return null;
    }
}
