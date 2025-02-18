package jets.projects.Services.CallBack;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jets.projects.Controllers.ClientAlerts;
import jets.projects.Controllers.HomeScreenController;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entity_info.GroupMemberInfo;

import java.rmi.RemoteException;
import java.util.ArrayList;


public class CallBackGroupService {
    DataCenter dataCenter = DataCenter.getInstance();
    public static HomeScreenController homeScreenController;

    public void addedToGroup(Group group) {
        System.out.println("added to group");

        Platform.runLater(() -> {
            try{
                dataCenter.getGroupMessagesMap().put(
                        group.getGroupID(), FXCollections.synchronizedObservableList(FXCollections.observableArrayList(new ArrayList<>())));
                dataCenter.getGroupMessagesMap().get(group.getGroupID()).addAll(
                        ServerConnectivityService.getServerAPI().getGroupMessages(
                                ServerConnectivityService.getMyToken(), group.getGroupID()));
                dataCenter.getGroupMembersMap().put(
                  group.getGroupID(), FXCollections.synchronizedObservableList(FXCollections.observableArrayList(new ArrayList<>()))
                );
                dataCenter.getGroupMembersMap().get(group.getGroupID()).addAll(
                        ServerConnectivityService.getServerAPI().getGroupMembers(ServerConnectivityService.getMyToken(), group.getGroupID())
                );
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
            dataCenter.getGroupInfoMap().put(group.getGroupID(), group);
            dataCenter.getGroupList().add(group);
        });
    }

    public void removedFromGroup(int groupID) {
        Platform.runLater(() -> {
            dataCenter.getGroupList().removeIf(group -> group.getGroupID() == groupID);
            //dataCenter.getGroupMessagesMap().remove(groupID);
            homeScreenController.hideChatBox();
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
            homeScreenController.hideChatBox();
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

    public void groupMemberLeft(int groupID, int memberID) {
        Platform.runLater(()->{
            dataCenter.getGroupMembersMap().get(groupID).removeIf(groupMemberInfo->
                    groupMemberInfo.getMember().getMemberID() == memberID);
        });
    }
}