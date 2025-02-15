package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import jets.projects.Controllers.HomeScreenController;
import jets.projects.entities.Group;
import jets.projects.entity_info.ContactInfo;

public class CallBackUpdateService {
    DataCenter dataCenter = DataCenter.getInstance();
    public static HomeScreenController homeScreenController;


    public void contactUpdateInfo(int contactID, String newDisplayName, byte[] newPic){
        Platform.runLater(()->{
            ContactInfo contactInfo = dataCenter.getContactList().stream()
                    .filter((contact)->contactID == contact.getContact().getSecondID())
                            .findAny().get();
            contactInfo.setName(newDisplayName);
            contactInfo.setPic(newPic);
            dataCenter.getContactList().removeIf(c->
               c.getContact().getSecondID() == contactID
            );
            dataCenter.getContactList().add(contactInfo);
            homeScreenController.updateContactProfile();

        });
    }

    public void groupPicChanged(int groupID, byte[] newPic){
        Group group = dataCenter.getGroupList().get(groupID);
        Platform.runLater(()->{
            group.setPic(newPic);
        });
    }
}
