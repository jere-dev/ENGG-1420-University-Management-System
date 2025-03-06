package ca.uoguelph.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class SideLoaderApp extends Application {
    public static String fxmlPath = "subject_catalog_user"; // <-- PAGE HERE

    public void start(Stage primaryStage) throws Exception {
        // .fxml file to load
        String fullPath = "/assets/fxml/" + fxmlPath + ".fxml";
        if (!new File(fullPath).exists()) System.out.println(fullPath + " exists");
        else {
            System.out.println(fullPath + " does not exist");
            System.exit(1);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fullPath));
        Parent root = loader.load();

        // Set up scene and stage
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle(fxmlPath);
        primaryStage.setScene(scene);

        // Set minimum window size
        primaryStage.setMinHeight(550);
        primaryStage.setMinWidth(900);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(true);

        primaryStage.show();
    }

    public static void main(String[] args) {launch(args);}
}