
package jets.projects.shared_ds;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OnlineNormalUserTable {
    
    public static Map<Integer, OnlineNormalUserInfo> table 
            = Collections.synchronizedMap(new HashMap<>());
  
    private OnlineNormalUserTable() {
        throw new UnsupportedOperationException("Do not create object.");
    }
    
    public static void truncate() {
        table.clear();
    }
}
