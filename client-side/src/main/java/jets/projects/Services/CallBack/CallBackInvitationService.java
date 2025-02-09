package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;

public class CallBackInvitationService {

    DataCenter dataCenter = DataCenter.getInstance();
    public void contactInvitationReceived(ContactInvitationInfo invitationInfo) {
        Platform.runLater(()->{
            dataCenter.getContactInvitationList().add(invitationInfo);
        });

    }

    public void contactInvitationAccepted(ContactInfo newContactInfo){
        Platform.runLater(()->{
            dataCenter.getContactList().add(newContactInfo);
        });
    }

    public void contactInvitationRejected(int invitationID){
        //need to implement
    }
}
