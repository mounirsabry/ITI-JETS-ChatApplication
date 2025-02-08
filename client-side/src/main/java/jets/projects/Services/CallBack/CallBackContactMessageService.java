package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jets.projects.Controllers.ClientAlerts;
import jets.projects.entities.ContactMessage;

import java.util.Map;

public class CallBackContactMessageService {

    DataCenter dataCenter = DataCenter.getInstance();

    public void contactMessageReceived(ContactMessage message){

        int myID = dataCenter.getMyProfile().getUserID();
        int senderID = -1;
        if(message.getSenderID() != myID && message.getReceiverID() == myID){
            senderID = message.getSenderID();
        }else if (message.getSenderID() == myID && message.getReceiverID() != myID) {
            senderID = message.getReceiverID();
        }else{
            Platform.runLater(()->{
                ClientAlerts.invokeErrorAlert(
                        "Error", "Message sent from server with wrong ids:::" + message.toString());
            });
            return;
        }

        Map<Integer, ObservableList<ContactMessage>> contactMessagesMap = dataCenter.getContactMessagesMap();

        ObservableList<ContactMessage> contactMessages = contactMessagesMap.getOrDefault(
                senderID, FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

        Platform.runLater(()->{
            contactMessages.add(message);
        });
    }
}
