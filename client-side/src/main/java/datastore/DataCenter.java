package datastore;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jets.projects.entities.*;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.entity_info.GroupMemberInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;




/**
 * The ContactData class serves as a singleton model.
 * It provides synchronized access to collections that hold different types of
 * information, including contacts, messages, groups, announcements, invitations,
 * and notifications. This ensures thread-safe operations when multiple threads
 * may access the data concurrently.
 *
 * This class maintains observable lists and synchronized maps to manage the following:
 * - My Account Profile
 * - A list of contact information.
 * - A map associating contacts with their respective messages.
 * - A list of groups.
 * - A map associating groups with their respective messages.
 * - A list of announcements.
 * - A list of contact invitations.
 * - A list of notifications.
 *
 * The class follows a singleton design pattern to ensure only one instance of the
 * ContactData is created and provides methods to access various collections.
 */
public class DataCenter {

    private static final DataCenter instance = new DataCenter();

    private DataCenter() {}
    public static DataCenter getInstance() {
        return instance;
    }
    /*****************************************************************************/
    private NormalUser myProfile = null;
    private NormalUserStatus myStatus = null;

    public NormalUser getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(NormalUser myProfile) {
        this.myProfile = myProfile;
    }

    public NormalUserStatus getMyStatus() {
        return myStatus;
    }

    public void setMyStatus(NormalUserStatus myStatus) {
        this.myStatus = myStatus;
    }

    /********************************************************************************************/
    private final ObservableList<ContactInfo> contactList =
            FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    private final Map<Integer, ContactInfo> contactInfoMap =
            Collections.synchronizedMap(new HashMap<>());


    private final Map<Integer, ObservableList<ContactMessage>> contactMessagesMap =
            Collections.synchronizedMap(new HashMap<>());

    private final Map<Integer, IntegerProperty> unreadContactMessages =
            Collections.synchronizedMap(new HashMap<>());

    private final ObservableList<Group> groupList =
            FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    private final Map<Integer, ObservableList<GroupMessage>> groupMessagesMap =
            Collections.synchronizedMap(new HashMap<>());

    private final Map<Integer, ObservableList<GroupMemberInfo>> groupMembersMap =
            Collections.synchronizedMap(new HashMap<>());

    private final ObservableList<AnnouncementInfo> announcementList =
            FXCollections.synchronizedObservableList(FXCollections.observableArrayList());


    private final ObservableList<ContactInvitationInfo> contactInvitationList =
            FXCollections.synchronizedObservableList(FXCollections.observableArrayList());


    private final ObservableList<Notification> notificationList =
            FXCollections.synchronizedObservableList(FXCollections.observableArrayList());

    /*********************************************************************************************/

    public ObservableList<ContactInfo> getContactList() {
        return contactList;
    }

    public Map<Integer, ObservableList<ContactMessage>> getContactMessagesMap() {
        return contactMessagesMap;
    }

    public ObservableList<Group> getGroupList() {
        return groupList;
    }

    public Map<Integer, ObservableList<GroupMessage>> getGroupMessagesMap() {
        return groupMessagesMap;
    }

    public ObservableList<AnnouncementInfo> getAnnouncementList() {
        return announcementList;
    }

    public ObservableList<ContactInvitationInfo> getContactInvitationList() {
        return contactInvitationList;
    }

    public ObservableList<Notification> getNotificationList() {
        return notificationList;
    }

    public Map<Integer, ContactInfo> getContactInfoMap() {
        return contactInfoMap;
    }

    public Map<Integer, IntegerProperty> getUnreadContactMessages() {
        return unreadContactMessages;
    }

    public Map<Integer, ObservableList<GroupMemberInfo>> getGroupMembersMap() {
        return groupMembersMap;
    }

}
