package jets.projects;

import jets.projects.classes.ClientToken;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dbconnections.ConnectionManager;

public class App {
    public static void main(String[] args) {
        // Initialize the connection manager with the config from the file.
        boolean isBDWorking = ConnectionManager.initDeviceManager();
        if (!isBDWorking) {
            System.err.println("Server will abort.");
            return;
        }
        
        ClientToken token = new ClientToken("1", 0);
        System.out.println(token);
        TokenValidatorDao dao = new TokenValidatorDao();
        boolean result = dao.checkClientToken(token);
        System.out.println(result);
        System.out.println("Hello from server-side application.");
    }
}