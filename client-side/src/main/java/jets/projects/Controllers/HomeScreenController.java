package jets.projects.Controllers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import datastore.DataCenter;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import jets.projects.ServiceManager;
import jets.projects.Services.Request.*;
import jets.projects.Services.ServerConnectivityService;
import jets.projects.Utilities;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jets.projects.Director;
import javafx.scene.input.MouseEvent;
import jets.projects.entities.*;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;

import static jets.projects.Utilities.createContactItem;

import jets.projects.entity_info.ContactInvitationInfo;
import jets.projects.session_saving.SessionSaver;

public class HomeScreenController {
    @FXML
    private GridPane mainContainer;
    @FXML
    private Circle myprofilepicture;
    @FXML
    private Circle mystatus;
    @FXML
    private Circle pic;
    @FXML
    private Label name;
    @FXML
    private Label status;
    @FXML
    private Label id;
    @FXML
    private Label unseenNotifications;
    @FXML
    private Label unseenAnnouncements;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private ListView<ContactMessage> contactMessagesListView;
    @FXML
    private ListView<GroupMessage> groupMessagesListView;
    @FXML
    private HBox contactInfoBox;
    @FXML
    private HBox messageBox;
    @FXML
    private ListView<ContactInfo> contactListView;
    @FXML
    private ListView<Group> groupListView;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label unseenMessages;
    @FXML
    private Label unseenInvitations;

    private Stage stage;
    private Director myDirector;
    ClientAuthenticationService authenticationService = new ClientAuthenticationService();
    ClientContactService contactService = new ClientContactService();
    ClientContactMessageService contactMessageService = new ClientContactMessageService();
    ClientGroupMessageService groupMessageService = new ClientGroupMessageService();
    ClientNotificationService notificationService = new ClientNotificationService();
    ClientAnnouncementService announcementService = new ClientAnnouncementService();

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    public void perform() {
        Image image = new Image(getClass().getResource("/images/bg3.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(
                image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(150, 150, true, true, true, false)
        );
        stackPane.setVisible(false);
        stackPane.setBackground(new Background(bgImage));
        NormalUser myprofile = DataCenter.getInstance().getMyProfile();
        if (myprofile.getPic() != null) {
            myprofilepicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(myprofile.getPic()))));
        } else {
            myprofilepicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        }
        String statusType = myprofile.getStatus().toString().toLowerCase();
        String statusPath = String.format("/images/%s.png", statusType);
        mystatus.setFill(new ImagePattern(new Image(getClass().getResource(statusPath).toExternalForm())));

        // Bind unseenCount to changes in lists
        DataCenter.getInstance().getAnnouncementList().addListener((ListChangeListener<AnnouncementInfo>) change -> {
            DataCenter.getInstance().unseenAnnouncementsCountProperty().set((int) DataCenter.getInstance().getAnnouncementList().stream().filter(a -> !a.isIsRead()).count());
        });
        DataCenter.getInstance().getNotificationList().addListener((ListChangeListener<Notification>) change -> {
            DataCenter.getInstance().unseenNotificationsCountProperty().set((int) DataCenter.getInstance().getNotificationList().stream().filter(n -> !n.isIsRead()).count());
        });
        unseenAnnouncements.textProperty().bind(DataCenter.getInstance().unseenAnnouncementsCountProperty().asString());
        unseenAnnouncements.visibleProperty().bind(DataCenter.getInstance().unseenAnnouncementsCountProperty().greaterThan(0));

        unseenNotifications.textProperty().bind(DataCenter.getInstance().unseenNotificationsCountProperty().asString());
        unseenNotifications.visibleProperty().bind(DataCenter.getInstance().unseenNotificationsCountProperty().greaterThan(0));

        unseenMessages.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    int total = DataCenter.getInstance().getTotalUnreadMessages().get();
                    return total > 0 ? String.valueOf(total) : "";
                },
                DataCenter.getInstance().getTotalUnreadMessages()
        ));
        unseenMessages.visibleProperty().bind(DataCenter.getInstance().getTotalUnreadMessages().greaterThan(0));
        int total = DataCenter.getInstance().getUnreadContactMessages().values()
                .stream().mapToInt(prop -> prop.get()).sum();
        DataCenter.getInstance().getTotalUnreadMessages().set(total);

        DataCenter.getInstance().getContactInvitationList().addListener((ListChangeListener<ContactInvitationInfo>) change -> {
            DataCenter.getInstance().getInvitationsCount().set((int) DataCenter.getInstance().getContactInvitationList().size());
        });
        unseenInvitations.textProperty().bind(DataCenter.getInstance().getInvitationsCount().asString());
        unseenInvitations.visibleProperty().bind(DataCenter.getInstance().getInvitationsCount().greaterThan(0));
        DataCenter.getInstance().getInvitationsCount().set(DataCenter.getInstance().getContactInvitationList().size());
    }
    public void updateProfile(){
        NormalUser myprofile = DataCenter.getInstance().getMyProfile();
        if(myprofile.getPic() != null){
            myprofilepicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(myprofile.getPic()))));
        }else{
            myprofilepicture.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        }
    }
    @FXML
    void handleChatProfile(MouseEvent event){

        if (groupMessagesListView.isVisible()) {
            // Show group info
            Node currentNode = (Node) event.getSource();
            Stage owner = (Stage) currentNode.getScene().getWindow();
            URL fxmlURL = getClass().getResource("/fxml/groupInfo.fxml");
            Utilities.showPopup(owner, fxmlURL, 600, 400, controller -> {
                if (controller instanceof GroupInfoController) {
                    if(id.getText()!=null)
                        ((GroupInfoController) controller).setData(Integer.parseInt(id.getText()));
                }
            });
        }
    }
    @FXML
    void handleEditProfileButton(ActionEvent event) {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/editProfile.fxml");
        EditProfileController.setHome(this);
        Utilities.showPopup(owner, fxmlURL, 600, 400);
    }
    @FXML
    void handleAddContact(ActionEvent event) {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/addContact.fxml");
        Utilities.showPopup(owner, fxmlURL, 600, 400);
    }
    @FXML
    void handleAddGroup(ActionEvent event) {   
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/addGroup.fxml");
        Utilities.showPopup(owner, fxmlURL, 600, 400);
    }

    @FXML
    void handleContactButton(){
        contactListView.getSelectionModel().clearSelection(); // Clear previous selection

        contactListView.setVisible(true);
        groupListView.setVisible(false);

        contactListView.setCellFactory(lv->new ContactCard());
        contactListView.setItems(DataCenter.getInstance().getContactList());

        contactListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedContact) -> {
            if (selectedContact != null) {
                contactMessageService.markContactMessagesAsRead(selectedContact.getContact().getSecondID());
                DataCenter.getInstance().getContactMessagesMap().get(selectedContact.getContact().getSecondID())
                        .forEach(m->{
                            if(m.getSenderID() == selectedContact.getContact().getSecondID()){
                                m.setIsRead(true);
                            }
                        });
                DataCenter.getInstance().getUnreadContactMessages().get(selectedContact.getContact().getSecondID())
                        .set(0);
                int contactID =  selectedContact.getContact().getSecondID(); // extract group id
                id.setText(""+contactID);
                // open group chat
                ContactInfo contactInfo = DataCenter.getInstance().getContactInfoMap().get(contactID);
                if(contactInfo.getPic()!=null){
                    pic.setFill(new ImagePattern(new Image(new ByteArrayInputStream(contactInfo.getPic()))));
                }else{
                    pic.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
                }
                name.setText(contactInfo.getName());
                NormalUserStatus s = contactService.getContactOnlineStatus(contactID);
                status.setText(s.name());
                status.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                if(s == NormalUserStatus.AVAILABLE) {
                    Image image = new Image(getClass().getResource("/images/available.png").toExternalForm());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(10);  // Adjust width
                    imageView.setFitHeight(10); // Adjust height
                    status.setGraphic(imageView);
                }
                if(s == NormalUserStatus.BUSY){
                    Image image = new Image(getClass().getResource("/images/busy.png").toExternalForm());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(10);  // Adjust width
                    imageView.setFitHeight(10); // Adjust height
                    status.setGraphic(imageView);
                }
                if(s == NormalUserStatus.AWAY){
                    Image image = new Image(getClass().getResource("/images/away.png").toExternalForm());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(10);  // Adjust width
                    imageView.setFitHeight(10); // Adjust height
                    status.setGraphic(imageView);
                }
                if(s == NormalUserStatus.OFFLINE){
                    Image image = new Image(getClass().getResource("/images/offline.png").toExternalForm());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(10);  // Adjust width
                    imageView.setFitHeight(10); // Adjust height
                    status.setGraphic(imageView);
                }
                contactInfoBox.setVisible(true);
                messageBox.setVisible(true);
                status.setVisible(true);

                Platform.runLater(() -> {
                    stackPane.setVisible(true);
                    groupMessagesListView.setVisible(false);
                    groupMessagesListView.setManaged(false);

                    contactMessagesListView.setVisible(true);
                    contactMessagesListView.setManaged(true);

                    contactMessagesListView.toFront();
                    contactMessagesListView.getParent().requestLayout();

                    contactMessagesListView.setItems(DataCenter.getInstance().getContactMessagesMap().getOrDefault(contactID
                    ,FXCollections.synchronizedObservableList(FXCollections.observableArrayList())));
                    DataCenter.getInstance().getContactMessagesMap().get(contactID)
                            .addListener((ListChangeListener<ContactMessage>) change -> {
                                while (change.next()) {
                                    if (change.wasAdded()) {
                                        contactMessagesListView.scrollTo(
                                                DataCenter.getInstance().getContactMessagesMap()
                                                        .get(contactInfo.getContact().getSecondID()).size() - 1);
                                        if(contactMessagesListView.isVisible()){
                                            if(contactID == Integer.parseInt(id.getText())){
                                                contactMessageService.markContactMessagesAsRead(selectedContact.getContact().getSecondID());
                                                DataCenter.getInstance().getContactMessagesMap().get(selectedContact.getContact().getSecondID())
                                                        .forEach(m->{
                                                            if(m.getSenderID() == selectedContact.getContact().getSecondID()){
                                                                m.setIsRead(true);
                                                            }
                                                        });
                                                DataCenter.getInstance().getUnreadContactMessages().get(selectedContact.getContact().getSecondID())
                                                        .set(0);
                                            }
                                        }
                                    }
                                }

                            });

                    contactMessagesListView.setCellFactory(lv -> new MessageContactCard());
                    contactMessagesListView.scrollTo(contactMessagesListView.getItems().size() -1);
                });
            }
        });
    }
    @FXML
    void handleGroupButton(ActionEvent event) {
        groupListView.getSelectionModel().clearSelection(); // Clear previous selection
        contactListView.setVisible(false);
        groupListView.setVisible(true);

        groupListView.setCellFactory(lv->new GroupCard());
        groupListView.setItems(DataCenter.getInstance().getGroupList());


        groupListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedGroup) -> {
            if (selectedGroup != null) {
                int groupID =  selectedGroup.getGroupID(); // extract group id
                id.setText(""+groupID);
                // open group chat
                Group groupInfo = DataCenter.getInstance().getGroupInfoMap().get(groupID);
                if(groupInfo.getPic()!=null){
                    pic.setFill(new ImagePattern(new Image(new ByteArrayInputStream(groupInfo.getPic()))));
                }else{
                    pic.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm())));
                }
                name.setText(groupInfo.getGroupName());

                status.setVisible(false);
                contactInfoBox.setVisible(true);
                messageBox.setVisible(true);

                Platform.runLater(() -> {

                    stackPane.setVisible(true);
                    contactMessagesListView.setVisible(false);
                    contactMessagesListView.setManaged(false);

                    groupMessagesListView.setVisible(true);
                    groupMessagesListView.setManaged(true);

                    groupMessagesListView.toFront();
                    groupMessagesListView.getParent().requestLayout();

                    groupMessagesListView.setItems(DataCenter.getInstance().getGroupMessagesMap().get(groupID));
                    (DataCenter.getInstance().getGroupMessagesMap().getOrDefault(groupID,FXCollections.observableArrayList(new ArrayList<>()))).addListener((ListChangeListener<GroupMessage>) change -> {
                        while (change.next()) {
                            if (change.wasAdded()) {
                                // Scroll to the last item
                                groupMessagesListView.scrollTo(DataCenter.getInstance().getGroupMessagesMap().get(groupID).size() - 1);
                            }
                        }
                    });
                    groupMessagesListView.setCellFactory(lv -> new MessageGroupCard());
                    groupMessagesListView.scrollTo(groupMessagesListView.getItems().size() -1);
                });
            }
        });
    }
    @FXML
    void handleNotificationsButton(ActionEvent event) throws IOException {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content

        URL fxmlURL = getClass().getResource("/fxml/notifications.fxml");
        Utilities.showPopup(owner, fxmlURL, 620, 420);
        notificationService.markNotificationsAsRead();
        DataCenter.getInstance().getNotificationList().forEach((n)->{
            n.setIsRead(true);
        });
        DataCenter.getInstance().unseenNotificationsCountProperty().set(0);
    }
    @FXML
    void handleAnnouncementButton(ActionEvent event){
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/announcements.fxml");
        Utilities.showPopup(owner, fxmlURL, 620, 420);
        announcementService.markAnnouncementsAsRead();
        DataCenter.getInstance().getAnnouncementList().forEach((n)->{
            n.setIsRead(true);
        });
        DataCenter.getInstance().unseenAnnouncementsCountProperty().set(0);
    }
    @FXML
    void handleFriendRequestsButton(ActionEvent event){
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/friendRequests.fxml");
        Utilities.showPopup(owner, fxmlURL, 620, 420);
    }

    @FXML
    void handleSettingsButton(ActionEvent event) throws IOException {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/settings.fxml");
        Utilities.showPopup(owner, fxmlURL, 600, 400);
        NormalUserStatus s = DataCenter.getInstance().getMyStatus();
        if(s == NormalUserStatus.AVAILABLE){
            mystatus.setFill(new ImagePattern(new Image(getClass().getResource("/images/available.png").toExternalForm())));
        }
        if(s == NormalUserStatus.BUSY){
            mystatus.setFill(new ImagePattern(new Image(getClass().getResource("/images/busy.png").toExternalForm())));
        }
        if(s == NormalUserStatus.AWAY){
            mystatus.setFill(new ImagePattern(new Image(getClass().getResource("/images/away.png").toExternalForm())));
        }
    }
    @FXML
    void handleLogOutButton(ActionEvent event) throws IOException {
        // clear all cached data



        boolean loggedOut = authenticationService.logout();
        if(!loggedOut) ClientAlerts.invokeErrorAlert("Error" , "Failed to logout");
        else{
            stackPane.setVisible(false);
            contactMessagesListView.setVisible(false);
            groupMessagesListView.setVisible(false);
            contactListView.setVisible(false);
            groupListView.setVisible(false);
            contactInfoBox.setVisible(false);
            messageBox.setVisible(false);
            DataCenter.getInstance().getContactList().clear();
            DataCenter.getInstance().getAnnouncementList().clear();
            DataCenter.getInstance().getNotificationList().clear();
            DataCenter.getInstance().getContactMessagesMap().clear();
            DataCenter.getInstance().getGroupMessagesMap().clear();
            DataCenter.getInstance().getGroupList().clear();
            DataCenter.getInstance().unseenAnnouncementsCountProperty().set(0);
            DataCenter.getInstance().unseenNotificationsCountProperty().set(0);
            DataCenter.getInstance().getContactInfoMap().clear();
            DataCenter.getInstance().getGroupMembersMap().clear();
            DataCenter.getInstance().getContactInvitationList().clear();
            DataCenter.getInstance().setMyProfile(null);
            DataCenter.getInstance().getUnreadContactMessages().clear();
            DataCenter.getInstance().getGroupInfoMap().clear();
            ServerConnectivityService.shutDown();
            ServiceManager.stopService();

            
            SessionSaver sessionSaver = new SessionSaver();
            sessionSaver.deleteSessionFile();
        }
        myDirector.signin();
    }
    @FXML
    void handleSendButton(ActionEvent event) {
        if(messageTextArea.getText().trim().isEmpty())
            return;
        if(contactMessagesListView.isVisible()){
            ContactMessage message = new ContactMessage();
            message.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
            Integer contact_id = Integer.parseInt(id.getText());
            message.setReceiverID(contact_id);
            message.setSentAt(LocalDateTime.now());

            message.setContent(messageTextArea.getText());
            messageTextArea.clear();
            int message_ID = contactMessageService.sendContactMessage(message);
            if(message_ID == 0){
                return;
            }
            message.setID(message_ID);

            Platform.runLater(() -> {
                DataCenter.getInstance().getContactMessagesMap().get(contact_id).add(message);
                //contactMessagesListView.refresh();
            });

        }
        if (groupMessagesListView.isVisible()) {
            GroupMessage message = new GroupMessage();
            message.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
            message.setGroupID(Integer.parseInt(id.getText()));
            message.setSentAt(LocalDateTime.now());
            message.setContent(messageTextArea.getText());
            messageTextArea.clear();

            int i = groupMessageService.sendGroupMessage(message);
            if(i == 0){
                return;
            }
            message.setMessageID(i);
            DataCenter.getInstance().getGroupMessagesMap().getOrDefault(Integer.parseInt(id.getText()),FXCollections.observableArrayList(new ArrayList<>())).add(message);
        }
    }

    @FXML
    void handleAttachmentButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.mkv", "*.mov"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.doc", "*.docx", "*.txt")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Read the file into a byte array
            byte[] fileBytes;
            try {
                fileBytes = Files.readAllBytes(selectedFile.toPath());
                String fileName = selectedFile.getName();
                if (contactMessagesListView.isVisible()) {
                    // Sending a contact message attachment
                    ContactMessage attachmentMessage = new ContactMessage();
                    attachmentMessage.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
                    attachmentMessage.setReceiverID(Integer.parseInt(id.getText()));
                    attachmentMessage.setSentAt(LocalDateTime.now());
                    attachmentMessage.setContent("Attachment: " + fileName);
                    attachmentMessage.setContainsFile(true);
                    attachmentMessage.setFile(fileBytes);

                    int message_ID = contactMessageService.sendContactMessage(attachmentMessage);
                    if(message_ID == 0){
                        return;
                    }
                    attachmentMessage.setID(message_ID);
                    DataCenter.getInstance().getContactMessagesMap().get(Integer.parseInt(id.getText())).add(attachmentMessage);
                } else if (groupMessagesListView.isVisible()) {
                    // Sending a group message attachment
                    GroupMessage attachmentMessage = new GroupMessage();
                    attachmentMessage.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
                    attachmentMessage.setGroupID(Integer.parseInt(id.getText()));
                    attachmentMessage.setSentAt(LocalDateTime.now());
                    attachmentMessage.setContent("Attachment: " + fileName);
                    attachmentMessage.setContainsFile(true);
                    attachmentMessage.setFile(fileBytes);
                    int i = groupMessageService.sendGroupMessage(attachmentMessage);
                    if(i == 0){
                        return;
                    }
                    attachmentMessage.setMessageID(i);
                    DataCenter.getInstance().getGroupMessagesMap().get(Integer.parseInt(id.getText())).add(attachmentMessage);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }
    }
    public void updateStatus(){
        if(!contactMessagesListView.isVisible())
            return;
        int i = Integer.parseInt(id.getText());
        NormalUserStatus s = contactService.getContactOnlineStatus(i);
        status.setText(s.name());
        status.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        if(s == NormalUserStatus.AVAILABLE) {
            Image image = new Image(getClass().getResource("/images/available.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(10);  // Adjust width
            imageView.setFitHeight(10); // Adjust height
            status.setGraphic(imageView);
        }
        if(s == NormalUserStatus.BUSY){
            Image image = new Image(getClass().getResource("/images/busy.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(10);  // Adjust width
            imageView.setFitHeight(10); // Adjust height
            status.setGraphic(imageView);
        }
        if(s == NormalUserStatus.AWAY){
            Image image = new Image(getClass().getResource("/images/away.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(10);  // Adjust width
            imageView.setFitHeight(10); // Adjust height
            status.setGraphic(imageView);
        }
        if(s == NormalUserStatus.OFFLINE){
            Image image = new Image(getClass().getResource("/images/offline.png").toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(10);  // Adjust width
            imageView.setFitHeight(10); // Adjust height
            status.setGraphic(imageView);
        }
    }

    public void updateContactProfile() {
        if(!contactListView.isVisible())
            return;
        //contactListView.refresh();
        //handleContactButton();
        if(!contactMessagesListView.isVisible())
            return;
        int i = Integer.parseInt(id.getText());
        ContactInfo contactInfo = DataCenter.getInstance().getContactList().stream()
                .filter(c->c.getContact().getSecondID()==i).findAny().get();
        if(contactInfo.getPic()!=null){
            pic.setFill(new ImagePattern(new Image(new ByteArrayInputStream(contactInfo.getPic()))));
        }else{
            pic.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
        }
        name.setText(contactInfo.getName());
    }
}
