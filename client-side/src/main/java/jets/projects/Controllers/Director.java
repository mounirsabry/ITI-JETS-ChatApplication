package jets.projects.Controllers;

import javafx.stage.Stage;

public class Director {
    private final Stage stage;
    
    public Director(Stage stage) {
        this.stage = stage;
    }
    
    public void startWorking() {
        stage.show();
    }
}
