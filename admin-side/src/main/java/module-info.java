module jets.projects {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.rmi;

    opens jets.projects to javafx.fxml;
    opens jets.projects.Controllers to javafx.fxml;
    opens jets.projects.entities to javafx.base;
    exports jets.projects;
}