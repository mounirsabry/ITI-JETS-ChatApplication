package jets.projects.dao;

import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entities.ContactMessage;

public class ContactMessagesDao {
    
    public RequestResult<List<ContactMessage>> getContactMessages(int senderID , int receiverID ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactMessages'");
    }

    public RequestResult<List<ContactMessage>> getUnReadContactMessages(int senderID , int receiverID ) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getContactMessages'");
    }

    public RequestResult<Boolean> sendContactMessage(ContactMessage message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendContactMessage'");
    }

    public RequestResult<Boolean> markContactMessagesAsRead(List<ContactMessage> messages) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markContactMessagesAsRead'");
    }

    public RequestResult<Boolean> sendContactFileMessage(int senderID, String file, int receiverID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendContactFileMessage'");
    }
}
