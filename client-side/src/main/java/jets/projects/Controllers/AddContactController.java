package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jets.projects.Services.Request.ClientInvitationService;
import jets.projects.entities.ContactGroup;
import javafx.event.ActionEvent;

public class AddContactController {

   
   
    
@FXML
    private Button addContactButton;
    @FXML
    private TextField phoneField;


    @FXML
    private ComboBox<String> categoryComboBox;
    
    private Stage owner; 
    private Stage popupStage; 
   

    @FXML
    private void initialize(){
        categoryComboBox.getItems().addAll("Family", "Friends", "Work", "Other");
    }
    @FXML
    void handleAddContact(ActionEvent event) {

        String category = categoryComboBox.getValue().toUpperCase();
        String phoneNumber = phoneField.getText(); 
        
        if (category == null || phoneNumber.isEmpty()) {
            ClientAlerts.invokeWarningAlert("Add Contact", "Please enter a phone number and select a category.");
            return;
        }
        
        
        ClientInvitationService invitationService = new ClientInvitationService();
        boolean success = invitationService.sendContactInvitation(phoneNumber,ContactGroup.valueOf(category) );


        
        if (success) {
           ClientAlerts.invokeInformationAlert("Add Contact", "Sent Contact Invitation Successfully");
        }


    }


  
}
