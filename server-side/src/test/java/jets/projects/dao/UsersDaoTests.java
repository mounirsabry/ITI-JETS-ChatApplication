package jets.projects.dao;

import jets.projects.entities.Country;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.session.ClientSessionData;
import jets.projects.test_utils.DBInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class UsersDaoTests {
    private static final UsersDao usersDao
            = new UsersDao();
    
    @BeforeAll
    public static void init() {
        boolean isInit = DBInitializer.init();
        if (!isInit) {
            fail();
        }
    }
    
    @Test
    public void clientLoginAndLogout() {
        String phoneNumber = "1111";
        String password = "mounir";
        
        var result1 = usersDao.clientLogin(phoneNumber, password);
        if (result1.getErrorMessage() != null) {
            fail(result1.getErrorMessage());
        }
        ClientSessionData session = result1.getResponseData();
        assertEquals(1, session.getUserID());
        assertEquals("1111", session.getPhoneNumber());
        assertEquals("Mounir", session.getDisplayName());
        
        var result2 = usersDao.clientLogout(session.getUserID());
        if (result2.getErrorMessage() != null) {
            fail(result2.getErrorMessage());
        }
        boolean isLogout = result2.getResponseData();
        assertTrue(isLogout);
    }
    
    @Test
    public void registerViewEditView() {
        NormalUser user = new NormalUser();
        /*
        private String displayName;
        private String phoneNumber;
        private String email;
        private byte[] pic;
        private String password;
        private Gender gender;
        private Country country;
        private Date birthDate;
        private LocalDateTime createdAt;
        private NormalUserStatus status;
        private String bio;
        private boolean isAdminCreated;
        private boolean isPasswordValid;
        */
        
        user.setDisplayName("Test1");
        user.setPhoneNumber("11223345");
        user.setEmail("test1@email.com");
        user.setPassword("testtest");
        user.setGender(Gender.FEMALE);
        user.setCountry(Country.SPAIN);
        user.setBio("");
        int userID = 10;
        
        var result1 = usersDao.registerNormalUser(user);
        if (result1.getErrorMessage() != null) {
            fail(result1.getErrorMessage());
        }
        boolean isCreated = result1.getResponseData();
        assertTrue(isCreated);
        
        var result2 = usersDao.getNormalUserProfile(
                userID);
        if (result2.getErrorMessage() != null) {
            fail(result2.getErrorMessage());
        }
        NormalUser createdUser = result2.getResponseData();
        assertEquals(userID, createdUser.getUserID());
        assertEquals("Test1", createdUser.getDisplayName());
        assertEquals(
                NormalUserStatus.OFFLINE, createdUser.getStatus());
        
        var result3 = usersDao.editProfile(
                userID, "New Test",
                null, "Some bio", null);
        if (result3.getErrorMessage() != null) {
            fail(result3.getErrorMessage());
        }
        boolean isEdited = result3.getResponseData();
        assertTrue(isEdited);
    }
    
    @Test
    public void getPicValidatePasswordUpdatePassword() {
        int userID = 1;
        String oldPassword = "mounir";
        String newPassword = "newPassword";
        
        var result1 = usersDao.getNormalUserProfilePic(userID);
        if (result1.getErrorMessage() != null) {
            fail(result1.getErrorMessage());
        }
        byte[] pic = result1.getResponseData();
        assertNull(pic);
        
        var result2 = usersDao.isPasswordValid(userID, oldPassword);
        if (result2.getErrorMessage() != null) {
            fail(result1.getErrorMessage());
        }
        boolean isValid = result2.getResponseData();
        assertTrue(isValid);
        
        var result3 = usersDao.updatePassword(userID, oldPassword, newPassword);
        if (result3.getErrorMessage() != null) {
            fail(result3.getErrorMessage());
        }
        boolean isChanged = result3.getResponseData();
        assertTrue(isChanged);
    }
}
