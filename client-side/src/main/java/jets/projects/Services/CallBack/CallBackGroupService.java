package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entity_info.GroupMemberInfo;

import java.rmi.RemoteException;


public class CallBackGroupService {
    DataCenter dataCenter = DataCenter.getInstance();

    public void addedToGroup(Group group) {
        Platform.runLater(() -> {
            dataCenter.getGroupList().add(group);

        });
        try{
            dataCenter.getGroupMessagesMap().put(
                    group.getGroupID(), FXCollections.synchronizedObservableList(FXCollections.observableArrayList()));
            dataCenter.getGroupMembersMap().get(group.getGroupID()).addAll(
                    ServerConnectivityService.getServerAPI().getGroupMembers(ServerConnectivityService.getMyToken(), group.getGroupID())
            );
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
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
            PopUpNotification.showNotification("you are the new Admin of group "+group.getGroupName());
        });

    }

    public void groupDeleted(int groupID){
        Platform.runLater(()->{
            dataCenter.getGroupList().removeIf(group -> group.getGroupID() == groupID);
        });
    }

    public void groupMessageReceived(GroupMessage message){
        ObservableList list = dataCenter.getGroupMessagesMap().get(message.getGroupID());
        String group = dataCenter.getGroupInfoMap().get(message.getGroupID()).getGroupName();

        GroupMemberInfo groupMemberInfo = dataCenter.getGroupMembersMap().get(message.getGroupID()).stream()
                .filter(c->c.getMember().getMemberID() == message.getSenderID()).findAny().get();
        String name = groupMemberInfo.getName();
        Platform.runLater(()->{
            PopUpNotification.showNotification(name+" has sent a message to group "+group);
            list.add(message);
        });
    }

    public void newGroupMemberAdded(GroupMemberInfo newMember){
        Platform.runLater(()->{
            dataCenter.getGroupMembersMap().get(newMember.getMember().getGroupID()).add(newMember);
        });
    }
    public void groupMemberRemoved(int groupID, int memberID){
        Platform.runLater(()->{
            dataCenter.getGroupMembersMap().get(groupID).removeIf(groupMemberInfo->
               groupMemberInfo.getMember().getMemberID() == memberID);
        });
    }
    public void adminChanged(int groupID, int newAdminID){
        Platform.runLater(()->{
            dataCenter.getGroupList().stream().filter(group -> group.getGroupID() == groupID).findFirst().get().setGroupAdminID(newAdminID);
        });
    }
}