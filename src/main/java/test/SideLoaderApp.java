package test;

import ca.uoguelph.frontend.objects.DisplayError;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        // add popup logging
        Thread.setDefaultUncaughtExceptionHandler(DisplayError::createPopup);
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
            DisplayError.createPopup(Thread.currentThread(), e);
            DisplayError.log.error(e);
        }
    }
}