package jets.projects;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        
        Parent root = null;
        URL fxmlURL = getClass().getResource("/fxml/signin.fxml"); 
        
        if (fxmlURL != null) {
            try  {
                root = FXMLLoader.load(fxmlURL);
            } catch (IOException e) {
                System.out.println("Could not load the fxml file.");
                e.printStackTrace();
            }
        } else {
            System.out.println("FXML file not found at: " + fxmlURL);
        }
        
        if (root != null) {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } else {
            System.out.println("FXML loading failed.");
        }
    }
    // @Override
    // public void start(Stage stage) {
    //     Director myDirector = new Director(stage);
    //     myDirector.startWorking();
    // }

    public static void main(String[] args) {
        Application.launch(args);
    }

}