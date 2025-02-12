package jets.projects.dao;

import java.util.Arrays;
import java.util.List;
import jets.projects.entities.NormalUser;
import jets.projects.test_utils.DBInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class UsersQueryDaoTests {
    private static final UsersQueryDao usersQueryDao
            = new UsersQueryDao();
    
    @BeforeAll
    public static void init() {
        boolean isInit = DBInitializer.init();
        if (!isInit) {
            fail();
        }
    }

    @Test
    public void isNormalUserExistsByID() {
        List<Integer> IDs = Arrays.asList(
                1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        for (int ID : IDs) {
            var result = usersQueryDao.isNormalUserExistsByID(ID);
            if (result.getErrorMessage() != null) {
                fail(result.getErrorMessage());
            }
            boolean isExists = result.getResponseData();
            assertEquals(true, isExists);
        }
        
        IDs = Arrays.asList(0, -1);
        for (int ID : IDs) {
            var result = usersQueryDao.isNormalUserExistsByID(ID);
            if (result.getErrorMessage() != null) {
                fail(result.getErrorMessage());
            }
            boolean isExists = result.getResponseData();
            assertEquals(false, isExists);
        }
    }
    
    @Test
    public void isNormalUserExistsByPhoneNumber() {
        List<String> phoneNumbers = Arrays.asList(
                "1111", "2222", "3333", "4444", "5555",
                "6666", "7777", "8888", "9999");
        for (String phoneNumber : phoneNumbers) {
            var result = usersQueryDao.isNormalUserExistsByPhoneNumber(
                    phoneNumber);
            if (result.getErrorMessage() != null) {
                fail(result.getErrorMessage());
            }
            int ID = result.getResponseData();
            assertNotEquals(-1, ID);
        }
        
        phoneNumbers = Arrays.asList(
                "", "22", "1234");
        for (String phoneNumber : phoneNumbers) {
            var result = usersQueryDao.isNormalUserExistsByPhoneNumber(
                    phoneNumber);
            if (result.getErrorMessage() != null) {
                fail(result.getErrorMessage());
            }
            Integer ID = result.getResponseData();
            assertNull(ID);
        }       
    }
    
    @Test
    public void getNormalUserDisplayNameByID() {
        List<Integer> IDs = Arrays.asList(
                1, 2, 3, 4, 5, 6, 7, 8, 9);
        
        for (int ID : IDs) {
            var result = usersQueryDao.getNormalUserDisplayNameByID(ID);
            if (result.getErrorMessage() != null) {
                fail(result.getErrorMessage());
            }
            String displayName = result.getResponseData();
            assertNotNull(displayName);
        }
        
        IDs = Arrays.asList(0, -1);
        for (int ID : IDs) {
            var result = usersQueryDao.getNormalUserDisplayNameByID(ID);
            if (result.getErrorMessage() != null) {
                fail(result.getErrorMessage());
            }
            String displayName = result.getResponseData();
            assertNull(displayName);
        }
    }
    
    @Test
    public void getNormalUserByPhoneNumber() {
        String phoneNumber = "1111";
        
        var result = usersQueryDao.getNormalUserByPhoneNumber(
                phoneNumber);
        if (result.getErrorMessage() != null) {
            fail(result.getErrorMessage());
        }
        NormalUser user = result.getResponseData();
        assertEquals("Mounir", user.getDisplayName());
    }
}
