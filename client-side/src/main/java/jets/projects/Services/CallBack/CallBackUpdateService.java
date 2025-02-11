package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import jets.projects.entities.Group;
import jets.projects.entity_info.ContactInfo;

public class CallBackUpdateService {
    DataCenter dataCenter = DataCenter.getInstance();

    public void contactUpdateInfo(int contactID, String newDisplayName, byte[] newPic){
        ContactInfo contactInfo = dataCenter.getContactList().get(contactID);
        Platform.runLater(()->{
            contactInfo.setPic(newPic);
            contactInfo.setName(newDisplayName);
        });
    }

    public void groupPicChanged(int groupID, byte[] newPic){
        Group group = dataCenter.getGroupList().get(groupID);
        Platform.runLater(()->{
            group.setPic(newPic);
        });
    }
}
