module jets.projects {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    exports jets.projects;
    exports jets.projects.Controllers;    
    opens jets.projects to javafx.fxml;
    opens jets.projects.Controllers to javafx.fxml;
}