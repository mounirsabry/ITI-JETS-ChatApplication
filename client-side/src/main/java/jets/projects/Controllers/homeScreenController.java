package jets.projects.Controllers;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
import javafx.scene.input.MouseEvent;

public class homeScreenController {

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
    private ListView<HBox> openChatList;
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

    @FXML
    void initialize(){
        Image userprofile = new Image(getClass().getResource("/images/profile.png").toExternalForm());
        Image myprofile = new Image(getClass().getResource("/images/blank-profile.png").toExternalForm());
        chatProfile.setFill(new ImagePattern(userprofile));
        myprofilepicture.setFill(new ImagePattern(myprofile));
    }
    @FXML
    void handleChatProfile(MouseEvent event){
        //if currently open chat is normal chat show user profile
        //else show group info
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/groupInfo.fxml");
        jets.projects.Utilities.showPopup(owner, fxmlURL, 600, 400);        
    }
    @FXML
    void handleEditProfileButton(ActionEvent event) {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/editProfile.fxml");
        jets.projects.Utilities.showPopup(owner, fxmlURL, 600, 400);
    }
    @FXML
    void handleAddContact(ActionEvent event) {
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/addContact.fxml");
        jets.projects.Utilities.showPopup(owner, fxmlURL, 600, 400);
    }
    @FXML
    void handleAddGroup(ActionEvent event) {   
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/addGroup.fxml");
        jets.projects.Utilities.showPopup(owner, fxmlURL, 600, 400);
    }

    @FXML
    void handleAttachmentButton(ActionEvent event) {
        

    }
    @FXML
    void handleContactButton(ActionEvent event) {
        TreeView<String> contactsList = new TreeView<>();
        contactsList.getStylesheets().add(getClass().getResource("/styles/homeScreenStyles.css").toExternalForm());
        contactsList.getStyleClass().add("chatsList");

        // creating contacts categories
        TreeItem<String> rootItem = new TreeItem<>("Contacts");
        TreeItem<String> familyItem = new TreeItem<>("Family");
        TreeItem<String> friendsItem = new TreeItem<>("Friends");
        TreeItem<String> workItem = new TreeItem<>("Work");
        
        //add dummy data
        Node profile = new ImageView(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm()));
        ((ImageView) profile).setFitWidth(25);  
        ((ImageView) profile).setFitHeight(25);
        familyItem.getChildren().addAll(Arrays.asList(
            new TreeItem<>("Alice",profile)
        ));
        friendsItem.getChildren().addAll(Arrays.asList(
            new TreeItem<>("John",profile),
            new TreeItem<>("Doe",profile),
            new TreeItem<>("Bob",profile)
        ));
        workItem.getChildren().addAll(Arrays.asList(
            new TreeItem<>("Manager",profile),
            new TreeItem<>("Team Lead",profile)
        ));

        familyItem.setExpanded(true);
        friendsItem.setExpanded(true);
        workItem.setExpanded(true);  

        contactsList.setRoot(rootItem); 
        contactsList.setShowRoot(false);
        contactsList.getRoot().getChildren().addAll(Arrays.asList(familyItem, friendsItem, workItem));

         // Set cell factory to apply styles
        contactsList.setCellFactory(treeView -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    TreeItem<String> currentItem = getTreeItem();
                    if (currentItem != null && currentItem.getGraphic() != null) {
                        setGraphic(currentItem.getGraphic()); 
                    } else {
                        setGraphic(null);
                    }
                    if (currentItem == familyItem || currentItem == workItem || currentItem == friendsItem) {
                        getStyleClass().add("subtitle");
                    } else {
                        getStyleClass().remove("subtitle"); 
                    }
                }
            }
        });
        Node existingNode = jets.projects.Utilities.getExistingNode(mainContainer, 1, 2);
        if (existingNode != null) {
            mainContainer.getChildren().remove(existingNode);
            mainContainer.add(contactsList, 1, 2);
            GridPane.setRowSpan(contactsList , GridPane.REMAINING);
            contactsList.minWidth(200);
        }
    }
    @FXML
    void handleFriendRequestsButton(ActionEvent event){
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/friendRequests.fxml");
        jets.projects.Utilities.showPopup(owner, fxmlURL, 620, 420);
    }

    @FXML
    void handleGroupButton(ActionEvent event) {
        ListView<HBox> chatsListView = new ListView<HBox>();
        chatsListView.getStylesheets().add(getClass().getResource("/styles/homeScreenStyles.css").toExternalForm());
        chatsListView.getStyleClass().add("chatsList");
        // set custom list items
        chatsListView.setCellFactory(lv->jets.projects.Utilities.createCustomCell());

       Node existingNode = jets.projects.Utilities.getExistingNode(mainContainer, 1, 2);
        if (existingNode != null) {
            mainContainer.getChildren().remove(existingNode);
            mainContainer.add(chatsListView, 1, 2);
            GridPane.setRowSpan(chatsListView , GridPane.REMAINING);
            chatsListView.minWidth(200);
        }
        populateChatListWithDummyData(chatsListView);
    }
    @FXML
    void handleAnnouncemnetButton(ActionEvent event){
        Node currentNode = (Node)event.getSource();
        Stage owner = (Stage)currentNode.getScene().getWindow();
        // load the popup content
        URL fxmlURL = getClass().getResource("/fxml/announcements.fxml");
        jets.projects.Utilities.showPopup(owner, fxmlURL, 620, 420);

    }
    @FXML
    void handleLogOutButton(ActionEvent event) throws IOException {
        // Node currentNode = (Node)event.getSource();
        // Stage stage = (Stage)currentNode.getScene().getWindow();
        // Parent root = FXMLLoader.load(getClass().getResource("/fxml/signin.fxml"));
        // Scene scene = new Scene(root,stage.getWidth(),stage.getHeight());
        // stage.setScene(scene);
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
        jets.projects.Utilities.showPopup(owner, fxmlURL, 620, 420);
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
        jets.projects.Utilities.showPopup(owner, fxmlURL, 600, 400);

    }

    // for testing purposes only
    private void populateChatListWithDummyData(ListView<HBox> chatsListView) {
        HBox card = new HBox();

        // create list item 
        ImageView profile = new ImageView(getClass().getResource("/images/profile.png").toExternalForm());
        Text name = new Text("Salma ElKhatib");

        // inline styling
        profile.setFitWidth(30);
        profile.setFitHeight(30);
        HBox.setMargin(profile, new Insets(0, 10, 0, 0));  
        profile.setStyle("-fx-background-color: #003249; -fx-background-radius: 50%; -fx-border-radius: 50%; -fx-padding: 2; -fx-border-width: 2; -fx-border-color: #ffffff;");
        card.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0.5, 0, 2); -fx-margin: 10; -fx-pref-width: 270px;");
        name.setStyle("-fx-font-weight: bold; -fx-fill: #007ea7; -fx-font-size: 12px;");

        //add nodes to list item
        card.getChildren().addAll(profile,name);
        //add item to list
        chatsListView.getItems().add(card);    
    }
} 