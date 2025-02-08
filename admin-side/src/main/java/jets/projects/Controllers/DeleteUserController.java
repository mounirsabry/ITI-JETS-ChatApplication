package jets.projects.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jets.projects.entities.NormalUser;

import java.io.ByteArrayInputStream;

public class DeleteUserController {
    private Stage stage;
    private Director director;
    private NormalUser normalUser = null;
    public void setStageDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField IDField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField birthdayField;
    @FXML
    private Button searchButton;
    @FXML
    private TextField userPhoneSearchField;
    @FXML
    private ImageView userProfileImage;
    @FXML
    public void handleSearchButton() {
        String phone = userPhoneSearchField.getText().trim();
        if(phone.isEmpty()){
            AdminAlerts.invokeWarningAlert("Search User", "invalid phone number");
            return;
        }
        normalUser = director.getUserData(phone);
        if(normalUser == null){
            return;
        }
        nameField.setText(normalUser.getDisplayName());
        emailField.setText(normalUser.getEmail());
        IDField.setText(String.valueOf(normalUser.getUserID()));
        passwordField.setText(normalUser.getPassword());
        countryField.setText(normalUser.getCountry().toString());
        genderField.setText(normalUser.getGender().toString());
        birthdayField.setText(normalUser.getBirthDate().toString());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(normalUser.getPic());
        Image image = new Image(inputStream);
        userProfileImage.setImage(image);
    }
    public void handleDeleteUserButton() {
        if (normalUser == null) {
            AdminAlerts.invokeWarningAlert("delete User Warning", "Please enter valid User phone number");
            return;
        }
        director.deleteAccount(normalUser);
        normalUser = null;
        clearFields();
    }
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        IDField.clear();
        userPhoneSearchField.clear();
        countryField.clear();
        genderField.clear();
        birthdayField.clear();
        userProfileImage.setImage(null);
    }
}
