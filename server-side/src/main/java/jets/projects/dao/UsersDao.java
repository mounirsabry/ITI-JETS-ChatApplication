package jets.projects.dao;

import java.util.Date;
import jets.projects.classes.RequestResult;
import jets.projects.session.ClientSessionData;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class UsersDao {

    public RequestResult<Boolean> isNormalUserExists(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public NormalUser getUserById(int userID){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public RequestResult<ClientSessionData> clientLogin(String phoneNumber, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public RequestResult<Boolean> clientLogout(int userID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public RequestResult<Boolean> registerNormalUser(String displayName, String phoneNumber,
            String email, String pic, String password, Gender gender,
            String country, Date birthDate, String bio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> setOnlineStatus(int userID, NormalUserStatus newStatus) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<String> getNormalUserProfilePic(int userID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<NormalUser> getNormalUserProfile(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNormalUserProfile'");
    }

    public RequestResult<Boolean> saveProfileChanges(int userID , String username , String bio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveProfileChanges'");
    }

    public RequestResult<Boolean> isPasswordValid(int userID, String oldPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPasswordValid'");
    }

    public RequestResult<Boolean> updatePassword(int userID, String oldPassword, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
    }
    
}
