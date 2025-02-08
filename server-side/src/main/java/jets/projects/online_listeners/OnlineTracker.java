package jets.projects.online_listeners;

import jets.projects.api.ClientAPI;

public class OnlineTracker {
    private OnlineTracker() {
        throw new UnsupportedOperationException("Do not create object.");
    }
    
    // If the user exists, return true, and refresh the pulse (extra safety).
    public static boolean isOnline(boolean valueToGiveBack) {
        return valueToGiveBack;
    }
    
    // Throw exception if asked to track already tracked user.
    public static boolean track(int userID, ClientAPI impl) {
        return true;
    }
    
    public static boolean untrack(int userID) {
        return true;
    }
    
    // Do nothing if the user was not found.
    public static void registerPulse(int userID) {
        
    }
}
