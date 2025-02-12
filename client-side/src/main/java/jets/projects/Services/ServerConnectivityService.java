package jets.projects.Services;

import jets.projects.ServiceManager;
import jets.projects.api.NormalUserAPI;
import jets.projects.session.ClientToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnectivityService {
    private static ExecutorService executorService;
    private static ServiceManager serviceManager = null;
    private static NormalUserAPI serverAPI = null;
    private static ClientToken myToken = null;

    public static boolean check(){
        serviceManager = ServiceManager.getInstance();
        if(serviceManager == null){
            return false;
        }
        serverAPI = serviceManager.getNormalUserAPI();
        myToken = serviceManager.getClientToken();
        if(serverAPI == null || myToken == null){
            return false;
        }
        return true;
    }

    public static NormalUserAPI getServerAPI() {
        return serverAPI;
    }

    public static ClientToken getMyToken() {
        return myToken;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }
    public static void startWorking() {
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newSingleThreadExecutor();
            System.out.println("Server connectivity service started.");
        } else {
            System.out.println("Server connectivity already working");
        }
    }

    public static void shutDown(){
        if(executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        else{
            System.out.println("already server connectivity shutdown");
        }
    }
}
