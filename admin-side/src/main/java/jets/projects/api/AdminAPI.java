package jets.projects.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;
import jets.projects.entities.Announcement;
import jets.projects.entities.Country;
import jets.projects.entities.NormalUser;

public interface AdminAPI extends Remote {
    public AdminSessionData login(
            int userID, String password) throws RemoteException;

    public boolean logout(
            AdminToken token) throws RemoteException;
    
    public boolean getNormalUserServiceStatus(
            AdminToken token) throws RemoteException;

    public boolean startNormalUserService(
            AdminToken token) throws RemoteException;

    public boolean stopNormalUserService(
            AdminToken token) throws RemoteException;
    
    public boolean shutDown(
            AdminToken token) throws RemoteException;
    
    public NormalUser getNormalUserByPhoneNumber(AdminToken token,
            String phoneNumber) throws RemoteException;
    
    public boolean addNormalUser(AdminToken token,
            NormalUser user) throws RemoteException;
    
    public boolean deleteNormalUser(AdminToken token,
            int userID) throws RemoteException;

    public List<Announcement> getAllAnnouncements(
            AdminToken token) throws RemoteException;

    public boolean submitNewAnnouncement(AdminToken token,
            Announcement newAnnouncement) throws RemoteException;

    public List<Integer> getOnlineOfflineStats(
            AdminToken token) throws RemoteException;

    public List<Integer> getMaleFemaleStats(
            AdminToken token) throws RemoteException;

    public Map<Country,Integer> getTopCountries(
            AdminToken token) throws RemoteException;
    
    public int getCountryUsers(AdminToken token,
            Country country) throws RemoteException;
}
