module jets.projects {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    opens jets.projects to javafx.fxml;
    opens jets.projects.Controllers to javafx.fxml;
    exports jets.projects;
}