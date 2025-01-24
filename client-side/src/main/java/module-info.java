module jets.projects {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens jets.projects to javafx.fxml;
    opens jets.projects.fxmlcontrollers to javafx.fxml;
    exports jets.projects;
}