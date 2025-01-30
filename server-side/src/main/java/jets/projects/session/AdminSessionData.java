package jets.projects.session;

public class AdminSessionData {
    private final int userID;
    private final String displayName;
    
    public AdminSessionData(int userID, String displayName) {
        this.userID = userID;
        this.displayName = displayName;
    }

    public int getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(AdminSessionData.class.getName());
        builder.append('{');
        
        builder.append("userID=");
        builder.append(userID);
        
        builder.append(", displayName=");
        builder.append(displayName);
        
        builder.append('}');
        return builder.toString();
    }
}
