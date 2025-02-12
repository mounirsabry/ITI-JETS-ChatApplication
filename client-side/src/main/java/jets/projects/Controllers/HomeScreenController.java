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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import jets.projects.ServiceManager;
import jets.projects.Services.Request.*;
import jets.projects.Services.ServerConnectivityService;
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

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    public void perform() {
        NormalUser myprofile = DataCenter.getInstance().getMyProfile();
        if(myprofile.getPic() != null){
            myprofilepicture.setFill(new ImagePattern(new Image(new ByteArrayInputStream(myprofile.getPic()))));
        }else{
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
        unseenNotifications.textProperty().bind(DataCenter.getInstance().unseenNotificationsCountProperty().asString());
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
    public void handleContactButton(ActionEvent event) {
        TreeView<String> treeView = new TreeView<>();
        treeView.getStylesheets().add(getClass().getResource("/styles/homeScreenStyles.css").toExternalForm());
        treeView.getStyleClass().add("chatsList");

        // creating contacts categories
        TreeItem<String> rootItem = new TreeItem<>("Contacts");
        TreeItem<String> familyItem = new TreeItem<>("Family");
        TreeItem<String> friendsItem = new TreeItem<>("Friends");
        TreeItem<String> workItem = new TreeItem<>("Work");
        TreeItem<String> otherItem = new TreeItem<>("Others");
        List<ContactInfo> familyContacts = new ArrayList<>();
        List<ContactInfo> workContacts = new ArrayList<>();
        List<ContactInfo> friendsContacts = new ArrayList<>();
        List<ContactInfo> otherContacts = new ArrayList<>();
        for (ContactInfo contact : DataCenter.getInstance().getContactList()) {
            switch (contact.getContact().getContactGroup()) {
                case FAMILY -> {familyContacts.add(contact);}
                case WORK -> {workContacts.add(contact);}
                case FRIENDS -> {friendsContacts.add(contact);}
                case OTHER -> {otherContacts.add(contact);}
                default -> {}
            }
        }
        Utilities.populateTree(familyItem,familyContacts);
        Utilities.populateTree(friendsItem,friendsContacts);
        Utilities.populateTree(workItem,workContacts);
        Utilities.populateTree(otherItem,otherContacts);

        familyItem.setExpanded(true);
        friendsItem.setExpanded(true);
        workItem.setExpanded(true);
        otherItem.setExpanded(true);
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);
        treeView.getRoot().getChildren().addAll(Arrays.asList(familyItem, friendsItem, workItem, otherItem));
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
            if (newItem != null && newItem.getParent() != rootItem) { // Ensure it's not a category
                String contactId = newItem.getValue();
                int contactIdInt = Integer.parseInt(contactId); // Parse safely

                ContactInfo contactInfo = DataCenter.getInstance().getContactInfoMap().get(contactIdInt);
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
                int contact_ID = Integer.parseInt(contactId);
                // open chat
                Platform.runLater(() -> {
                    contactMessagesListView.setVisible(true);
                    groupMessagesListView.setVisible(false);
                    contactMessagesListView.setItems(DataCenter.getInstance().getContactMessagesMap().getOrDefault(contact_ID , FXCollections.observableArrayList()));
                    //scrollable
                    (DataCenter.getInstance().getContactMessagesMap().getOrDefault(contact_ID , FXCollections.observableArrayList())).addListener((ListChangeListener<ContactMessage>) change -> {
                        while (change.next()) {
                            if (change.wasAdded()) {
                                // Scroll to the last item
                                contactMessagesListView.scrollTo(DataCenter.getInstance().getContactMessagesMap().get(contact_ID).size() - 1);
                            }
                        }
                    });
                    contactMessagesListView.setCellFactory(lv -> new MessageContactCard());
                    contactMessagesListView.scrollTo(contactMessagesListView.getItems().size() -1);
                    if(!DataCenter.getInstance().getContactMessagesMap().get(Integer.parseInt(contactId)).isEmpty()){
                        boolean read = contactMessageService.markContactMessagesAsRead(Integer.parseInt(contactId));
                        if(!read) ClientAlerts.invokeErrorAlert("Error" , "Failed to mark messages as read");
                    }
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
        Utilities.populateGroupsList(groupsListView,DataCenter.getInstance().getGroupList());
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

                Platform.runLater(() -> {
                    groupMessagesListView.setVisible(true);
                    contactMessagesListView.setVisible(false);
                    groupMessagesListView.setItems(DataCenter.getInstance().getGroupMessagesMap().get(Integer.parseInt(groupID.getText())));
                    (DataCenter.getInstance().getGroupMessagesMap().get(Integer.parseInt(groupID.getText()))).addListener((ListChangeListener<GroupMessage>) change -> {
                        while (change.next()) {
                            if (change.wasAdded()) {
                                // Scroll to the last item
                                groupMessagesListView.scrollTo(DataCenter.getInstance().getGroupMessagesMap().get(Integer.parseInt(groupID.getText())).size() - 1);
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
        // clear all cached data



        boolean loggedOut = authenticationService.logout();
        if(!loggedOut) ClientAlerts.invokeErrorAlert("Error" , "Failed to logout");
        else{
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
            contactMessagesListView.getItems().clear();
            contactMessagesListView.setVisible(false);
            groupMessagesListView.getItems().clear();
            groupMessagesListView.setVisible(false);
        }
        myDirector.signin();
    }
    @FXML
    void handleSendButton(ActionEvent event) {
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
            DataCenter.getInstance().getContactMessagesMap().get(contact_id).add(message);
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
            DataCenter.getInstance().getGroupMessagesMap().get(Integer.parseInt(id.getText())).add(message);
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
} 
