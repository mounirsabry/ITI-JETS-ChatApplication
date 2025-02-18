package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jets.projects.Controllers.ClientAlerts;
import jets.projects.Controllers.HomeScreenController;
import jets.projects.entities.ContactMessage;

import java.util.Map;

public class CallBackContactMessageService {

    DataCenter dataCenter = DataCenter.getInstance();

    public void contactMessageReceived(ContactMessage message){
        int myID = dataCenter.getMyProfile().getUserID();
        if(message.getReceiverID() != myID){
            Platform.runLater(()->{
                ClientAlerts.invokeErrorAlert("From Server", "Message received with wrong id 1 to 1" + message);
            });
            return;
        }

        String name = dataCenter.getContactInfoMap().get(message.getSenderID()).getName();
        Platform.runLater(()->{
            System.out.println(message);
            PopUpNotification.showNotification(name +" has sent you a message");
            dataCenter.getContactMessagesMap().get(message.getSenderID()).add(message);
        });
    }
}
