package test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class SideLoaderApp extends Application {
    private Stage primaryStage;

    public void start(Stage primaryStage) {
        if (SideLoader.getLoadedApp() == null) SideLoader.setLoadedApp(this);

        // Set up scene and stage
        this.primaryStage = primaryStage;

        // Set minimum window size
        primaryStage.setMinHeight(550);
        primaryStage.setMinWidth(900);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(true);

        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
        primaryStage.show();
    }

    void loadScene(String fxmlName) {
        // .fxml file to load
        fxmlName += fxmlName.contains(".fxml") ? "" : ".fxml";
        String fullPath = "../assets/fxml/" + fxmlName;

        if (getClass().getResource(fullPath) == null) {
            System.err.println("File " + fxmlName + " does not exist under /assets/fxml/.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fullPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1280, 720);

            primaryStage.setTitle(fxmlName);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}