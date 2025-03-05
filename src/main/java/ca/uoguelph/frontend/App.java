package ca.uoguelph.frontend;

import java.util.ArrayList;

import ca.uoguelph.backend.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen FXML from the assets folder

        //test
        // Database.loadExcelSheet(getClass().getResource("/database/UMS_Data.xlsx").getPath());
        // ArrayList<ArrayList<String>> lists = Database.loadStrings(1);
        // for(int i = 0; i < lists.size(); i++)
        // {
        //     for(String s : lists.get(i))
        //     {
        //         System.out.print(s + " ");
        //     }
        //     System.out.println("");
        // }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/login.fxml"));
        Parent root = loader.load();

        // Set up the scene and stage
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("University Management System");
        primaryStage.setScene(scene);

        // Set minimum window size
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(550);
        primaryStage.centerOnScreen();

        primaryStage.setResizable(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
