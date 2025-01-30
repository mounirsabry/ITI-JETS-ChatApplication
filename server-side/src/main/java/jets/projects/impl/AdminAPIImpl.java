package jets.projects.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;
import jets.projects.topcontrollers.AdminManager;
import jets.projects.entities.Announcement;
import jets.projects.api.AdminAPI;
import jets.projects.entities.NormalUser;

/** 
 * @author Mounir
 * If the ResultSet is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class AdminAPIImpl extends UnicastRemoteObject
        implements AdminAPI {
    private final AdminManager controller;
    
    public AdminAPIImpl() throws RemoteException {
        super();
        controller = new AdminManager();
    }
    
    private boolean validToken(AdminToken token) {
        return !(token == null
            ||  token.getUserID() <= 0);
    }
    
    @Override
    public AdminSessionData login(int userID, String password) throws RemoteException {
        if (userID <= 0 
        || password == null || password.isBlank()) {
            throw new RemoteException("Invalid login data.");
        }
        var result = controller.login(userID, password);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean logout(AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.logout(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean startNormalUserService(AdminToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.startService(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
            
    @Override
    public boolean stopNormalUserService(AdminToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.stopService(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean shutDown(
            AdminToken token) throws RemoteException {
        return false;
    }
    // Get methods all the data about the users except
    // for pic and password.
    @Override
    public List<NormalUser> getAllNormalUsers(
            AdminToken token) throws RemoteException {
        return null;
    }
    
    @Override
    public NormalUser getNormalUserByID(AdminToken token,
            int userID) throws RemoteException {
        return null;
    }
    
    @Override
    public NormalUser getNormalUserByName(AdminToken token,
            String userName) throws RemoteException {
        return null;
    }
    
    @Override
    public String getNormlUserPic(AdminToken token,
            int userID) throws RemoteException {
        return null;
    }
    
    @Override
    public boolean addNormalUser(AdminToken token,
            NormalUser user) throws RemoteException {
        return false;
    }
    
    @Override
    public boolean deleteNormalUser(AdminToken token,
            int userID) throws RemoteException {
        return false;
    }
    
    @Override
    public Announcement getLastAnnouncement(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getLastAnnouncement(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Announcement> getAllAnnouncements(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getAllAnnouncements(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean submitNewAnnouncement(
            AdminToken token, Announcement newAnnouncement) throws RemoteException {
        if (!validToken(token))  {
            throw new RemoteException("Invalid token.");
        }
        if (newAnnouncement == null) {
            throw new RemoteException("The announcement cannot be null."); 
        }  
        
        var result = controller.submitNewAnnouncement(token, newAnnouncement);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Integer> getOnlineOfflineStats(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getOnlineOfflineStats(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<Integer> getMaleFemaleStats(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getMaleFemaleStats(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public Map<String,Integer> getTopCountries(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getTopCountries(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public Map<String,Integer> getAllCountries(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getAllCountries(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public int getCountryUsers(AdminToken token,
            String countryName) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException("Invalid token.");
        }
        var result = controller.getCountryUsers(token, countryName);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
}
