package jets.projects;

import jets.projects.classes.*;
import jets.projects.dbconnections.ConnectionManager;
import jets.projects.topcontrollers.AdminManager;

public class App {
    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) {
        // Initialize the connection manager with the config from the file.
        boolean isBDWorking = ConnectionManager.initDeviceManager();
        if (!isBDWorking) {
            System.err.println("Server will abort.");
            return;
        }
        
        if (ServiceManager.startAdminService() == false) {
            System.err.println("Could not start the admin service.");
            System.err.println("Server will abort.");
            return;
        }
        
        if (ServiceManager.startNormalUserService() == false) {
            System.err.println("Could not start the normal user service.");
            System.err.println("Server will abort.");
            return;
        }
        
        ServerManager myManager = new ServerManager();
        AdminManager.setServerManager(myManager);
        
        System.out.println("Server is up and running.");
        
        while (true) {
            ServerCommand nextCommand = myManager.getAndSetNextCommand(
                    ServerCommand.WAIT);
            
            if (nextCommand == ServerCommand.START_NORMAL_USER_SERVICE) {
                ServiceManager.startNormalUserService();
            }
            
            if (nextCommand == ServerCommand.STOP_NORMAL_USER_SERVICE) {
                ServiceManager.stopNormalUserService();
            }
            
            if (nextCommand == ServerCommand.SHUT_DOWN) {
                ServiceManager.stopNormalUserService();
                ServiceManager.stopAdminService();
                break;
            }
            
            try {
                Thread.sleep(Delays.SERVER_CHECK_NEXT_COMMAND_DELAY);
            } catch (InterruptedException ex) {
                System.out.println("Something interrupted the main thread.");
                return;
            }
        }
    }
}