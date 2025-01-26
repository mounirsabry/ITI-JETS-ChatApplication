package jets.projects.classes;

public class ClientToken {
    private final String phoneNumber;
    private final int userID;

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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(ClientToken.class.getName());
        builder.append('{');
        
        builder.append("PhoneNumber=");
        builder.append(phoneNumber);
        
        builder.append(", UserID=");
        builder.append(userID);
        
        builder.append('}');
        return builder.toString();
    }
}
