package jets.projects.entities;

import java.io.Serializable;

public class AdminUser implements Serializable {
    private int userID;
    private String displayName;
    private String password;

    public AdminUser() {
    }

    public AdminUser(int userID, String displayName, String password) {
        this.userID = userID;
        this.displayName = displayName;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append("AdminUser");   
        builder.append('{');
        
        builder.append("userID=");
        builder.append(userID);
        
        builder.append(", displayName=");
        builder.append(displayName);
        
        builder.append(", password=");
        builder.append(password);

        builder.append('}');
        return builder.toString();
    }
    
    
}
