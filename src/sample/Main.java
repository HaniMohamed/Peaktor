package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {



        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            root = loader.load();
            Controller controller = loader.getController();
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("style.css").toString());
            stage.setScene(scene);
            stage.setTitle("Peaktor");
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
            stage.setResizable(true);
            controller.start(stage);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}