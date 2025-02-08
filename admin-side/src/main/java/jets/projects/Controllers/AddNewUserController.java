package jets.projects.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jets.projects.entities.Country;
import jets.projects.entities.Gender;
import jets.projects.entities.NormalUser;

import java.time.ZoneId;
import java.util.Date;


public class AddNewUserController {
    @FXML
    public void initialize() {
        countryComboBox.getItems().addAll(Country.values());
        genderComboBox.getItems().addAll(Gender.values());
    }

    Stage stage;
    Director director;
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
    private TextField phoneField;
    @FXML
    private Button addNewUserButton;

    public void handleAddNewUserButton() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        Country country = countryComboBox.getValue();
        Gender gender = genderComboBox.getValue();
        Date DOB = DOBField.getValue() == null? null:Date.from(DOBField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String phone = phoneField.getText().trim();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || country == null || gender == null || DOB == null || phone.isEmpty()){
            AdminAlerts.invokeErrorAlert("Add new user", "All fields are required");
            return;
        }
        NormalUser normalUser = new NormalUser();
        normalUser.setDisplayName(name);
        normalUser.setEmail(email);
        normalUser.setPassword(password);
        normalUser.setCountry(country);
        normalUser.setBirthDate(DOB);
        normalUser.setGender(gender);
        normalUser.setPhoneNumber(phone);
        director.createAccount(normalUser);
    }

}
