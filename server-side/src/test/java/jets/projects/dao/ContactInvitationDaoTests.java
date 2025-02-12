package jets.projects.dao;

import java.util.List;
import jets.projects.entities.Country;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.session.ClientSessionData;
import jets.projects.test_utils.DBInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

public class ContactInvitationDaoTests {
    private static final ContactInvitationDao conatctInvitationDao
            = new ContactInvitationDao();
    
    @BeforeAll
    public static void init() {
        boolean isInit = DBInitializer.init();
        if (!isInit) {
            fail();
        }
    }
    
    @Test
    public void getAllInvitationsInfo() {
        int userID = 4;
        var result = conatctInvitationDao.getAllInvitationsInfo(userID);
        if (result.getErrorMessage() != null) {
            fail(result.getErrorMessage());
        }
        List<ContactInvitationInfo> list = result.getResponseData();
        assertEquals(2, list.size());
    }
}
