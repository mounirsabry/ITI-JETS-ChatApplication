package jets.projects.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jets.projects.entities.Country;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.util.Date;

public class UpdateUserDataController {
    @FXML
    public void initialize() {
        countryComboBox.getItems().addAll(Country.values());
        genderComboBox.getItems().addAll(Gender.values());
    }

    private Stage stage;
    private Director director;
    private NormalUser normalUser;
    public void setStageDirector(Stage stage, Director director) {
        this.stage = stage;
        this.director = director;
    }
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Gender> genderComboBox;
    @FXML
    private DatePicker DOBField;
    @FXML
    private TextField IDField;
    @FXML
    private TextField userPhoneSearchField;
    @FXML
    private ImageView userProfileImage;
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
        countryComboBox.setValue(normalUser.getCountry());
        genderComboBox.setValue(normalUser.getGender());
        DOBField.setValue(normalUser.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(normalUser.getPic());
        Image image = new Image(inputStream);
        userProfileImage.setImage(image);
    }
    @FXML
    public void handleUpdateUserDataButton() {
        if (normalUser == null) {
            AdminAlerts.invokeWarningAlert("Update User Warning", "Please enter valid User phone number");
            return;
        }
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        Country country = countryComboBox.getValue();
        Date DOB = DOBField.getValue() == null ? null : Date.from(DOBField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Gender gender = genderComboBox.getValue();
        String ID = IDField.getText().trim();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || country == null || gender == null || ID.isEmpty() || DOB == null) {
            AdminAlerts.invokeErrorAlert("Update User Data", "All fields are required");
            return;
        }
        normalUser.setDisplayName(name);
        normalUser.setEmail(email);
        normalUser.setPassword(password);
        normalUser.setCountry(country);
        normalUser.setBirthDate(DOB);
        normalUser.setGender(gender);
        director.updateUserData(normalUser);
        normalUser = null;
        clearFields();
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        IDField.clear();
        userPhoneSearchField.clear();
        countryComboBox.setValue(null);
        genderComboBox.setValue(null);
        DOBField.setValue(null);
        userProfileImage.setImage(null);
    }
}

