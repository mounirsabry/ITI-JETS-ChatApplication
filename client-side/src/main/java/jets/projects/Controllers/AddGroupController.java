package jets.projects.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jets.projects.ServiceManager;
import jets.projects.Services.Request.ClientGroupService;
import jets.projects.entities.Group;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import datastore.DataCenter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import jets.projects.entities.GroupMember;
import jets.projects.entity_info.GroupMemberInfo;

public class AddGroupController {
    private byte[] selectedImageBytes = null;

    @FXML
    private Button addContactButton;

    @FXML
    private VBox groupIcon;
    @FXML
    private Circle groupPicture;
    @FXML
    private TextField groupName;
    @FXML
    private TextField groupDesc;
    @FXML
    private VBox groupMembers;  
    @FXML
    private VBox addedMembersVbox;
    @FXML
    private Button chooseGroupIconButton;

    @FXML
    private TextField addmemberTextField;
    private List<String> addedMembersList = new ArrayList<>();

    @FXML
    private Button addmemberButton;
    private Stage owner; 
    private Stage popupStage; 

    @FXML
    private void initialize(){
        Image blankGroupPicture = new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm());
        groupPicture.setFill(new ImagePattern(blankGroupPicture));
    }
    public void setOriginalStage(Stage stage) {
        this.owner = stage;
    }

    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }
    @FXML
    void handleAddGroup(ActionEvent event) {
        if (groupName.getText().isBlank()) {
            ClientAlerts.invokeInformationAlert("Required", "Group name cannot be empty.");
            return;
        }
        String groupNameInput = groupName.getText().trim();
        String groupDescInput = groupDesc.getText().trim();
        
        ClientGroupService clientgroupservice = new ClientGroupService();
        Group newGroup = new Group();
        int loggedinUserID = ServiceManager.getInstance().getClientToken().getUserID();
        newGroup.setGroupAdminID(loggedinUserID);
 
        newGroup.setGroupName(groupNameInput);
        newGroup.setGroupDesc(groupDescInput);
        newGroup.setPic(selectedImageBytes);
        
        int groupID = clientgroupservice.createGroup(newGroup);
        if(groupID > 0){
            ClientAlerts.invokeInformationAlert("Create Group" , 
                    "Created Group Successfully");
            newGroup.setGroupID(groupID);

            DataCenter.getInstance().getGroupInfoMap().put(
                    groupID, newGroup);
            DataCenter.getInstance().getGroupMembersMap().put(groupID, 
                    FXCollections.synchronizedObservableList(FXCollections.observableArrayList(new ArrayList<>())));
            GroupMember groupMember = new GroupMember(groupID, loggedinUserID);
            DataCenter.getInstance().getGroupMembersMap().get(groupID).add(
              new GroupMemberInfo(groupMember, DataCenter.getInstance().getMyProfile().getDisplayName()
                      ,DataCenter.getInstance().getMyProfile().getPic()));
            DataCenter.getInstance().getGroupMessagesMap().put(groupID
            ,FXCollections.synchronizedObservableList(FXCollections.observableArrayList(new ArrayList<>())));
            DataCenter.getInstance().getGroupList().add(newGroup);
        } else {
            ClientAlerts.invokeErrorAlert("Create Group" , "Failed to create group");
        }
    }

    @FXML
    void handleAddMember(ActionEvent event) {
        // select members from contacts list //
        String member = addmemberTextField.getText();
        if (!member.isEmpty() && !addedMembersList.contains(member)) {
            addedMembersList.add(member);
            Label memberLabel = new Label(member);
            Button removeButton = new Button("");
            removeButton.setStyle("-fx-background-color: transparent;");
            removeButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/minus.png"))));
            
            HBox newMemberHbox = new HBox();
            newMemberHbox.getChildren().addAll(memberLabel,removeButton);

            removeButton.setOnAction(e -> {
                addedMembersList.remove(member);
                addedMembersVbox.getChildren().remove(newMemberHbox);
            });
            addedMembersVbox.getChildren().addAll(newMemberHbox);
            addmemberTextField.clear(); 
        }
    }

    @FXML
    void handleChooseGroupIcon(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog((Stage) chooseGroupIconButton.getScene().getWindow());

        if (selectedFile != null) {
            try {

                selectedImageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                Image image = new Image(selectedFile.toURI().toString());
                groupPicture.setFill(new ImagePattern(image));
            } catch (Exception e) {
                System.out.println("Could not load the image: " + e.getMessage());
            }
        } else {
            System.out.println("No file selected.");
        }
    }

}