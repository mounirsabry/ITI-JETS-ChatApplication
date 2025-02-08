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
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entities.Notification;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactInvitationInfo;
import java.util.*;
import java.util.concurrent.*;

public class LoadingController {
    @FXML
    private ProgressBar progressBar;
    private Stage stage;
    private Director director;
    private static final int THREAD_POOL_SIZE = 5;

    ClientContactService contactService = new ClientContactService();
    ClientContactMessageService messageService = new ClientContactMessageService();
    ClientGroupService groupService = new ClientGroupService();
    ClientAnnouncementService announcementService = new ClientAnnouncementService();
    ClientInvitationService invitationService = new ClientInvitationService();
    ClientNotificationService notificationService = new ClientNotificationService();

    public void setDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }

    public void perform() {
        Platform.runLater(() -> progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS));

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            Future<List<ContactInfo>> contactsFuture = executor.submit(() -> contactService.getContacts());
            Future<List<Group>> groupsFuture = executor.submit(() -> groupService.getGroups());
            Future<List<AnnouncementInfo>> announcementsFuture = executor.submit(() -> announcementService.getAllAnnouncements());
            Future<List<ContactInvitationInfo>> invitationsFuture = executor.submit(() -> invitationService.getContactInvitations());
            Future<List<Notification>> notificationsFuture = executor.submit(() -> notificationService.getNotifications());

            List<ContactInfo> contactList = contactsFuture.get();
            // Fetch messages only from each contact
            if (contactList != null) {
                DataCenter.getInstance().getContactList().addAll(contactList);
                List<Future<List<ContactMessage>>> futureMessages = new ArrayList<>();
                for (ContactInfo contact : contactList) {
                    futureMessages.add(executor.submit(() -> messageService.getAllContactMessages(contact.getContact().getSecondID())));
                }
                for (int i = 0; i < contactList.size(); i++) {
                    ContactInfo contact = contactList.get(i);
                    try {
                        List<ContactMessage> messages = futureMessages.get(i).get();
                        int unread = (int) messages.stream().filter(message -> !message.getIsRead()).count();
                        DataCenter.getInstance().getUnreadContactMessages().put(contact.getContact().getSecondID(), new SimpleIntegerProperty(unread));
                        DataCenter.getInstance().getContactMessagesMap().put(contact.getContact().getSecondID(), FXCollections.observableArrayList(messages));
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
            // Wait for all other tasks to complete
            DataCenter.getInstance().getGroupList().addAll(groupsFuture.get());
            DataCenter.getInstance().getAnnouncementList().addAll(announcementsFuture.get());
            DataCenter.getInstance().getContactInvitationList().addAll(invitationsFuture.get());
            DataCenter.getInstance().getNotificationList().addAll(notificationsFuture.get());
            Platform.runLater(() -> director.home());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println(e.getMessage());
        } finally {
            if(!executor.isShutdown()){
                executor.shutdownNow();
            }
        }
    }
}