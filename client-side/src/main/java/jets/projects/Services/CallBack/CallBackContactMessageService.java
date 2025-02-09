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
        if(message.getReceiverID() != myID){
            Platform.runLater(()->{
                ClientAlerts.invokeErrorAlert("From Server", "Message received with wrong id 1 to 1" + message);
            });
            return;
        }
        int senderID = message.getSenderID();

        Map<Integer, ObservableList<ContactMessage>> contactMessagesMap = dataCenter.getContactMessagesMap();

        ObservableList<ContactMessage> contactMessages = contactMessagesMap.getOrDefault(
                senderID, FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));

        Platform.runLater(()->{
            contactMessages.add(message);
        });
    }
}
