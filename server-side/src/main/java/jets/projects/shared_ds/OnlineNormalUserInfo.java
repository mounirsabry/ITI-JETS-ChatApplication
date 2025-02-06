package jets.projects.shared_ds;

import java.sql.Timestamp;
import java.time.Instant;
import jets.projects.api.ClientAPI;

public class OnlineNormalUserInfo {
    private final ClientAPI impl;
    private Timestamp lastRefreshed;

    public OnlineNormalUserInfo(ClientAPI impl) {
        this.impl = impl;
        lastRefreshed = Timestamp.from(Instant.now());
    }

    public ClientAPI getImpl() {
        return impl;
    }

    public Timestamp getLastRefreshed() {
        return lastRefreshed;
    }
    
    public void setLastRefreshed() {
        lastRefreshed = Timestamp.from(Instant.now());
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(OnlineNormalUserInfo.class.getName());
        builder.append('{');
        
        builder.append("impl=");
        builder.append(impl);
        
        builder.append(", lastRefreshed=");
        builder.append(lastRefreshed);
        
        builder.append('}');
        return builder.toString();
    }
}
