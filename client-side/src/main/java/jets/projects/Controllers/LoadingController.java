package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import jets.projects.Director;
import jets.projects.Services.Request.*;
import jets.projects.entities.*;
import jets.projects.entity_info.*;

import java.util.ArrayList;
import java.util.List;

public class LoadingController {
    @FXML
    private ProgressBar progressBar;
    private Stage stage;
    private Director director;

    ClientProfileService profileService = new ClientProfileService();
    ClientContactService contactService = new ClientContactService();
    ClientContactMessageService messageService = new ClientContactMessageService();
    ClientGroupService groupService = new ClientGroupService();
    ClientGroupMessageService groupMessageService = new ClientGroupMessageService();
    ClientAnnouncementService announcementService = new ClientAnnouncementService();
    ClientInvitationService invitationService = new ClientInvitationService();
    ClientNotificationService notificationService = new ClientNotificationService();

    public void setDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }

    public void perform() {
        Platform.runLater(() -> progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS));

        try {
            // Fetch all data sequentially without Future or ExecutorService
            NormalUser myProfile = profileService.getMyProfile();
            System.out.println(myProfile);
            List<ContactInfo> contactList = contactService.getContacts();
            System.out.println(contactList);
            List<Group> groups = groupService.getGroups();
            List<AnnouncementInfo> announcements = announcementService.getAllAnnouncements();
            List<ContactInvitationInfo> invitations = invitationService.getContactInvitations();
            List<Notification> notifications = notificationService.getNotifications();

            // Process Contacts and Messages
            if (contactList != null) {
                DataCenter.getInstance().getContactList().addAll(contactList);
                for (ContactInfo contact : contactList) {
                    List<ContactMessage> messages = messageService.getAllContactMessages(contact.getContact().getSecondID());
                    int unreadCount = (int) messages.stream().filter(msg -> !msg.getIsRead()).count();

                    DataCenter.getInstance().getContactInfoMap().put(contact.getContact().getSecondID(), contact);
                    DataCenter.getInstance().getUnreadContactMessages().put(contact.getContact().getSecondID(), new SimpleIntegerProperty(unreadCount));
                    DataCenter.getInstance().getContactMessagesMap().put(contact.getContact().getSecondID(),
                            FXCollections.synchronizedObservableList(FXCollections.observableArrayList(messages != null ? messages : new ArrayList<>())));
                }
            }

            // Process Groups and Messages
            if (groups != null) {
                DataCenter.getInstance().getGroupList().addAll(groups);
                for (Group group : groups) {
                    List<GroupMessage> messages = groupMessageService.getGroupMessages(group.getGroupID());
                    List<GroupMemberInfo> members = groupService.getGroupMembers(group.getGroupID());

                    DataCenter.getInstance().getGroupInfoMap().put(group.getGroupID(), group);
                    DataCenter.getInstance().getGroupMessagesMap().put(group.getGroupID(),
                            FXCollections.synchronizedObservableList(FXCollections.observableArrayList(messages != null ? messages : new ArrayList<>())));
                    DataCenter.getInstance().getGroupMembersMap().put(group.getGroupID(),
                            FXCollections.synchronizedObservableList(FXCollections.observableArrayList(members != null ? members : new ArrayList<>())));
                }
            }

            // Store all retrieved data
            DataCenter.getInstance().setMyProfile(myProfile);
            DataCenter.getInstance().getContactInvitationList().addAll(invitations);
            DataCenter.getInstance().getAnnouncementList().addAll(announcements);
            DataCenter.getInstance().getNotificationList().addAll(notifications);

            // Count unread announcements and notifications
            DataCenter.getInstance().unseenAnnouncementsCountProperty().set(
                    (int) announcements.stream().filter(a -> !a.isIsRead()).count()
            );
            DataCenter.getInstance().unseenNotificationsCountProperty().set(
                    (int) notifications.stream().filter(n -> !n.isIsRead()).count()
            );

            // Navigate to Home
            Platform.runLater(() -> director.home());
        } catch (Exception e) {
            System.err.println("Error in loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

}





//package jets.projects.Controllers;
//
//import datastore.DataCenter;
//import javafx.application.Platform;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.collections.FXCollections;
//import javafx.fxml.FXML;
//import javafx.scene.control.ProgressBar;
//import javafx.stage.Stage;
//import jets.projects.Director;
//import jets.projects.Services.Request.*;
//import jets.projects.entities.*;
//import jets.projects.entity_info.AnnouncementInfo;
//import jets.projects.entity_info.ContactInfo;
//import jets.projects.entity_info.ContactInvitationInfo;
//import jets.projects.entity_info.GroupMemberInfo;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class LoadingController{
//    @FXML
//    private ProgressBar progressBar;
//    private Stage stage;
//    private Director director;
//    private static final int THREAD_POOL_SIZE = 6;
//
//    ClientProfileService profileService = new ClientProfileService();
//    ClientContactService contactService = new ClientContactService();
//    ClientContactMessageService messageService = new ClientContactMessageService();
//    ClientGroupService groupService = new ClientGroupService();
//    ClientGroupMessageService groupMessageService = new ClientGroupMessageService();
//    ClientAnnouncementService announcementService = new ClientAnnouncementService();
//    ClientInvitationService invitationService = new ClientInvitationService();
//    ClientNotificationService notificationService = new ClientNotificationService();
//
//    public void setDirector(Stage stage, Director director) {
//        this.stage = stage;
//        this.director = director;
//    }
//    public void perform() {
//        Platform.runLater(() -> progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS));
//
//        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
//        try {
//            Future<NormalUser> myprofile = executor.submit(()->profileService.getMyProfile());
//            Future<List<ContactInfo>> contactsFuture = executor.submit(() -> contactService.getContacts());
//            Future<List<Group>> groupsFuture = executor.submit(() -> groupService.getGroups());
//            Future<List<AnnouncementInfo>> announcementsFuture = executor.submit(() -> announcementService.getAllAnnouncements());
//            Future<List<ContactInvitationInfo>> invitationsFuture = executor.submit(() -> invitationService.getContactInvitations());
//            Future<List<Notification>> notificationsFuture = executor.submit(() -> notificationService.getNotifications());
//
//            List<ContactInfo> contactList = contactsFuture.get();
//            // Fetch messages only from each contact
//            if (contactList != null) {
//                DataCenter.getInstance().getContactList().addAll(contactList);
//                List<Future<List<ContactMessage>>> futureMessages = new ArrayList<>();
//                for (ContactInfo contact : contactList) {
//                    futureMessages.add(executor.submit(() -> messageService.getAllContactMessages(contact.getContact().getSecondID())));
//                    DataCenter.getInstance().getContactInfoMap().put(contact.getContact().getSecondID(),contact);
//                }
//                for (int i = 0; i < contactList.size(); i++) {
//                    ContactInfo contact = contactList.get(i);
//                    System.out.println(contact);
//                    try {
//                        List<ContactMessage> messages = futureMessages.get(i).get();
//                        System.out.println(messages);
//                        int unread = (int) messages.stream().filter(message -> !message.getIsRead()).count();
//                        DataCenter.getInstance().getUnreadContactMessages().put(contact.getContact().getSecondID(), new SimpleIntegerProperty(unread));
//                        DataCenter.getInstance().getContactMessagesMap().put(contact.getContact().getSecondID()
//                                , FXCollections.synchronizedObservableList(FXCollections.observableArrayList(messages != null ? messages : new ArrayList<>())));
//                    } catch (InterruptedException | ExecutionException e) {
//                        System.err.println(e.getMessage());
//                    }
//                }
//            }
//            List<Group> groups = groupsFuture.get();
//            // Fetch messages only from each group
//            if (groups != null) {
//                DataCenter.getInstance().getGroupList().addAll(groups);
//                List<Future<List<GroupMessage>>> futureMessages = new ArrayList<>();
//                List<Future<List<GroupMemberInfo>>> futureMembers = new ArrayList<>();
//                for (Group group : groups) {
//                    futureMessages.add(executor.submit(() -> groupMessageService.getGroupMessages(group.getGroupID())));
//                    futureMembers.add(executor.submit(() -> groupService.getGroupMembers(group.getGroupID())));
//                    DataCenter.getInstance().getGroupInfoMap().put(group.getGroupID(),group);
//                }
//                for (int i = 0; i < groups.size(); i++) {
//                    Group group = groups.get(i);
//                    System.out.println(group);
//                    try {
//                        List<GroupMessage> messages = futureMessages.get(i).get();
//                        List<GroupMemberInfo>  members = futureMembers.get(i).get();
//                        DataCenter.getInstance().getGroupMembersMap().put(group.getGroupID()
//                                ,FXCollections.synchronizedObservableList(FXCollections.observableArrayList(members != null ? members : new ArrayList<>())));
//                        DataCenter.getInstance().getGroupMessagesMap().put(group.getGroupID()
//                                ,FXCollections.synchronizedObservableList(FXCollections.observableArrayList(messages != null ? messages : new ArrayList<>())));
//                    } catch (InterruptedException | ExecutionException e) {
//                        System.err.println(e.getMessage());
//                    }
//                }
//            }
//            // Wait for all other tasks to complete
//            DataCenter.getInstance().setMyProfile(myprofile.get());
//            DataCenter.getInstance().getContactInvitationList().addAll(invitationsFuture.get());
//            DataCenter.getInstance().getAnnouncementList().addAll(announcementsFuture.get());
//            DataCenter.getInstance().getNotificationList().addAll(notificationsFuture.get());
//            DataCenter.getInstance().unseenAnnouncementsCountProperty().set((int) announcementsFuture.get().stream().filter(a -> !a.isIsRead()).count());
//            DataCenter.getInstance().unseenNotificationsCountProperty().set((int) notificationsFuture.get().stream().filter(n -> !n.isIsRead()).count());
//            Platform.runLater(() -> director.home());
//        } catch (InterruptedException | ExecutionException e) {
//            System.err.println(e.getMessage());
//        } finally {
//            if (!executor.isShutdown()) {
//                executor.shutdownNow();
//            }
//        }
//    }
//}


