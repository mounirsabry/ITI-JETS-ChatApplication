package jets.projects.Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class groupInfoController {        
        @FXML
        private Circle groupIcon;
    
        @FXML
        private Label groupName;
    
        @FXML
        private Label description;
    
        @FXML
        private Button addmember;
    
        @FXML
        private Button deleteGroup;
    
        @FXML
        private Button leaveGroup;
    
        @FXML
        private ListView<HBox> membersList;

        @FXML
        void initialize(){
            // set custom list items
            membersList.setCellFactory(lv->jets.projects.Utilities.createCustomCell());
            populateChatListWithDummyData(membersList);
        }
        @FXML
        void handleAddMember(ActionEvent event) {
            
    
        }
    
        @FXML
        void handleDeleteGroup(ActionEvent event) {
    
        }
    
        @FXML
        void handleLeaveGroup(ActionEvent event) {
    
        }

        // for testing purposes only
        private void populateChatListWithDummyData(ListView<HBox> chatsListView) {
            HBox card = new HBox(10);

            // create list item 
            Circle profile = new Circle();
            profile.setFill(new ImagePattern(new Image(getClass().getResource("/images/blank-profile.png").toExternalForm())));
            Text name = new Text("Salma ElKhatib");
            profile.setRadius(20);

            //add nodes to list item
            card.getChildren().addAll(profile,name);
            //add item to list
            chatsListView.getItems().add(card);    
        }
}
