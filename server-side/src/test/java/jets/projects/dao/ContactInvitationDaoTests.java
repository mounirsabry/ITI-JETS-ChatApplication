package jets.projects.dao;

import java.util.List;
import jets.projects.entities.ContactGroup;
import jets.projects.entities.ContactInvitation;
import jets.projects.entity_info.ContactInvitationInfo;

import jets.projects.test_utils.DBInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

public class ContactInvitationDaoTests {
    private static final ContactInvitationDao contactInvitationDao
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
        var result = contactInvitationDao.getAllInvitationsInfo(userID);
        if (result.getErrorMessage() != null) {
            fail(result.getErrorMessage());
        }
        List<ContactInvitationInfo> list = result.getResponseData();
        assertEquals(2, list.size());
    }
    
    @Test
    public void getContactInvitation() {
        int invitationID = 1;
        var result = contactInvitationDao.getContactInvitation(
                invitationID);
        if (result.getErrorMessage() != null) {
            fail(result.getErrorMessage());
        }
        ContactInvitation invitation = result.getResponseData();
        assertEquals(1, invitation.getInvitationID());
        assertEquals(1, invitation.getSenderID());
        assertEquals(3, invitation.getReceiverID());
    }
}
