package jets.projects;

import jets.projects.classes.Delays;
import jets.projects.classes.ServerCommand;
import jets.projects.db_connections.ConnectionManager;
import jets.projects.shared_ds.OnlineNormalUserTable;
import jets.projects.top_controllers.AdminController;

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
        
        ExecutorsManager executorsManager = new ExecutorsManager();
        executorsManager.startExecutors();
        
        ServerManager myManager = new ServerManager();
        AdminController.setServerManager(myManager);
        
        System.out.println("Server is up and running.");
        
        while (true) {
            ServerCommand nextCommand = myManager.getAndSetNextCommand(
                    ServerCommand.WAIT);
            
            if (nextCommand == ServerCommand.START_NORMAL_USER_SERVICE) {
                ServiceManager.startNormalUserService();
                executorsManager.startExecutors();
            }
            
            if (nextCommand == ServerCommand.STOP_NORMAL_USER_SERVICE) {
                ServiceManager.stopNormalUserService();
                executorsManager.stopExecutors();
                OnlineNormalUserTable.truncate();
            }
            
            if (nextCommand == ServerCommand.SHUT_DOWN) {
                ServiceManager.stopNormalUserService();
                executorsManager.stopExecutors();
                OnlineNormalUserTable.truncate();
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