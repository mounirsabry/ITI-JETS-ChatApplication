package jets.projects.Controllers;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jets.projects.entities.Country;

import java.util.List;
import java.util.Map;

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
        List<Integer> stats = director.getOnlineOfflineStats();
        if(stats == null){
            return;
        }
        PieChart status = new PieChart();
        PieChart.Data online = new PieChart.Data("online", stats.get(0));
        PieChart.Data offline = new PieChart.Data("offline", stats.get(1));

        status.getData().addAll(online,offline);
        status.setLegendVisible(true);
        status.setLabelsVisible(true);
        status.setTitle("User State Statistics");
        
        stackPane.getChildren().clear();
        stackPane.getChildren().add(status);
    }
    @FXML
    public void genderStatusAction(){
        List<Integer> stats = director.getMaleFemaleStats();
        if(stats == null){
            return;
        }
        PieChart pieChart = new PieChart();

        PieChart.Data maleData = new PieChart.Data("Male", stats.get(0));
        PieChart.Data femaleData = new PieChart.Data("Female", stats.get(1));

        pieChart.getData().addAll(maleData, femaleData);

        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setTitle("User State Statistics");
        stackPane.getChildren().clear();
        stackPane.getChildren().add(pieChart);
    }
    @FXML
    public void countryStatusAction(){
        Map<Country, Integer> map = director.getTopCountries();
        if(map == null){
            return;
        }
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
        for(Map.Entry<Country, Integer> entry : map.entrySet()){
            series.getData().add(new XYChart.Data<>(entry.getKey().name(), entry.getValue()));
        }

        barChart.getData().add(series);
        barChart.setLegendVisible(true);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(barChart);
    }

}
