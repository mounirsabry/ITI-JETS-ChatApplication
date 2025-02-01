package jets.projects.topcontrollers;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jets.projects.classes.ExceptionMessages;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Announcement;
import jets.projects.session.ClientToken;
import jets.projects.sharedds.OnlineNormalUserInfo;
import jets.projects.sharedds.OnlineNormalUserTable;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.onlinelisteners.ContactInvitationCallback;
import jets.projects.onlinelisteners.ContactMessageCallback;
import jets.projects.onlinelisteners.GroupCallback;
import jets.projects.onlinelisteners.GroupMessageCallback;
import jets.projects.onlinelisteners.NotificatonCallback;
import jets.projects.session.ClientSessionData;
import jets.projects.dao.AnnouncementDao;
import jets.projects.dao.ContactDao;
import jets.projects.dao.ContactInvitationDao;
import jets.projects.dao.ContactMessagesDao;
import jets.projects.dao.GroupDao;
import jets.projects.dao.GroupMemberDao;
import jets.projects.dao.GroupMessagesDao;
import jets.projects.dao.NotificationDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.Gender;
import jets.projects.entities.GroupMember;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class NormalUserManager {
    // DAOs
    AnnouncementDao announcementDao = new AnnouncementDao();
    ContactDao contactsDao = new ContactDao();
    ContactInvitationDao contactInvitationDao = new ContactInvitationDao();
    ContactMessagesDao contactMessagesDao = new ContactMessagesDao();
    GroupDao groupDao = new GroupDao();
    GroupMemberDao groupMemberDao = new GroupMemberDao();
    GroupMessagesDao groupMessagesDao = new GroupMessagesDao();
    NotificationDao notificationDao = new NotificationDao();
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    //  listener classes
    ContactInvitationCallback contactInvitationCallback;
    ContactMessageCallback contactMessageCallback;
    GroupMessageCallback groupMessageCallback;
    GroupCallback groupCallback;
    NotificatonCallback notificatonCallback;
    Map<Integer, OnlineNormalUserInfo> onlineUsers;

    public NormalUserManager(){
        this.contactInvitationCallback = new ContactInvitationCallback(contactInvitationDao);
        this.contactMessageCallback = new ContactMessageCallback(contactMessagesDao);
        this.groupMessageCallback = new GroupMessageCallback(groupMessagesDao,groupMemberDao);
        this.groupCallback = new GroupCallback(groupDao,groupMemberDao);
        this.notificatonCallback = new NotificatonCallback(notificationDao , contactsDao);
        onlineUsers = OnlineNormalUserTable.getOnlineUsersTable();
    }
    public RequestResult<ClientSessionData> login(String phoneNumber, String password) throws RemoteException {
        var result = usersDao.clientLogin(phoneNumber, password);  //check database
         if (result.getErrorMessage() != null) {
            throw new RemoteException(result.getErrorMessage());
        }
        notificatonCallback.userWentOnline(result.getResponseData().getUserID());   //callback for contacts
        return result;
    }
    public RequestResult<Boolean> registerNormalUser(
            String displayName, String phoneNumber, String email, String pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) throws RemoteException {
        var result = usersDao.registerNormalUser(
                displayName, phoneNumber, email, pic,
                password, gender, country, birthDate, bio);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> logout(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.clientLogout(token.getUserID()); //update database
        notificatonCallback.userWentOffline(token.getUserID());//callback for contacts
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<String> getProfilePic(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.getNormalUserProfilePic(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if (newStatus==null) {
            return new RequestResult<>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = usersDao.setOnlineStatus(token.getUserID(), newStatus); //update database
        notificatonCallback.userStatusChanged(token.getUserID(),newStatus);//callback for contacts
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<List<Contact>> getContacts(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = contactsDao.getAllContacts(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<NormalUser> getContactProfile(ClientToken token, int contactID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(contactID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), contactID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactsDao.getContactProfile(contactID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }    
    public RequestResult<String> getContactProfilePic(ClientToken token, int contactID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(contactID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), contactID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactsDao.getContactProfilePic(contactID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<ContactMessage>> getContactMessages(ClientToken token, int otherID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.getContactMessages(token.getUserID(),otherID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(ClientToken token, int otherID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(null, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(null, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.getUnReadContactMessages(token.getUserID(), otherID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> sendContactMessage(ClientToken token, ContactMessage message) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if(message.getContent()==null){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(message.getReceiverID()).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), message.getReceiverID()).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.sendContactMessage(message);   //save in database
        contactMessageCallback.contactMessageReceived(message);  //callback for receiver
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }   
    public RequestResult<Boolean> sendContactFileMessage(ClientToken token, String file ,int receiverID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if(file==null || file.isBlank()){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        boolean isContactExists = usersDao.isNormalUserExists(receiverID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(),receiverID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        var result = contactMessagesDao.sendContactFileMessage(token.getUserID(),file,receiverID);   //save in database
        contactMessageCallback.fileContactMessageReceived(token.getUserID() , file , receiverID); //callback for receiver
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token, List<ContactMessage> messages) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if(messages==null){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        var result = contactMessagesDao.markContactMessagesAsRead(messages);   //update in database
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<List<Group>> getAllGroups(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = groupDao.getAllGroups(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<String> getGroupPic(ClientToken token, int groupID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(null, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        var result = groupDao.getGroupPic(groupID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> setGroupPic(ClientToken token, int groupID, String pic) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<Boolean>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        if(pic==null || pic.isBlank()){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = groupDao.setGroupPic(groupID,pic);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> createGroup(ClientToken token, Group newGroup) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = groupDao.createGroup(newGroup);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<List<GroupMember>> getGroupMembers(ClientToken token, int groupID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(null, ExceptionMessages.GROUP_DOES_NOT_EXIST);
            }
            boolean isMember = groupMemberDao.isMember(groupID , token.getUserID()).getResponseData();
            if (!isMember) {
                return new RequestResult<List<GroupMember>>(null, ExceptionMessages.NOT_MEMBER);            
            }
        var result = groupMemberDao.getAllMembers(groupID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> addMemberToGroup(ClientToken token, int groupID, int otherID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData();
        if (AdminID!=token.getUserID()) {
            return new RequestResult<>(null, ExceptionMessages.NOT_ADMIN);            
        }
        var result = groupMemberDao.addMemberToGroup(token.getUserID(),groupID,otherID);   //update in database
        groupCallback.addedToGroup(otherID, groupID);  //callback for added member
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token,int groupID, int otherID) throws RemoteException {
       boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isContactExists = usersDao.isNormalUserExists(otherID).getResponseData();
        if (!isContactExists) {
            return new RequestResult<>(false, ExceptionMessages.CONTACT_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), otherID).getResponseData();
        if (!isContacts) {
            return new RequestResult<>(false, ExceptionMessages.NOT_CONTACTS);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData();
        if (AdminID!=token.getUserID()) {
            return new RequestResult<>(null, ExceptionMessages.NOT_ADMIN);            
        }
        var result = groupMemberDao.removeMemberFromGroup(token.getUserID(),groupID,otherID);   //update in database
        groupCallback.removedFromGroup(otherID, groupID);  //callback for removed member
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token, int groupID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        var result = groupMemberDao.leaveGroupAsMemeber(token.getUserID(),groupID);   //update in database
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token, int groupID, int newAdminID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData();
        if (AdminID!=token.getUserID()) {
            return new RequestResult<>(false, ExceptionMessages.NOT_ADMIN);            
        }
        var result1 = groupMemberDao.leaveGroupAsAdmin(token.getUserID(),groupID,newAdminID);   //update in database
        var result2 = groupDao.updateAdmin(groupID,newAdminID);   //update in database
        if (result1.getErrorMessage()!=null) {
            throw new RemoteException(result1.getErrorMessage());            
        }
        if (result2.getErrorMessage()!=null) {
            throw new RemoteException(result2.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> assignGroupLeadership(ClientToken token, int groupID, int newAdminID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData();
        if (AdminID!=token.getUserID()) {
            return new RequestResult<>(false, ExceptionMessages.NOT_ADMIN);            
        }
        var result = groupDao.updateAdmin(groupID, newAdminID);   //update in database
        groupCallback.leadershipGained(newAdminID, groupID);  //callback for new admin
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> deleteGroup(ClientToken token, int groupID) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(groupID).getResponseData();
        if(!isGroupExists){
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        int AdminID = groupDao.getGroupAdmin(groupID).getResponseData();
        if (AdminID!=token.getUserID()) {
            return new RequestResult<>(false, ExceptionMessages.NOT_ADMIN);            
        }
        var result = groupDao.deleteGroup(groupID);         //update in database
        groupCallback.groupDeleted(groupID);              //callback for group members
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = groupMessagesDao.getGroupMessages(token.getUserID());  
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> sendGroupMessage(ClientToken token , GroupMessage message) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if(message.getContent()==null){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_MESSAGE);
        }
        boolean isGroupExists = groupDao.isGroupExists(message.getGroupID()).getResponseData();
        if (!isGroupExists) {
            return new RequestResult<>(false, ExceptionMessages.GROUP_DOES_NOT_EXIST);
        }
        boolean isMember = groupMemberDao.isMember(message.getGroupID(), token.getUserID()).getResponseData();
        if (!isMember) {
            return new RequestResult<>(false, ExceptionMessages.NOT_MEMBER);
        }
        var result = groupMessagesDao.sendGroupMessage(message); //save in database
        groupMessageCallback.groupMessageReceived(message);  //callback for group members
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<List<Announcement>> getAnnouncements(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = announcementDao.getAllAnnouncements(token.getUserID()); 
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<Announcement>> getUnReadAnnouncements(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = announcementDao.getUnreadAnnouncements(token.getUserID()); 
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<ContactInvitation>> getContactInvitations(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = contactInvitationDao.getAllInvitations(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> sendContactInvitation(ClientToken token,ContactInvitation invitation) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if(invitation==null){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        boolean isUserExists = usersDao.isNormalUserExists(invitation.getReceiverID()).getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(false, ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), invitation.getReceiverID()).getResponseData();
        if (isContacts) {
            return new RequestResult<>(false, ExceptionMessages.ALREADY_CONTACTS);
        }
        var result = contactInvitationDao.sendContactInvitation(invitation);  //save in database
        contactInvitationCallback.contactInvitationReceived(invitation);  //callback for receiver
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> acceptContactInvitation(ClientToken token,ContactInvitation invitation) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(invitation==null){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean isUserExists = usersDao.isNormalUserExists(invitation.getReceiverID()).getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(false, ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), invitation.getReceiverID()).getResponseData();
        if (isContacts) {
            return new RequestResult<>(false, ExceptionMessages.ALREADY_CONTACTS);
        }
        var result = contactInvitationDao.acceptContactInvitation(invitation);  //update in database
        contactInvitationCallback.contactInvitationAccepted(invitation);  //callback for sender
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<Boolean> rejectContactInvitation(ClientToken token , ContactInvitation invitation) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<Boolean>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        if(invitation==null){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        boolean isUserExists = usersDao.isNormalUserExists(invitation.getReceiverID()).getResponseData();
        if (!isUserExists) {
            return new RequestResult<>(false, ExceptionMessages.USER_DOES_NOT_EXIST);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), invitation.getReceiverID()).getResponseData();
        if (isContacts) {
            return new RequestResult<>(false, ExceptionMessages.ALREADY_CONTACTS);
        }
        var result = contactInvitationDao.rejectContactInvitation(invitation);  //update in database
        contactInvitationCallback.contactInvitationRejected(invitation);  //callback for sender
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
    public RequestResult<List<Notification>> getNotifications(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.getAllNotifications(token.getUserID()); 
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<List<Notification>> getUnReadNotifications(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.getUnreadNotifications(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.markNotificationsAsRead(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> deleteNotification(ClientToken token , int notificationID) throws RemoteException{
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = notificationDao.deleteNotification(token.getUserID() , notificationID);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);

    }
    public RequestResult<NormalUser> getMyProfile(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.getNormalUserProfile(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> saveProfileChanges(ClientToken token) throws RemoteException {
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(false, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(false, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        var result = usersDao.saveProfileChanges(token.getUserID());
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(result.getResponseData(), null);
    }
    public RequestResult<Boolean> changePassword(ClientToken token , String oldPassword , String newPassword) throws RemoteException{
        boolean validToken = tokenValidator.checkClientToken(token).getResponseData();
        if (!validToken) {
            return new RequestResult<>(null, ExceptionMessages.UNREGISTERED_USER);
        }
        if(!onlineUsers.containsKey(token.getUserID())){
            return new RequestResult<>(null, ExceptionMessages.TIMEOUT_USER_EXCEPTION_MESSAGE);
        }
        boolean validOldPassword = usersDao.isPasswordValid(token.getUserID(),oldPassword).getResponseData();
        if(!validOldPassword){
            return new RequestResult<Boolean>(false, ExceptionMessages.INVALID_INPUT_DATA);
        }
        var result = usersDao.updatePassword(token.getUserID() , oldPassword , newPassword);
        if (result.getErrorMessage()!=null) {
            throw new RemoteException(result.getErrorMessage());            
        }
        return new RequestResult<>(true, null);
    }
}
