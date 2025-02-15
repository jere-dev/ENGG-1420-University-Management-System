package ca.uoguelph.frontend;

import ca.uoguelph.backend.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create a simple UI
        try {
            String path = getClass().getResource("").getPath();
System.out.println("Absolute Path: " + path);
            Parent root = FXMLLoader.load(getClass().getResource("/assets/fxml/Main.fxml"));
            Scene scene = new Scene(root, 400, 200);
    
            // Set up the stage
            stage.setTitle("University Management System");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Start JavaFX
    }
}
