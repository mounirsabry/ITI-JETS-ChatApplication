package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;


public class CallBackGroupService {
    DataCenter dataCenter = DataCenter.getInstance();

    public void addedToGroup(Group group) {
        Platform.runLater(() -> {
            dataCenter.getGroupList().add(group);
            dataCenter.getGroupMessagesMap().put(
                    group.getGroupID(), FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));
        });
    }

    public void removedFromGroup(int groupID) {
        Platform.runLater(() -> {
            dataCenter.getGroupList().removeIf(group -> group.getGroupID() == groupID);
            dataCenter.getGroupMessagesMap().remove(groupID);
        });
    }

    public void leadershipGained(int groupID){
        Group group = dataCenter.getGroupList().stream().filter(g->g.getGroupID() == groupID).findFirst().get();
        Platform.runLater(()->{
            group.setGroupAdminID(dataCenter.getMyProfile().getUserID());
        });

    }

    public void groupDeleted(int groupID){
        Platform.runLater(()->{
            dataCenter.getGroupList().removeIf(group -> group.getGroupID() == groupID);
        });
    }

    public void groupMessageReceived(GroupMessage message){
        ObservableList list = dataCenter.getGroupMessagesMap().get(message.getGroupID());
        Platform.runLater(()->{
            list.add(message);
        });
    }
}