package jets.projects.classes;

public class ClientToken {
    private String phoneNumber;
    private int userID;

    public ClientToken(String phoneNumber, int userID) {
        this.phoneNumber = phoneNumber;
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getUserID() {
        return userID;
    }
}
