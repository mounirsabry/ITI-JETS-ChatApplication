package jets.projects.classes;

public class ExceptionMessages {
    public static final String USER_MUST_CHANGE_PASSWORD_FOR_FIRST_LOGIN
            = "The user is admin created and this is the first login, use "
            + "the other login function to change password.";
    
    public static final String USER_TIMEOUT 
            = "Your session has expired , please login again.";
    
    public static final String INVALID_TOKEN_FORMAT
            = "Invalid token format.";
    
    public static final String INVALID_TOKEN 
            = "Invalid token, token does not map to a registered user.";
    
    public static final String ALREADY_LOGGED_IN
            = "You are already logged in,"
            + " please logout of the other device first.";
    
    public static final String INVALID_INPUT_DATA 
            = "Invalid input data.";
    
    public static final String FILE_TOO_BIG
            = "File size is over the allowed size.";
    
    public static final String FILE_TYPE_NOT_SUPPORTED
            = "File type is not supported.";
        
    public static final String USER_DOES_NOT_EXIST 
            = "User does not exist";
    
    public static final String NOT_CONTACTS
            = "User does not belong to contacts list.";
    
    public static final String ALREADY_CONTACTS
            = "Can't send invitation. User already in contacts list.";
    
    public static final String GROUP_DOES_NOT_EXIST
            = "Group does not exist";
    
    public static final String NOT_MEMBER
            = "You are not a member of this group.";
    
    public static final String ALREADY_MEMBER
            = "The user is already a member in the group.";
    
    public static final String NOT_GROUP_ADMIN
            = "You have no access on this action. Not the group admin.";
    
    public static final String NOT_THE_LAST_MEMER
            = "You are not the last member of the group to use this parameter.";
    
    // Sent from the client.
    public static final String NULL_DATA_SENT_FORM_SERVER
            = "Null data sent form Server.";
}
