package jets.projects.topcontrollers;

import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Announcement;
import jets.projects.classes.ClientToken;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.classes.ClientSessionData;
import jets.projects.entities.GroupMember;
import jets.projects.entities.NormalUserStatus;

public class DaoClientManager {

    public RequestResult<ClientSessionData> login(String phoneNumber, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> logout() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> registerNormalUser() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<String> getProfilePic(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<Boolean> setOnlineStatus(ClientToken token, NormalUserStatus newStatus) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<Contact>> getContacts(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<Contact> getContactProfile(ClientToken token, String contactPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<String> getContactProfilePic(ClientToken token, String contactPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<ContactMessage>> getContactMessages(ClientToken token, String otherPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(ClientToken token, String otherPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<Boolean> sendContactMessage(ClientToken token, ContactMessage message) {
        // validatorDao.checkClientToken(token) == false return.
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<Boolean> sendContactFileMessage(ClientToken token, String file) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token, List<ContactMessage> messages) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<Group>> getGroups(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<String> getGroupPic(ClientToken token, int groupID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
        public RequestResult<Boolean> createGroup(ClientToken token, Group newGroup) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<GroupMember>> getGroupMembers(ClientToken token, int groupID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> addMemberToGroup(ClientToken token, int groupID, String contactPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token, int groupID, String contactPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token, int groupID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token, int groupID, String newAdminPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> assignGroupLeadership(ClientToken token, int groupID, String newAdminPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> deleteGroup(ClientToken token, int groupID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<Announcement>> getAnnouncements() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<ContactInvitation>> getContactInvitations(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<List<Notification>> getNotifications(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RequestResult<Boolean> saveProfileChanges(ClientToken token) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
