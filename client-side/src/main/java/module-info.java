module jets.projects {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.rmi;
    requires jakarta.xml.bind;
    requires java.desktop;

    exports jets.projects;
    exports jets.projects.Controllers;    
    opens jets.projects to javafx.fxml;
    opens jets.projects.Controllers to javafx.fxml;
    opens jets.projects.session_saving to jakarta.xml.bind;
    exports jets.projects.api;
}