package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class AddContactController {
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
        // save data
        if (owner != null) {
            owner.getScene().getRoot().setEffect(null); // Remove blur effect
        }
        if (popupStage != null) {
            popupStage.close();
        }
    }
    @FXML
    void handleCustomCategory(ActionEvent event) {
        //save choice
    }
    
}
