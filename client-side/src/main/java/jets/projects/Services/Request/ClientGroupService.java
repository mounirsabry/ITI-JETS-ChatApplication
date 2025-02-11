package jets.projects.Services.Request;

import jets.projects.Controllers.ClientAlerts;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.api.NormalUserAPI;
import jets.projects.entities.Group;
import jets.projects.entity_info.GroupMemberInfo;
import jets.projects.session.ClientToken;

import java.rmi.RemoteException;
import java.util.List;

public class ClientGroupService{
    
    public List<Group> getGroups() {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Get Groups", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            return serverAPI.getGroups(myToken);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Groups Error", e.getMessage());
            return null;
        }
    }

    public boolean setGroupPic(int groupID, byte[] pic) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Set Group Picture", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.setGroupPic(myToken, groupID, pic);
            if (success) {
                ClientAlerts.invokeInformationAlert("Set Group Picture", "Group picture updated successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Set Group Picture", "Failed to update group picture");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Set Group Picture Error", e.getMessage());
            return false;
        }
    }

    public boolean createGroup(Group newGroup) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Create Group", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.createGroup(myToken, newGroup);
            if (success) {
                ClientAlerts.invokeInformationAlert("Create Group", "Group created successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Create Group", "Failed to create group");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Create Group Error", e.getMessage());
            return false;
        }
    }
    
    public List<GroupMemberInfo> getGroupMembers(int groupID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Get Group Members", "Can't connect to server");
            return null;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            return serverAPI.getGroupMembers(myToken, groupID);
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Get Group Members Error", e.getMessage());
            return null;
        }
    }

    public boolean addMemberToGroup(int groupID, int contactID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Add Member To Group", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.addMemberToGroup(myToken, groupID, contactID);
            if (success) {
                ClientAlerts.invokeInformationAlert("Add Member To Group", "Member added successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Add Member To Group", "Failed to add member to group");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Add Member To Group Error", e.getMessage());
            return false;
        }
    }

    public boolean removeMemberFromGroup(int groupID, int contactID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Remove Member From Group", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.removeMemberFromGroup(myToken, groupID, contactID);
            if (success) {
                ClientAlerts.invokeInformationAlert("Remove Member From Group", "Member removed successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Remove Member From Group", "Failed to remove member from group");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Remove Member From Group Error", e.getMessage());
            return false;
        }
    }

    public boolean leaveGroupAsMember(int groupID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Leave Group As Member", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.leaveGroupAsMember(myToken, groupID);
            if (success) {
                ClientAlerts.invokeInformationAlert("Leave Group As Member", "You have left the group successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Leave Group As Member", "Failed to leave group");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Leave Group As Member Error", e.getMessage());
            return false;
        }
        
    }

    public boolean leaveGroupAsAdmin(int groupID, int newAdminID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Leave Group As Admin", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.leaveGroupAsAdmin(myToken, groupID, newAdminID);
            if (success) {
                ClientAlerts.invokeInformationAlert("Leave Group As Admin", "You have left the group as admin successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Leave Group As Admin", "Failed to leave the group as admin");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Leave Group As Admin Error", e.getMessage());
            return false;
        }
    }

    public boolean assignGroupLeadership(int groupID, int newAdminID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Assign Group Leadership", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.assignGroupLeadership(myToken, groupID, newAdminID);
            if (success) {
                ClientAlerts.invokeInformationAlert("Assign Group Leadership", "Group leadership assigned successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Assign Group Leadership", "Failed to assign group leadership");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Assign Group Leadership Error", e.getMessage());
            return false;
        }
    }

    public boolean deleteGroup(int groupID) {
        if (!ServerConnectivityService.check()) {
            ClientAlerts.invokeWarningAlert("Delete Group", "Can't connect to server");
            return false;
        }
        NormalUserAPI serverAPI = ServerConnectivityService.getServerAPI();
        ClientToken myToken = ServerConnectivityService.getMyToken();
        try {
            boolean success = serverAPI.deleteGroup(myToken, groupID);
            if (success) {
                ClientAlerts.invokeInformationAlert("Delete Group", "Group deleted successfully");
            } else {
                ClientAlerts.invokeErrorAlert("Delete Group", "Failed to delete group");
            }
            return success;
        } catch (RemoteException e) {
            ClientAlerts.invokeErrorAlert("Delete Group Error", e.getMessage());
            return false;
        }
    }


}
