package jets.projects;

import java.util.Date;
import java.util.Map;

import jets.projects.classes.AdminSessionData;
import jets.projects.classes.AdminToken;
import jets.projects.classes.ClientToken;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.StatsDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.entities.Announcement;

public class App {
    public static void main(String[] args) {
        // Initialize the connection manager with the config from the file.
        boolean isBDWorking = ConnectionManager.initDeviceManager();
        if (!isBDWorking) {
            System.err.println("Server will abort.");
            return;
        }
        
        StatsDao statsDao = new StatsDao();
        System.out.println(
            "online: "+ statsDao.getOnlineOfflineStats().get(0) 
            + "   offline: " +statsDao.getOnlineOfflineStats().get(1));
        
        System.out.println(
            "male: "+ statsDao.getMaleFemaleStats().get(0) 
            + "   female: " +statsDao.getMaleFemaleStats().get(1));

        Map<String, Integer> topCountries = statsDao.getTopCountries();

        System.out.println("Top 3 Countries:");
        for (Map.Entry<String, Integer> entry : topCountries.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " users");
        }

        Map<String, Integer> allCountries = statsDao.getAllCountries();

        System.out.println("All countries:");
        for (Map.Entry<String, Integer> entry : allCountries.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " users");
        }

        System.out.println("USA users: "+statsDao.getCountryUsers("USA"));
        System.out.println("Hello from server-side application.");
    }
}