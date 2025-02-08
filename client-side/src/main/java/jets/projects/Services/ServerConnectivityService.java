package jets.projects.Services;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.ServiceManager;
import jets.projects.api.NormalUserAPI;
import jets.projects.session.ClientToken;

public class ServerConnectivityService {
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
}
