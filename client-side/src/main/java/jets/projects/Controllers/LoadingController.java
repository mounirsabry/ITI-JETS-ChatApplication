package jets.projects.Controllers;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import jets.projects.Director;
import jets.projects.entities.Announcement;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.Notification;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.entity_info.ContactMessagesInfo;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.*;

public class LoadingController {
    @FXML
    private ProgressBar progressBar;
    private Stage stage;
    private Director director;
    private static final int THREAD_POOL_SIZE = 5;

    public void setDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }

    public void perform() {
        Platform.runLater(() -> progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS));

        Map<Integer, ContactMessagesInfo> messagesInfoMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            Future<List<ContactInfo>> contacts = executor.submit(this::getAllContacts);
            List<ContactInfo> contactList = contacts.get(); // Wait for contacts to load

            if (contactList != null) {
                List<Future<ContactMessagesInfo>> futures = new ArrayList<>();

                // Fetch messages from each contact
                for (ContactInfo contact : contactList) {
                    futures.add(executor.submit(() -> getAllContactMessages(1, contact.getContact().getSecondID())));
                }
                for (int i = 0; i < contactList.size(); i++) {
                    ContactInfo contact = contactList.get(i);
                    try {
                        ContactMessagesInfo messagesInfo = futures.get(i).get();
                        messagesInfoMap.put(contact.getContact().getSecondID(), messagesInfo);
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            Future<List<Group>> groupsFutures = executor.submit(()->getAllGroups(1));
            List<Group> groups = groupsFutures.get();
            Future<List<AnnouncementInfo>> announcementsFutures = executor.submit(()->getAllAnnouncements(1));
            List<AnnouncementInfo> announcements = announcementsFutures.get();
            DataCenter.getInstance().getAnnouncementList().addAll(announcements);

            Future<List<ContactInvitationInfo>> invitationsFutures = executor.submit(()->getAllInvitations(2));
            List<ContactInvitationInfo> invitations = invitationsFutures.get();
            DataCenter.getInstance().getContactInvitationList().addAll(invitations);

            Future<List<Notification>> notificationsFutures = executor.submit(()->getAllNotifications(2));
            List<Notification> notifications = notificationsFutures.get();
            DataCenter.getInstance().getNotificationList().addAll(notifications);

            Platform.runLater(() -> director.home(contactList, messagesInfoMap,groups));

        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        } finally {
            executor.shutdown();
        }
    }

    public List<ContactInfo> getAllContacts() {
        try {
            ContactService contactService = new MockContactService();
            List<ContactInfo> contacts = contactService.getAllContacts();
            return contacts != null ? contacts : Collections.emptyList();
        } catch (RemoteException e) {
            return Collections.emptyList();
        }
    }

    public ContactMessagesInfo getAllContactMessages(int userID, int contactId) {
        try {
            ContactService contactService = new MockContactService();
            List<ContactMessage> messages = contactService.getMessagesForContact(userID, contactId);
            int unread = (int) messages.stream().filter(message -> !message.getIsRead()).count();
            return new ContactMessagesInfo(messages, unread);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return new ContactMessagesInfo(Collections.emptyList(), 0);
        }
    }
    public List<Group> getAllGroups(int userID) {
        try {
            ContactService contactService = new MockContactService();
            List<Group> groups = contactService.getAllGroups(userID);
            return groups != null ? groups : Collections.emptyList();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<AnnouncementInfo> getAllAnnouncements(int userID) {
        try {
            ContactService contactService = new MockContactService();
            List<AnnouncementInfo> announcements = contactService.getAllAnnouncements(userID);
            return announcements != null ? announcements : Collections.emptyList();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<ContactInvitationInfo> getAllInvitations(int userID) {
        try {
            ContactService contactService = new MockContactService();
            List<ContactInvitationInfo> invitations = contactService.getAllContactInvitations(userID);
            return invitations != null ? invitations : Collections.emptyList();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<Notification> getAllNotifications(int userID) {
        try {
            ContactService contactService = new MockContactService();
            List<Notification> notifications = contactService.getAllNotifications(userID);
            return notifications != null ? notifications : Collections.emptyList();
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
            return Collections.emptyList();
        }
    }
}
