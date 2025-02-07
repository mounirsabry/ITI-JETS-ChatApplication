
package jets.projects.shared_ds;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OnlineNormalUserTable {
    
    public static Map<Integer, OnlineNormalUserInfo> table;
  
    public OnlineNormalUserTable() {
        table = Collections.synchronizedMap(new HashMap<>());
    }
    
    public static Map<Integer , OnlineNormalUserInfo> getOnlineUsersTable(){
		return table;
	}
}
