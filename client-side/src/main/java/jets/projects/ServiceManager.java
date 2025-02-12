package jets.projects;

import jets.projects.ClientApiImpl.ClientAPIImpl;
import jets.projects.api.NormalUserAPI;
import jets.projects.session.ClientSessionData;
import jets.projects.session.ClientToken;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServiceManager {
    private NormalUserAPI serverAPI = null;
    private ClientAPIImpl myClientAPIImpl = null;
    private ClientSessionData mySession = null;
    private ClientToken myToken = null;
    
    private static final String NORMAL_USER_SERVICE_NAME = "NormalUserChatService";
    private static final int NORMAL_USER_SERVICE_PORT = 1100;

    private static ServiceManager serviceManager = null;
    private ServiceManager(){

    }
    public static ServiceManager getInstance(){
        if(serviceManager == null){
           serviceManager = new ServiceManager();
           if(serviceManager.startServiceManager() == false) {
               serviceManager = null;
               return null;
           }
        }
        return serviceManager;
    }
    private boolean startServiceManager() {
        try {
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 
                    NORMAL_USER_SERVICE_PORT);
            serverAPI = (NormalUserAPI)reg.lookup(
                    NORMAL_USER_SERVICE_NAME);
            myClientAPIImpl = new ClientAPIImpl();
            return true;
        } catch (RemoteException | NotBoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void stopService(){
        serviceManager = null;
    }

    public NormalUserAPI getNormalUserAPI(){
        return serverAPI;
    }

    public ClientAPIImpl getMyClientAPIImpl() {
        return myClientAPIImpl;
    }

    public ClientSessionData getClientSessionData() {
        return mySession;
    }

    public ClientToken getClientToken() {
        return myToken;
    }

    public void setClientSessionData(ClientSessionData mySession) {
        this.mySession = mySession;
        this.myToken = new ClientToken(mySession.getPhoneNumber(), mySession.getUserID());
    }
}
