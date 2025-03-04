package jets.projects.session;

import java.io.Serializable;

public class AdminToken implements Serializable {
    private final int userID;

    public AdminToken(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(AdminToken.class.getName());
        builder.append('{');
        
        builder.append("UserID=");
        builder.append(userID);
        
        builder.append('}');
        return builder.toString();
    }
}
