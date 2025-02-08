package jets.projects.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import datastore.DataCenter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
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
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entity_info.ContactInfo;

public class HomeScreenController {

    @FXML
    private GridPane mainContainer;
    @FXML
    private TextField searchTextField;
    @FXML
    private Circle myprofilepicture;
    @FXML
    private Circle mystatus;
    @FXML
    private ListView<TextFlow> openChatList;
    @FXML
    private Circle chatProfile;
    private Stage stage;
    private Director myDirector;
    private final ObservableList<TextFlow> openChatObservableList = FXCollections.observableArrayList() ;
    private ObservableList<ContactInfo> contactsList = DataCenter.getInstance().getContactList();
    private  Map<Integer , ObservableList<ContactMessage>> messagesMap =DataCenter.getInstance().getContactMessagesMap();
    private ObservableList<Group> groupsList =DataCenter.getInstance().getGroupList();

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    public void perform() {
        Image userprofile = new Image(getClass().getResource("/images/profile.png").toExternalForm());
        Image myprofile = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
        chatProfile.setFill(new ImagePattern(userprofile));
        myprofilepicture.setFill(new ImagePattern(myprofile));
        openChatList.setItems(openChatObservableList);
    }
    @FXML
    void handleSearchTextField(ActionEvent event) {

    }
    @FXML
    void handleChatProfile(MouseEvent event){
        //if currently open chat is normal chat show user profile
        //else show group info
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/groupInfo.fxml");
        Utilities.showPopup(owner, fxmlURL, 600, 400);
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
                case FAMILY -> {familyContacts.add(contact); break;}
                case WORK -> {workContacts.add(contact);break;}
                case FRIENDS -> {friendsContacts.add(contact);break;}
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
                List<ContactMessage> messages = messagesMap.get(Integer.parseInt(contactId));
                Platform.runLater(() -> {
                    openChatObservableList.clear();
                    for (ContactMessage message : messages) {
                        Text text = new Text(message.getContent());
                        TextFlow messageFlow = new TextFlow(text);
                        openChatObservableList.add(messageFlow);
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
        Utilities.populateGroupsList(groupsListView,groupsList);
        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedGroup) -> {
            if (selectedGroup != null) {
                Text groupID =  (Text) selectedGroup.getChildren().get(2); // extract group id
                // open group chat on demand using groupID
                // remote server request
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
        myDirector.signin();
    }
    @FXML
    void handleMessageTextField(ActionEvent event) {

    }
    @FXML
    void handleSendButton(ActionEvent event) {

    }
    @FXML
    void handleAttachmentButton(ActionEvent event) {


    }
} 