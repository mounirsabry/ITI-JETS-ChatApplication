package jets.projects.topcontrollers;

import java.util.Date;
import java.util.List;
import jets.projects.classes.RequestResult;
import jets.projects.entities.Announcement;
import jets.projects.session.ClientToken;
import jets.projects.entities.Contact;
import jets.projects.entities.ContactInvitation;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.GroupMessage;
import jets.projects.entities.Notification;
import jets.projects.session.ClientSessionData;
import jets.projects.dao.ContactsDao;
import jets.projects.dao.FilesDao;
import jets.projects.dao.TokenValidatorDao;
import jets.projects.dao.UsersDao;
import jets.projects.entities.Gender;
import jets.projects.entities.GroupMember;
import jets.projects.entities.NormalUser;
import jets.projects.entities.NormalUserStatus;

public class NormalUserManager {
    UsersDao usersDao = new UsersDao();
    TokenValidatorDao tokenValidator = new TokenValidatorDao();
    FilesDao filesDao = new FilesDao();
    ContactsDao contactsDao = new ContactsDao();

    public RequestResult<ClientSessionData> login(String phoneNumber, String password) {
        ClientSessionData sessionData = usersDao.clientLogin(phoneNumber, password);
        return new RequestResult<>(true, sessionData);
    }

    public RequestResult<Boolean> logout(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
        Boolean result = usersDao.clientLogout(token.getUserID());
        return new RequestResult<>(true, result);
    }

    public RequestResult<Boolean> registerNormalUser(
            String displayName, String phoneNumber, String email, String pic,
            String password, Gender gender, String country, Date birthDate,
            String bio) {
        Boolean result = usersDao.registerNormalUser(
                displayName, phoneNumber, email, pic,
                password, gender, country, birthDate, bio);
        return new RequestResult<>(true, result);
    }

    public RequestResult<String> getProfilePic(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
        String pic = filesDao.getNormalUserProfilePic(token.getUserID());
        return new RequestResult<>(true, pic);
    }
    
    public RequestResult<Boolean> setOnlineStatus(ClientToken token,
            NormalUserStatus newStatus) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
        Boolean result = usersDao.setOnlineStatus(token.getUserID(), newStatus);
        return new RequestResult<>(true, result);
    }

    public RequestResult<List<Contact>> getContacts(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
        List<Contact> result = contactsDao.getContacts(token.getUserID());
        return new RequestResult<>(true, result);
    }
    
    public RequestResult<Contact> getContactProfile(ClientToken token, int contactID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
        boolean isContactExists = usersDao.isNormalUserExists(contactID);
        if (!isContactExists) {
            return new RequestResult<>(true, null);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), contactID);
        if (!isContacts) {
            return new RequestResult<>(false, null);
        }
        Contact result = usersDao.getContactProfile(contactID);
        return new RequestResult<>(true, result);
    }
    
    public RequestResult<String> getContactProfilePic(ClientToken token, int contactID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
        boolean isContacts = contactsDao.isContacts(token.getUserID(), contactID);
        if (!isContacts) {
            return new RequestResult<>(false, null);
        }
        String contactPic = filesDao.getNormalUserProfilePic(contactID);
        return new RequestResult<>(true, contactPic);
    }

    public RequestResult<List<ContactMessage>> getContactMessages(ClientToken token, int otherID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<List<ContactMessage>> getUnReadContactMessages(ClientToken token, int otherID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<Boolean> sendContactMessage(ClientToken token, ContactMessage message) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<Boolean> sendContactFileMessage(ClientToken token, String file) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<Boolean> markContactMessagesAsRead(ClientToken token, List<ContactMessage> messages) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<List<Group>> getGroups(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<String> getGroupPic(ClientToken token, int groupID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<Boolean> createGroup(ClientToken token, Group newGroup) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<List<GroupMember>> getGroupMembers(ClientToken token, int groupID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> addMemberToGroup(ClientToken token, int groupID, int otherID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> removeMemberFromGroup(ClientToken token, 
            int groupID, int otherID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> leaveGroupAsMember(ClientToken token, int groupID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> leaveGroupAsAdmin(ClientToken token, int groupID, int newAdminID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> assignGroupLeadership(ClientToken token, int groupID, int newAdminID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> deleteGroup(ClientToken token, int groupID) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    

    public RequestResult<List<GroupMessage>> getGroupMessages(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<List<Announcement>> getAnnouncements(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<List<Announcement>> getUnReadAnnouncements(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<List<ContactInvitation>> getContactInvitations(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<List<Notification>> getNotifications(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<Boolean> markNotificationsAsRead(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
    
    public RequestResult<NormalUser> getMyProfile(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }

    public RequestResult<Boolean> saveProfileChanges(ClientToken token) {
        boolean isTokenValid = tokenValidator.checkClientToken(token);
        if (!isTokenValid) {
            return new RequestResult<>(false, null);
        }
    }
}
