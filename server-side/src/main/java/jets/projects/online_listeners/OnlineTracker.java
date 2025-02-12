package jets.projects.online_listeners;

import jets.projects.api.ClientAPI;
import jets.projects.shared_ds.OnlineNormalUserInfo;
import jets.projects.shared_ds.OnlineNormalUserTable;

public class OnlineTracker {
    private OnlineTracker() {
        throw new UnsupportedOperationException("Do not create object.");
    }
    
    // If the user exists, return true, and refresh the pulse (extra safety).
    public static boolean isOnline(int userID) {
        var table = OnlineNormalUserTable.table;
        OnlineNormalUserInfo user = table.getOrDefault(
                userID, null);
        if (user == null) {
            return false;
        }
        user.setLastRefreshed();
        return true;
    }
    
    // Throw exception if asked to track already tracked user.
    public static boolean track(int userID, ClientAPI impl) {
        var table = OnlineNormalUserTable.table;
        if (table.containsKey(userID)) {
            throw new IllegalStateException(
                    "The user is already tracked.");
        }
        table.put(userID, new OnlineNormalUserInfo(impl));
        return true;
    }
    
    public static boolean untrack(int userID) {
        var table = OnlineNormalUserTable.table;
        OnlineNormalUserInfo user = table.remove(userID);
        return user != null;
    }
    
    // Do nothing if the user was not found.
    public static void registerPulse(int userID) {
        var table = OnlineNormalUserTable.table;
        OnlineNormalUserInfo user = table.getOrDefault(
                userID, null);
        if (user == null) {
            return;
        }
        user.setLastRefreshed();
    }
}
