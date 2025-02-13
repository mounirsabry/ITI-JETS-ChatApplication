package jets.projects.dao;

import java.util.List;
import jets.projects.test_utils.DBInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

public class ContactDaoTests {
    private static final ContactDao contactDao
            = new ContactDao();
    
    @BeforeAll
    public static void init() {
        boolean isInit = DBInitializer.init();
        if (!isInit) {
            fail();
        }
    }
    
    @Test
    public void getAllContactsIDs() {
        int userID = 2;
        var result = contactDao.getAllContactsIDs(userID);
        if (result.getErrorMessage() != null) {
            fail(result.getErrorMessage());
        }
        List<Integer> IDs = result.getResponseData();
        assertEquals(1, IDs.size());
        assertEquals(1, IDs.get(0));
    }
}
