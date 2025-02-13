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
        System.out.println("7 "+message);
        int myID = dataCenter.getMyProfile().getUserID();
        if(message.getReceiverID() != myID){
            Platform.runLater(()->{
                ClientAlerts.invokeErrorAlert("From Server", "Message received with wrong id 1 to 1" + message);
            });
            return;
        }
        int senderID = message.getSenderID();

        Map<Integer, ObservableList<ContactMessage>> contactMessagesMap = dataCenter.getContactMessagesMap();

        ObservableList<ContactMessage> contactMessages = contactMessagesMap.get(
                senderID);
        IntegerProperty n = DataCenter.getInstance().getUnreadContactMessages().get(message.getSenderID());
        n.set(n.getValue()+1);
        String name = dataCenter.getContactInfoMap().get(message.getSenderID()).getName();
        Platform.runLater(()->{

            PopUpNotification.showNotification(name +" has sent you a message");
            DataCenter.getInstance().getContactMessagesMap().get(message.getSenderID()).add(message);
        });
    }
}
