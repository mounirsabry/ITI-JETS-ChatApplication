package jets.projects.classes;

public class ClientSessionData {
    private final int userID;
    private final String phoneNumber;
    private String displayName;

    public ClientSessionData(int userID, String phoneNumber, String displayName) {
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
    }

    public int getUserID() {
        return userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String newName) {
        displayName = newName;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(ClientSessionData.class.getName());
        builder.append('{');
        
        builder.append("userID=");
        builder.append(userID);
        
        builder.append(", phoneNumber=");
        builder.append(phoneNumber);
        
        builder.append(", displayName=");
        builder.append(displayName);
        
        builder.append('}');
        return builder.toString();
    }
}
