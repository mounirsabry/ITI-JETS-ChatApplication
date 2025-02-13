package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import jets.projects.entities.Group;
import jets.projects.entity_info.ContactInfo;

public class CallBackUpdateService {
    DataCenter dataCenter = DataCenter.getInstance();

    public void contactUpdateInfo(int contactID, String newDisplayName, byte[] newPic){

        Platform.runLater(()->{
            dataCenter.getContactList().stream()
                    .filter((contact)->contactID == contact.getContact().getSecondID())
                    .peek((contactInfo)->{
                        contactInfo.setName(newDisplayName);
                        contactInfo.setPic(newPic);
                    });

        });
    }

    public void groupPicChanged(int groupID, byte[] newPic){
        Group group = dataCenter.getGroupList().get(groupID);
        Platform.runLater(()->{
            group.setPic(newPic);
        });
    }
}
