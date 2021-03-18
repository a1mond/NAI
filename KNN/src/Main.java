import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;

public class Main extends Application {
    private static String[] arguments;
    public static void main(String[] args) {
        arguments = args;
        launch(args);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage stage) throws Exception {
        Classifier classifier = new Classifier(arguments);
        if (arguments[3].equals("splot")) {
            classifier.assignOutputSet();
            HashSet<Iris> output_set = classifier.getOutputSet();
            stage.setTitle("KNN Implementation");
            final NumberAxis xAxis = new NumberAxis(2, 4, 0.2);
            final NumberAxis yAxis = new NumberAxis(0, 3, 0.2);
            final ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
            xAxis.setLabel("Sepal Width");
            yAxis.setLabel("Petal Width");
            sc.setTitle("Iris Classification Chart");

            XYChart.Series<Number, Number> iris_s = new XYChart.Series<>();
            iris_s.setName("Iris-setosa");
            XYChart.Series<Number, Number> iris_v = new XYChart.Series<>();
            iris_v.setName("Iris-versicolor");
            XYChart.Series<Number, Number> iris_vg = new XYChart.Series<>();
            iris_vg.setName("Iris-virginica");

            for (Iris i : output_set) {
                addToCharts(iris_s, iris_v, iris_vg, i);
            }
            sc.getData().addAll(iris_s, iris_v, iris_vg);
            Scene scene = new Scene(new Group());
            final VBox vBox = new VBox();
            final HBox hBox = new HBox();

            final Button add = new Button("Add Iris");

            final TextField x1 = new TextField();
            x1.setPromptText("Sepal length");
            final TextField x2 = new TextField();
            x2.setPromptText("Sepal width");
            final TextField x3 = new TextField();
            x3.setPromptText("Petal length");
            final TextField x4 = new TextField();
            x4.setPromptText("Petal width");

            add.setOnAction((ActionEvent e) -> {
                Iris i = classifier.classify(new Iris(
                        Double.parseDouble(x1.getText()),
                        Double.parseDouble(x2.getText()),
                        Double.parseDouble(x3.getText()),
                        Double.parseDouble(x4.getText())
                ));
                Iris cl_i = classifier.classify(i);
                addToCharts(iris_s, iris_v, iris_vg, cl_i);
            });

            hBox.setSpacing(10);
            hBox.getChildren().addAll(x1, x2, x3, x4, add);

            hBox.setPadding(new Insets(10, 10, 10, 50));
            vBox.getChildren().addAll(sc, hBox);

            ((Group) scene.getRoot()).getChildren().add(vBox);

            stage.setScene(scene);
            stage.show();
        } else if (arguments[3].equals("kplot")) {
            stage.setTitle("KNN Implementation");
            final NumberAxis xAxis = new NumberAxis(1, 100, 1);
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


            Scene scene = new Scene(lc, 500, 500);
            lc.getData().add(series);

            stage.setScene(scene);
            stage.show();
        }
    }

    private void addToCharts(XYChart.Series<Number, Number> iris_s, XYChart.Series<Number, Number> iris_v, XYChart.Series<Number, Number> iris_vg, Iris cl_i) {
        switch (cl_i.getName()) {
            case "Iris-setosa" -> iris_s.getData().add(new XYChart.Data<>(cl_i.getX2(), cl_i.getX4()));
            case "Iris-versicolor" -> iris_v.getData().add(new XYChart.Data<>(cl_i.getX2(), cl_i.getX4()));
            case "Iris-virginica" -> iris_vg.getData().add(new XYChart.Data<>(cl_i.getX2(), cl_i.getX4()));
        }
    }
}
