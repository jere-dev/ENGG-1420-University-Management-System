package ca.uoguelph.frontend;

import ca.uoguelph.backend.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create a simple UI
        Label label = new Label("Welcome to University Management System!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 200);

        // Set up the stage
        stage.setTitle("University Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Start JavaFX
    }
}
