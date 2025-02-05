package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdminStatisticsController {
    private Stage stage;
    private Director director;

    @FXML
    private StackPane stackPane;

    public void setStageDirector(Stage stage, Director director){
        this.director = director;
        this.stage = stage;
    }
    @FXML
    public void userStatusAction(){
        PieChart status = new PieChart();
        PieChart.Data online = new PieChart.Data("online", 50);
        PieChart.Data offline = new PieChart.Data("offline", 100);
        
        
        status.getData().addAll(online,offline);
        //online.getNode().setStyle("-fx-pie-color: #4CAF50;"); // Green
        //offline.getNode().setStyle("-fx-pie-color:rgb(21, 142, 194);"); // Red
        status.setLegendVisible(true);
        status.setLabelsVisible(true);
        status.setTitle("User State Statistics");
        
        stackPane.getChildren().clear();
        stackPane.getChildren().add(status);
    }
    @FXML
    public void genderStatusAction(){
        PieChart pieChart = new PieChart();

        PieChart.Data maleData = new PieChart.Data("Male", 60);
        PieChart.Data femaleData = new PieChart.Data("Female", 40);

        pieChart.getData().addAll(maleData, femaleData);
        
        //maleData.getNode().setStyle("-fx-pie-color: #4CAF50;"); // Green
        //femaleData.getNode().setStyle("-fx-pie-color:rgb(21, 142, 194);"); // Red
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setTitle("User State Statistics");
        stackPane.getChildren().clear();
        stackPane.getChildren().add(pieChart);
    }
    @FXML
    public void countryStatusAction(){
        // Define axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Country");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Users");

        // Create a BarChart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("User Distribution by Country");

        // Add data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Users");
        series.getData().add(new XYChart.Data<>("Egypt", 500));
        series.getData().add(new XYChart.Data<>("KSA", 300));
        series.getData().add(new XYChart.Data<>("Sudan", 400));

        barChart.getData().add(series);

        stackPane.getChildren().clear();
        stackPane.getChildren().add(barChart);
    }

}
