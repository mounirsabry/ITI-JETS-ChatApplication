package jets.projects.classes;

public class ClientSessionData {
    private final int userID;
    private final String phoneNumber;
    private String userDisplayName;

    public ClientSessionData(int userID, String phoneNumber, String userDisplayName) {
        this.userID = userID;
        this.phoneNumber = phoneNumber;
        this.userDisplayName = userDisplayName;
    }

    public int getUserID() {
        return userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }
    
    public void setUserDisplayName(String newName) {
        userDisplayName = newName;
    }
}
