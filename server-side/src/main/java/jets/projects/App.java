package jets.projects;

import jets.projects.dbconnections.ConnectionManager;

public class App {
    public static void main(String[] args) {
        // Initialize the connection manager with the config from the file.
        boolean isBDWorking = ConnectionManager.initDeviceManager();
        if (!isBDWorking) {
            System.err.println("Server will abort.");
            return;
        }
        
        System.out.println("Hello from server-side application.");
    }
}