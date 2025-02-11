package jets.projects.Controllers;
import datastore.DataCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import jets.projects.Services.Request.ClientGroupService;
import jets.projects.entities.Group;
import jets.projects.entity_info.GroupMemberInfo;

import java.io.ByteArrayInputStream;
import java.util.*;

public class GroupInfoController {
        @FXML
        private Circle groupIcon;
        @FXML
        private Label groupName;
        @FXML
        private ComboBox<HBox> contactsCombobox;
        @FXML
        private Label description;
        @FXML
        private ListView<HBox> membersList;
        private final ObservableList<HBox> memberInfoObservableList = FXCollections.observableArrayList();
        private final ObservableList<HBox> contactInfoObservableList = FXCollections.observableArrayList();
        private final Map<String, HBox> contactMap = new HashMap<>();
        private int groupId;
        List<GroupMemberInfo> members = new ArrayList<>();
        Group group = null;
        int groupAdminID;
        int loggedInUserID;
        private ClientGroupService groupService = null;

    @FXML
    void initialize(){
        membersList.setItems(memberInfoObservableList);
        groupService = new ClientGroupService();
        contactsCombobox.setItems(contactInfoObservableList);
        contactsCombobox.setEditable(true);
        loadContacts();
        addSearchFeature();
    }
    void setData(int groupId) {
        this.groupId = groupId;
        members = DataCenter.getInstance().getGroupMembersMap().get(groupId);
        group = DataCenter.getInstance().getGroupInfoMap().get(groupId);
        groupAdminID= DataCenter.getInstance().getGroupInfoMap().get(groupId).getGroupAdminID();
        loggedInUserID = DataCenter.getInstance().getMyProfile().getUserID();
        if(group.getPic()!= null){
            groupIcon.setFill(new ImagePattern(new Image(new ByteArrayInputStream(group.getPic()))));
        }else{
            groupIcon.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-group-picture.png").toExternalForm())));
        }
        groupName.setText(group.getGroupName());
        description.setText(group.getGroupDesc());
        memberInfoObservableList.addAll(members.stream().map(member -> {
            HBox card = new HBox(10);
            Circle profile = new Circle(20);
            if(member.getPic()!= null){
                profile.setFill(new ImagePattern(new Image(new ByteArrayInputStream(member.getPic()))));
            }else{
                profile.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
            }
            Text name = new Text(member.getName());
            Button removeButton = new Button("Remove");
            removeButton.getStylesheets().add(getClass().getResource("/styles/mainStyles.css").toExternalForm());
            removeButton.getStyleClass().add("primaryButton");
            removeButton.setOnAction(e -> {
                boolean removed = groupService.removeMemberFromGroup(groupId, member.getMember().getMemberID());
                if(!removed)  ClientAlerts.invokeErrorAlert("Error", "Failed to remove member from group");
                membersList.getItems().remove(card);
                memberInfoObservableList.remove(card);
                if (memberInfoObservableList.isEmpty()) {
                    contactsCombobox.setVisible(false);
                }
            });
            if(groupAdminID != loggedInUserID)     removeButton.setVisible(false);
            card.getChildren().addAll(profile, name , removeButton);
            return card;
        }).toList());
    }
    @FXML
    // add member to an existing group
    void handleAddMember(ActionEvent event) {
        contactsCombobox.setVisible(true);
        // Handle member selection
        contactsCombobox.setOnAction(e -> {
            HBox selected = contactsCombobox.getSelectionModel().getSelectedItem();
            if (selected != null && !memberInfoObservableList.contains(selected)) {
                memberInfoObservableList.add(selected);
                Text contactIdText = (Text)selected.getChildren().get(2);   // extract contactId
                int contactId = Integer.parseInt(contactIdText.getText());
                boolean added = groupService.addMemberToGroup(groupId, contactId);
                if(!added)  ClientAlerts.invokeErrorAlert("Error", "Failed to add member to group");
                contactsCombobox.getSelectionModel().clearSelection();
                contactsCombobox.getEditor().clear();
            }
        });
    }
    @FXML
    void handleDeleteGroup(ActionEvent event) {
        boolean deleted = groupService.deleteGroup(groupId);
        if(!deleted)  ClientAlerts.invokeErrorAlert("Error", "Failed to delete group");
    }
    @FXML
    void handleLeaveGroup(ActionEvent event) {
        boolean leftGroup = false;
        if (loggedInUserID != groupAdminID) {
            leftGroup = groupService.leaveGroupAsMember(groupId);
            DataCenter.getInstance().getGroupMembersMap().get(groupId).removeIf(member -> member.getMember().getMemberID() == loggedInUserID);
        } else {
            // Pick a random new admin from group members
            if (members != null && members.size() > 1) {
                int randomIndex;
                do {
                    randomIndex = (int) (Math.random() * members.size());
                } while (members.get(randomIndex).getMember().getMemberID() == loggedInUserID); // Ensure new admin is not the current admin
                int newAdminID = members.get(randomIndex).getMember().getMemberID();
                leftGroup = groupService.leaveGroupAsAdmin(groupId, newAdminID);

            } else {
                ClientAlerts.invokeWarningAlert("Error", "No other members available to assign as admin");
                groupService.deleteGroup(groupId);
            }
        }
        if (!leftGroup) {
            ClientAlerts.invokeErrorAlert("Error", "Failed to assign a new group admin");
        }
    }
    private void loadContacts() {
        DataCenter.getInstance().getContactList().forEach(contact -> {
            Circle profile = new Circle(20);
            if (contact.getPic() != null) {
                profile.setFill(new ImagePattern(new Image(new ByteArrayInputStream(contact.getPic()))));
            } else {
                profile.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
            }
            Text nameText = new Text(contact.getName());
            Text contactID = new Text(String.valueOf(contact.getContact().getSecondID()));
            contactID.setVisible(false);
            HBox contactItem = new HBox(10, profile, nameText ,contactID);
            contactInfoObservableList.add(contactItem);
            contactMap.put(contact.getName().toLowerCase(), contactItem);
        });
    }
    private void addSearchFeature() {
        contactsCombobox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            ObservableList<HBox> filteredList = FXCollections.observableArrayList();
            for (String name : contactMap.keySet()) {
                if (name.contains(newValue.toLowerCase())) {
                    filteredList.add(contactMap.get(name));
                }
            }
            contactsCombobox.setItems(filteredList.isEmpty() ? contactInfoObservableList : filteredList);
            if (!filteredList.isEmpty()) contactsCombobox.show();
        });
        // Restore full list on focus lost
        contactsCombobox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) contactsCombobox.setItems(contactInfoObservableList);
        });
    }
}
