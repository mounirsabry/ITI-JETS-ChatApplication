package jets.projects.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;
import jets.projects.top_controllers.AdminController;
import jets.projects.entities.Announcement;
import jets.projects.api.AdminAPI;
import jets.projects.classes.ExceptionMessages;
import jets.projects.entities.NormalUser;

/** 
 * @author Mounir
 * If the ResultSet is null, then either the database is down, or
 * an unhandled problem happens in the server.
 */
public class AdminAPIImpl extends UnicastRemoteObject
        implements AdminAPI {
    private final AdminController controller;
    
    public AdminAPIImpl() throws RemoteException {
        super();
        controller = new AdminController();
    }
    
    private boolean validToken(AdminToken token) {
        return !(token == null
            ||  token.getUserID() <= 0);
    }
    
    @Override
    public AdminSessionData login(int userID, String password) throws RemoteException {
        if (userID <= 0 
        || password == null || password.isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        var result = controller.startNormalUserService(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
            
    @Override
    public boolean stopNormalUserService(AdminToken token) 
            throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        var result = controller.stopNormalUserService(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean shutDown(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        var result = controller.shutDown(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    // Get methods all the data about the users except
    // for pic and password.
    @Override
    public List<NormalUser> getAllNormalUsers(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        var result = controller.getAllNormalUsers(token);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public NormalUser getNormalUserByID(AdminToken token,
            int userID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        if (userID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = controller.getNormalUserByID(token, userID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public List<NormalUser> getNormalUserByName(AdminToken token,
            String displayName) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        if (displayName == null || displayName.isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = controller.getNormalUserByName(token, displayName);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public byte[] getNormlUserPic(AdminToken token,
            int userID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        if (userID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = controller.getNormalUserPic(token, userID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean addNormalUser(AdminToken token,
            NormalUser user) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        if (user == null 
        ||  user.getDisplayName() == null || user.getDisplayName().isBlank()
        ||  user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()
        ||  user.getEmail() == null || user.getEmail().isBlank()
        ||  user.getPassword() == null || user.getPassword().isBlank()
        ||  user.getCountry() == null
        ||  user.getBirthDate().compareTo(Date.from(Instant.MIN)) <= 0
        ||  user.getIsAdminCreated() == false
        ||  user.getIsPasswordValid() == true) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        
        if (user.getBio() == null) {
            user.setBio("");
        }
        var result = controller.addNormalUser(token, user);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public boolean deleteNormalUser(AdminToken token,
            int userID) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        if (userID <= 0) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = controller.deleteNormalUser(token, userID);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
    
    @Override
    public Announcement getLastAnnouncement(
            AdminToken token) throws RemoteException {
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
        if (!validToken(token)) {
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        if (newAnnouncement == null
        ||  newAnnouncement.getHeader() == null || newAnnouncement.getHeader().isBlank()
        ||  newAnnouncement.getContent() == null || newAnnouncement.getContent().isBlank()) {
            throw new RemoteException(ExceptionMessages.INVALID_INPUT_DATA); 
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
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
            throw new RemoteException(ExceptionMessages.INVALID_TOKEN_FORMAT);
        }
        var result = controller.getCountryUsers(token, countryName);
        if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        return result.getResponseData();
    }
}
