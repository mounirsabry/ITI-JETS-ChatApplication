package jets.projects.dao;

import jets.projects.entities.Group;
import jets.projects.test_utils.DBInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

public class GroupDaoTests {
    private static final GroupDao groupDao
            = new GroupDao();
    
    public GroupDaoTests() {
    }
    
    @BeforeAll
    public static void init() {
        boolean isInit = DBInitializer.init();
        if (!isInit) {
            fail();
        }
    }

    @Test
    public void createGroup() {
        Group newGroup = new Group();
        newGroup.setGroupName("Some name");
        newGroup.setGroupDesc("Some desc");
        newGroup.setGroupAdminID(2);
        var result = groupDao.createGroup(newGroup);
        if (result.getErrorMessage() != null) {
            fail(result.getErrorMessage());
        }
        int ID = result.getResponseData();
        assertEquals(2, ID);
    }
}
