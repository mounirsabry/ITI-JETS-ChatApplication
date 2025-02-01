package jets.projects.dao;

import java.util.List;

import jets.projects.classes.RequestResult;
import jets.projects.entities.ContactInvitation;

public class ContactInvitationDao {

    public RequestResult<List<ContactInvitation>> getAllInvitations(int userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInvitations'");
    }

    public RequestResult<Boolean> sendContactInvitation(ContactInvitation invitation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendContactInvitation'");
    }

    public RequestResult<Boolean> acceptContactInvitation(ContactInvitation invitation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptContactInvitation'");
    }
    public RequestResult<Boolean> rejectContactInvitation(ContactInvitation invitation) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptContactInvitation'");
    }
}
