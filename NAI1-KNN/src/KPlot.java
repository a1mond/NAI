import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class KPlot extends Application {
    private static String[] arguments;
    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Classifier classifier = new Classifier(arguments);

        stage.setTitle("KNN Implementation");
        final NumberAxis xAxis = new NumberAxis(0, 100, 5);
        final NumberAxis yAxis = new NumberAxis(0, 100, 10);
        final LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);
        xAxis.setLabel("K-value");
        yAxis.setLabel("Accuracy");
        lc.setTitle("K-value to Accuracy");
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Accuracy");

        for (int i = 1; i <= 100; i++) {
            classifier.setK(i);
            classifier.assignOutputSet();
            series.getData().add(new XYChart.Data<>(i, classifier.calc_mistake()));
        }

        Scene scene = new Scene(lc, 1500, 700);
        lc.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }
}
