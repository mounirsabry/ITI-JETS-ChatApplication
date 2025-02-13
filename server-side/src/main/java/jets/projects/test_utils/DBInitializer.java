package jets.projects.test_utils;

import jets.projects.db_connections.ConnectionManager;

public class DBInitializer {
    private static boolean isInit = false;
    
    private DBInitializer() {
        throw new UnsupportedOperationException("Do not create object.");
    }
    
    public static boolean init() {
        if (isInit) {
            return true;
        }
        
        boolean isWorking = ConnectionManager.initDeviceManager();
        if (!isWorking) {
            System.err.println("Could not init the database connection");
            return false;
        }
        isInit = true;
        return true;
    }
}
