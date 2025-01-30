package jets.projects;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import jets.projects.api.AdminAPI;
import jets.projects.api.NormalUserAPI;
import jets.projects.impl.AdminAPIImpl;
import jets.projects.impl.NormalUserAPIImpl;

public class ServiceManager {
    private static final String NORMAL_USER_SERVICE_NAME = "NormalUserChatService";
    private static final String ADMIN_SERVICE_NAME = "AdminService";
    
    private static boolean isNormalUserServiceOnline = false;
    private static boolean isAdminServiceOnline = false;
    
    private static NormalUserAPI normalUserAPI;
    private static AdminAPI adminAPI;
    
    static boolean startAdminService() {
        if (isAdminServiceOnline) {
            return true;
        }
        
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(1099);
        } catch (AccessException ex) {
            System.err.println("Server denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            System.err.println("Server could not create a registry.");
            return false;
        }
        
        try {
            adminAPI = new AdminAPIImpl();
        } catch (RemoteException ex) {
            System.err.println("Server could create an impl object.");
            return false;
        }
        
        try {
            reg.rebind(ADMIN_SERVICE_NAME, adminAPI);
        } catch (RemoteException ex) {
            System.err.println("Server could not publish the impl.");
            return false;
        }
        
        isAdminServiceOnline = true;
        return true;
    }
    
    static boolean startNormalUserService() {
        if (isNormalUserServiceOnline) {
            return true;
        }
        
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(1099);
        } catch (AccessException ex) {
            System.err.println("Server denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            System.err.println("Server could not create a registry.");
            return false;
        }
        
        try {
            normalUserAPI = new NormalUserAPIImpl();
        } catch (RemoteException ex) {
            System.err.println("Server could create an impl object.");
            return false;
        }
        
        try {
            reg.rebind(NORMAL_USER_SERVICE_NAME, normalUserAPI);
        } catch (RemoteException ex) {
            System.err.println("Server could not publish the impl.");
            return false;
        }
        
        isNormalUserServiceOnline = true;
        return true;
    }
    
    static boolean stopNormalUserService() {
        if (!isNormalUserServiceOnline) {
            return true;
        }
        
        Registry reg;
        try {
            reg = LocateRegistry.getRegistry(1099);
        } catch (AccessException ex) {
            System.err.println("Server denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            System.err.println("Server could not create a registry.");
            return false;
        }
        
        try {
            reg.unbind(NORMAL_USER_SERVICE_NAME);
        } catch (NotBoundException ex) {
            System.out.println("The service is already unbounded.");
        } catch (RemoteException ex) {
            System.err.println("Failed to unbind the service.");
            return false;
        }
        
        isNormalUserServiceOnline = false;
        return true;
    }
    
    static boolean stopAdminService() {
        if (!isAdminServiceOnline) {
            return true;
        }
        
        Registry reg;
        try {
            reg = LocateRegistry.getRegistry(1099);
        } catch (AccessException ex) {
            System.err.println("Server denied access to registry.");
            return false;
        } catch (RemoteException ex) {
            System.err.println("Server could not create a registry.");
            return false;
        }
        
        try {
            reg.unbind(ADMIN_SERVICE_NAME);
        } catch (NotBoundException ex) {
            System.out.println("The service is already unbounded.");
        } catch (RemoteException ex) {
            System.err.println("Failed to unbind the service.");
            return false;
        }
        
        isAdminServiceOnline = false;
        return true;
    }
}
