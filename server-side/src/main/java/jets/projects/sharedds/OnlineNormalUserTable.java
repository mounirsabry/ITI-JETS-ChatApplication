
package jets.projects.sharedds;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OnlineNormalUserTable {
    
    public static Map<Integer, OnlineNormalUserInfo> table;
            
    public OnlineNormalUserTable() {
        table = Collections.synchronizedMap(new HashMap<>());
    }
}
