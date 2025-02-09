package jets.projects.shared_ds;

import java.time.LocalDateTime;
import jets.projects.api.ClientAPI;

public class OnlineNormalUserInfo {
    private final ClientAPI impl;
    private LocalDateTime lastRefreshed;

    public OnlineNormalUserInfo(ClientAPI impl) {
        this.impl = impl;
        lastRefreshed = LocalDateTime.now();
    }

    public ClientAPI getImpl() {
        return impl;
    }

    public LocalDateTime getLastRefreshed() {
        return lastRefreshed;
    }
    
    public void setLastRefreshed() {
        lastRefreshed = LocalDateTime.now();
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
