package jets.projects.Controllers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import datastore.DataCenter;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import jets.projects.Services.Request.*;
import jets.projects.Utilities;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jets.projects.Director;
import javafx.scene.input.MouseEvent;
import jets.projects.entities.*;
import jets.projects.entity_info.AnnouncementInfo;
import jets.projects.entity_info.ContactInfo;

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

    private Stage stage;
    private Director myDirector;
    ClientAuthenticationService authenticationService = new ClientAuthenticationService();
    ClientContactService contactService = new ClientContactService();
    ClientContactMessageService contactMessageService = new ClientContactMessageService();
    ClientGroupMessageService groupMessageService = new ClientGroupMessageService();
    private ObservableList<ContactMessage> contactMessageObservableList = FXCollections.observableArrayList() ;
    private ObservableList<GroupMessage> groupMessageObservableList = FXCollections.observableArrayList() ;
    private ObservableList<ContactInfo> contactsList = DataCenter.getInstance().getContactList();
    private ObservableList<AnnouncementInfo> announcementsList = DataCenter.getInstance().getAnnouncementList();
    private ObservableList<Notification> notificationsList = DataCenter.getInstance().getNotificationList();
    private Map<Integer , ObservableList<ContactMessage>> contactMessagesMap =DataCenter.getInstance().getContactMessagesMap();
    private Map<Integer , ObservableList<GroupMessage>> groupMessagesMap =DataCenter.getInstance().getGroupMessagesMap();
    private ObservableList<Group> groupsList =DataCenter.getInstance().getGroupList();
    private SimpleIntegerProperty unseenAnnouncementsCount = DataCenter.getInstance().unseenAnnouncementsCountProperty();
    private SimpleIntegerProperty unseenNotificationsCount = DataCenter.getInstance().unseenNotificationsCountProperty();

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    public void perform() {
        NormalUser myprofile = DataCenter.getInstance().getMyProfile();
        myprofilepicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(myprofile.getPic()))));
        String statusType = myprofile.getStatus().toString().toLowerCase();
        String statusPath = String.format("/images/%s.png", statusType);
        mystatus.setFill(new ImagePattern(new Image(getClass().getResource(statusPath).toExternalForm())));

        // Bind unseenCount to changes in lists
        announcementsList.addListener((ListChangeListener<AnnouncementInfo>) change -> {
            unseenAnnouncementsCount.set((int) announcementsList.stream().filter(a -> !a.isIsRead()).count());
        });
        notificationsList.addListener((ListChangeListener<Notification>) change -> {
            unseenNotificationsCount.set((int) notificationsList.stream().filter(n -> !n.isIsRead()).count());
        });
        unseenAnnouncements.textProperty().bind(unseenAnnouncementsCount.asString());
        unseenNotifications.textProperty().bind(unseenNotificationsCount.asString());
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
    void handleContactButton(ActionEvent event) {
        TreeView<String> treeView = new TreeView<>();
        treeView.getStylesheets().add(getClass().getResource("/styles/homeScreenStyles.css").toExternalForm());
        treeView.getStyleClass().add("chatsList");

        // creating contacts categories
        TreeItem<String> rootItem = new TreeItem<>("Contacts");
        TreeItem<String> familyItem = new TreeItem<>("Family");
        TreeItem<String> friendsItem = new TreeItem<>("Friends");
        TreeItem<String> workItem = new TreeItem<>("Work");
        List<ContactInfo> familyContacts = new ArrayList<>();
        List<ContactInfo> workContacts = new ArrayList<>();
        List<ContactInfo> friendsContacts = new ArrayList<>();
        for (ContactInfo contact : contactsList) {
            switch (contact.getContact().getContactGroup()) {
                case FAMILY -> {familyContacts.add(contact);}
                case WORK -> {workContacts.add(contact);}
                case FRIENDS -> {friendsContacts.add(contact);}
                default -> {}
            }
        }
        Utilities.populateTree(familyItem,familyContacts);
        Utilities.populateTree(friendsItem,friendsContacts);
        Utilities.populateTree(workItem,workContacts);

        familyItem.setExpanded(true);
        friendsItem.setExpanded(true);
        workItem.setExpanded(true);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        treeView.getRoot().getChildren().addAll(Arrays.asList(familyItem, friendsItem, workItem));
        treeView.setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    TreeItem<String> treeItem = getTreeItem();
                    if(treeItem.getParent()==rootItem){
                        setText(treeItem.getValue());
                    }
                    else {
                        setText(null);
                    }
                    setGraphic(treeItem.getGraphic());
                }
            }});
        Node existingNode = Utilities.getExistingNode(mainContainer, 1, 2);
        if (existingNode != null) {
            mainContainer.getChildren().remove(existingNode);
            mainContainer.add(treeView, 1, 2);
            GridPane.setRowSpan(treeView , GridPane.REMAINING);
            treeView.minWidth(200);
        }
        // view contact chat on selecting this contact from contact list
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                String contactId = newItem.getValue();
                ContactInfo contactInfo = DataCenter.getInstance().getContactInfoMap().get(Integer.parseInt(contactId));
                if(contactInfo.getPic()!=null){
                    pic.setFill(new ImagePattern(new Image(new ByteArrayInputStream(contactInfo.getPic()))));
                }else{
                    pic.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
                }
                name.setText(contactInfo.getName());
                id.setText(contactId);
                NormalUserStatus contactStatus = contactService.getContactOnlineStatus(Integer.parseInt(contactId));
                if(contactStatus!=null){
                    status.setText(contactStatus.toString());
                }else{
                    status.setText("UNKNOWN");
                    status.setStyle("-fx-text-fill: red;");
                }
                ObservableList<ContactMessage> messages = contactMessagesMap.get(Integer.parseInt(contactId));
                // open chat
                Platform.runLater(() -> {
                    contactMessagesListView.setVisible(true);
                    groupMessagesListView.setVisible(false);
                    contactMessagesListView.setItems(contactMessageObservableList);
                    contactMessagesListView.setCellFactory(lv -> new MessageContactCard());
                    contactMessagesListView.scrollTo(contactMessagesListView.getItems().size() -1);
                    contactMessageObservableList.clear();
                    contactMessageObservableList.addAll(messages);
                    boolean read = contactMessageService.markContactMessagesAsRead(Integer.parseInt(contactId));
                    if(!read) ClientAlerts.invokeErrorAlert("Error" , "Failed to mark messages as read");
                });
            }
        });
    }
    @FXML
    void handleGroupButton(ActionEvent event) {
        ListView<HBox> groupsListView = new ListView<HBox>();
        groupsListView.getStylesheets().add(getClass().getResource("/styles/homeScreenStyles.css").toExternalForm());
        groupsListView.getStyleClass().add("chatsList");
        // set custom list items
        groupsListView.setCellFactory(lv->Utilities.createCustomCell());

        Node existingNode = Utilities.getExistingNode(mainContainer, 1, 2);
        if (existingNode != null) {
            mainContainer.getChildren().remove(existingNode);
            mainContainer.add(groupsListView, 1, 2);
            GridPane.setRowSpan(groupsListView , GridPane.REMAINING);
            groupsListView.minWidth(200);
        }
        Utilities.populateGroupsList(groupsListView,groupsList);
        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedGroup) -> {
            if (selectedGroup != null) {
                Text groupID =  (Text) selectedGroup.getChildren().get(2); // extract group id
                id.setText(groupID.getText());
                // open group chat
                Group groupInfo = DataCenter.getInstance().getGroupInfoMap().get(Integer.parseInt(groupID.getText()));
                if(groupInfo.getPic()!=null){
                    pic.setFill(new ImagePattern(new Image(new ByteArrayInputStream(groupInfo.getPic()))));
                }else{
                    pic.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm())));
                }
                name.setText(groupInfo.getGroupName());
                ObservableList<GroupMessage> messages = groupMessagesMap.get(Integer.parseInt(groupID.getText()));
                Platform.runLater(() -> {
                    groupMessagesListView.setVisible(true);
                    contactMessagesListView.setVisible(false);
                    groupMessagesListView.setItems(groupMessageObservableList);
                    groupMessagesListView.setCellFactory(lv -> new MessageGroupCard());
                    groupMessagesListView.scrollTo(groupMessagesListView.getItems().size() -1);
                    groupMessageObservableList.clear();
                    groupMessageObservableList.addAll(messages);
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
    void handleAnnouncementButton(ActionEvent event){
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/announcements.fxml");
        Utilities.showPopup(owner, fxmlURL, 620, 420);
    }
    @FXML
    void handleSettingsButton(ActionEvent event) throws IOException {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/settings.fxml");
        Utilities.showPopup(owner, fxmlURL, 600, 400);
    }
    @FXML
    void handleLogOutButton(ActionEvent event) throws IOException {
        boolean loggedOut = authenticationService.logout();
        if(!loggedOut) ClientAlerts.invokeErrorAlert("Error" , "Failed to logout");
        myDirector.signin();
    }
    @FXML
    void handleSendButton(ActionEvent event) {
        if(contactMessagesListView.isVisible()){
            ContactMessage message = new ContactMessage();
            message.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
            message.setReceiverID(Integer.parseInt(id.getText()));
            message.setSentAt(LocalDateTime.now());
            message.setContent(messageTextArea.getText());
            messageTextArea.clear();
            contactMessageObservableList.add(message);
            contactMessagesListView.scrollTo(contactMessagesListView.getItems().size() -1);
            boolean sent = contactMessageService.sendContactMessage(message);
            if(!sent) ClientAlerts.invokeErrorAlert("Error" , "Failed to send contact message");
        }
        if (groupMessagesListView.isVisible()) {
            GroupMessage message = new GroupMessage();
            message.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
            message.setGroupID(Integer.parseInt(id.getText()));
            message.setSentAt(LocalDateTime.now());
            message.setContent(messageTextArea.getText());
            messageTextArea.clear();
            groupMessageObservableList.add(message);
            groupMessagesListView.scrollTo(contactMessagesListView.getItems().size() -1);
            boolean sent = groupMessageService.sendGroupMessage(message);
            if(!sent) ClientAlerts.invokeErrorAlert("Error" , "Failed to send group message");
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
                    contactMessageObservableList.add(attachmentMessage);
                    contactMessagesListView.scrollTo(contactMessagesListView.getItems().size() - 1);
                    boolean sent = contactMessageService.sendContactMessage(attachmentMessage);
                    if (!sent) ClientAlerts.invokeErrorAlert("Error", "Failed to send contact message");

                } else if (groupMessagesListView.isVisible()) {
                    // Sending a group message attachment
                    GroupMessage attachmentMessage = new GroupMessage();
                    attachmentMessage.setSenderID(DataCenter.getInstance().getMyProfile().getUserID());
                    attachmentMessage.setGroupID(Integer.parseInt(id.getText()));
                    attachmentMessage.setSentAt(LocalDateTime.now());
                    attachmentMessage.setContent("Attachment: " + fileName);
                    attachmentMessage.setContainsFile(true);
                    attachmentMessage.setFile(fileBytes);
                    groupMessageObservableList.add(attachmentMessage);
                    groupMessagesListView.scrollTo(groupMessagesListView.getItems().size() - 1);
                    boolean sent = groupMessageService.sendGroupMessage(attachmentMessage);
                    if (!sent) ClientAlerts.invokeErrorAlert("Error", "Failed to send group message");
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }
    }
} 
