package ca.uoguelph.frontend;

import java.util.ArrayList;

import ca.uoguelph.backend.CourseManager;
import ca.uoguelph.backend.Database;
import ca.uoguelph.backend.EventManager;
import ca.uoguelph.backend.ProfManager;
import ca.uoguelph.backend.StudentManager;
import ca.uoguelph.backend.SubjectManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen FXML from the assets folder

        //the order matters pls dont change
        Database.loadExcelSheet(getClass().getResource("/database/UMS_Data.xlsx").getPath());
        SubjectManager.loadSubjects();
        CourseManager.loadCourses();
        ProfManager.LoadProfs();
        StudentManager.loadStudents();
        EventManager.loadEvents();

        Image icon = new Image(getClass().getResourceAsStream("/assets/images/unilogoIcon.png"));
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
        primaryStage.getIcons().add(icon);

        primaryStage.setResizable(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
