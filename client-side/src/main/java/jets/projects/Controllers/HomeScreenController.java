package jets.projects.Controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jets.projects.Director;
import javafx.scene.input.MouseEvent;
import jets.projects.entities.ContactMessage;
import jets.projects.entities.Group;
import jets.projects.entity_info.ContactInfo;
import jets.projects.entity_info.ContactMessagesInfo;

public class HomeScreenController {

    @FXML
    private GridPane mainContainer;
    @FXML
    private ImageView addContactIcon;
    @FXML
    private ImageView addGroupIcon;
    @FXML
    private TextField searchTextField;
    @FXML
    private Circle myprofilepicture;
    @FXML
    private Circle mystatus;
    @FXML
    private Button announcementButton;
    @FXML
    private Button contactButton;
    @FXML
    private Button groupButton;
    @FXML
    private Button notificationButton;
    @FXML
    private Button friendRequestButton;
    @FXML
    private ImageView friendRequestIcon;
    @FXML
    private Button settingsButton;
    @FXML
    private Button logoutButton;
    @FXML
    private BorderPane openchatContainer;
    @FXML
    private ListView<TextFlow> openChatList;
    private final ObservableList<TextFlow> openChatObservablList = FXCollections.observableArrayList() ;
    @FXML
    private Circle chatProfile;
    @FXML
    private VBox currentUsernameContainer;
    @FXML
    private TextField messageTextField;
    @FXML
    private Button editButton;
    @FXML
    private Button sendButton;
    @FXML
    private Button attachmentsButton;
    private Stage stage;
    private Director myDirector;
    private List<ContactInfo> contactsList = new ArrayList<>();
    private  Map<Integer , ContactMessagesInfo> messagesInfoMap=new HashMap<>();
    private List<Group> groupsList = new ArrayList<>();

    public void setDirector(Stage stage, Director myDirector) {
        this.stage = stage;
        this.myDirector = myDirector;
    }
    public void perform(List<ContactInfo> contactsList, Map<Integer, ContactMessagesInfo> messagesInfoMap,
                        List<Group> groupsList) {
        Image userprofile = new Image(getClass().getResource("/images/profile.png").toExternalForm());
        Image myprofile = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
        chatProfile.setFill(new ImagePattern(userprofile));
        myprofilepicture.setFill(new ImagePattern(myprofile));
        openChatList.setItems(openChatObservablList);
        this.contactsList = contactsList;
        this.messagesInfoMap = messagesInfoMap;
        this.groupsList = groupsList;
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
    void handleAttachmentButton(ActionEvent event) {
        

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
       Utilities.populateTree(familyItem,familyContacts , messagesInfoMap);
       Utilities.populateTree(friendsItem,friendsContacts , messagesInfoMap);
       Utilities.populateTree(workItem,workContacts , messagesInfoMap);

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
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            if (newItem != null) {
                String contactId = newItem.getValue();
                List<ContactMessage> messages = messagesInfoMap.get(Integer.parseInt(contactId)).getMessages();
                Platform.runLater(() -> {
                    openChatObservablList.clear();
                    for (ContactMessage message : messages) {
                        Text text = new Text(message.getContent());
                        TextFlow messageFlow = new TextFlow(text);
                        openChatObservablList.add(messageFlow);
                    }
                });
                System.out.println(messagesInfoMap.get(Integer.parseInt(contactId)).getMessages());
            }
        });
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
        populateGroupsList(groupsListView,groupsList);
        groupsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedGroup) -> {
            if (selectedGroup != null) {
                // extract group id
                Text groupID =  (Text) selectedGroup.getChildren().get(2);

                // open group chat on demand using extracted groupID
            }
        });


    }
    @FXML
    void handleAnnouncemnetButton(ActionEvent event){
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/announcements.fxml");
        Utilities.showPopup(owner, fxmlURL, 620, 420);

    }
    @FXML
    void handleLogOutButton(ActionEvent event) throws IOException {
        myDirector.signin();
    }

    @FXML
    void handleMessageTextField(ActionEvent event) {

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
    void handleSeacrchTextField(ActionEvent event) {

    }

    @FXML
    void handleSendButton(ActionEvent event) {

    }

    @FXML
    void handleSettingsButton(ActionEvent event) throws IOException {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/settings.fxml");
        Utilities.showPopup(owner, fxmlURL, 600, 400);

    }
    // for testing purposes only
    private void populateGroupsList(ListView<HBox> groupListView , List<Group> groupsList) {
        for(Group group : groupsList){
            HBox groupHbox = new HBox(10);
            ImageView groupPic = new ImageView();
            if (group.getPic() != null){
                groupPic.setImage(new Image(new ByteArrayInputStream(group.getPic())));
            } else {
                groupPic.setImage(new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm()));
            }
            groupPic.setFitWidth(25);
            groupPic.setFitHeight(25);
            Text groupname = new Text(group.getGroupName());
            Text groupID = new Text(String.valueOf(group.getGroupID()));
            groupID.setVisible(false);
            groupHbox.getChildren().addAll(groupPic,groupname,groupID);
            groupListView.getItems().add(groupHbox);
        }

    }
} 