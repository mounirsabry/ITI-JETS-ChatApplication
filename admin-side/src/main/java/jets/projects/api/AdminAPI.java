package jets.projects.api;

import jets.projects.entities.Announcement;
import jets.projects.entities.NormalUser;
import jets.projects.session.AdminSessionData;
import jets.projects.session.AdminToken;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface AdminAPI extends Remote {
    public AdminSessionData login(
            String phoneNumber, String password) throws RemoteException;

    public boolean logout(
            AdminToken token) throws RemoteException;

    public boolean startNormalUserService(
            AdminToken token) throws RemoteException;

    public boolean stopNormalUserService(
            AdminToken token) throws RemoteException;

    public boolean getNormalUserServiceStatus(
            AdminToken token) throws RemoteException;

    public boolean shutDown(
            AdminToken token) throws RemoteException;
    
    // Get methods all the data about the users except
    // for pic and password.
    public List<NormalUser> getAllNormalUsers(
            AdminToken token) throws RemoteException;
    
    public NormalUser getNormalUserByID(AdminToken token,
                                        int userID) throws RemoteException;

    public NormalUser getNormalUserByPhoneNumber(AdminToken token,
                                        String phoneNumber) throws RemoteException;
    
    public NormalUser getNormalUserByName(AdminToken token,
                                          String userName) throws RemoteException;
    
    public String getNormalUserPic(AdminToken token,
            int userID) throws RemoteException;
    
    public boolean addNormalUser(AdminToken token,
            NormalUser user) throws RemoteException;

    public boolean updateNormalUser(
            AdminToken token, NormalUser user) throws RemoteException;


    public boolean deleteNormalUser(AdminToken token,
            int userID) throws RemoteException;

    public Announcement getLastAnnouncement(
            AdminToken token) throws RemoteException;

    public List<Announcement> getAllAnnouncements(
            AdminToken token) throws RemoteException;

    public boolean submitNewAnnouncement(AdminToken token,
            Announcement newAnnouncement) throws RemoteException;

    public List<Integer> getOnlineOfflineStats(
            AdminToken token) throws RemoteException;

    public List<Integer> getMaleFemaleStats(
            AdminToken token) throws RemoteException;

    public Map<String,Integer> getTopCountries(
            AdminToken token) throws RemoteException;

    public Map<String,Integer> getAllCountries(
            AdminToken token) throws RemoteException;

    public int getCountryUsers(AdminToken token,
            String countryName) throws RemoteException;
}
