package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;

import java.util.ArrayList;

public class CallBackInvitationService {

    DataCenter dataCenter = DataCenter.getInstance();
    public void contactInvitationReceived(ContactInvitationInfo invitationInfo) {
        Platform.runLater(()->{
            dataCenter.getContactInvitationList().add(invitationInfo);
        });

    }

    public void contactInvitationAccepted(ContactInfo newContactInfo){
        String name = newContactInfo.getName();
        Platform.runLater(()->{
            PopUpNotification.showNotification(name+" has accept your friend request");
            dataCenter.getContactList().add(newContactInfo);
            dataCenter.getContactInfoMap().put(newContactInfo.getContact().getSecondID(), newContactInfo);
            dataCenter.getContactMessagesMap().put(newContactInfo.getContact().getSecondID(),
                    FXCollections.synchronizedObservableList(FXCollections.observableArrayList(new ArrayList<>())));
        });

    }

    public void contactInvitationRejected(int invitationID){
        //need to implement
    }
}
