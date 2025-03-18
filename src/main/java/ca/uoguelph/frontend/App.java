package ca.uoguelph.frontend;

import java.util.ArrayList;

import ca.uoguelph.backend.Admin;
import ca.uoguelph.backend.CourseManager;
import ca.uoguelph.backend.Database;
import ca.uoguelph.backend.EventManager;
import ca.uoguelph.backend.Faculty;
import ca.uoguelph.backend.FacultyManager;
import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.StudentManager;
import ca.uoguelph.backend.SubjectManager;
import ca.uoguelph.backend.User;
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
        Database.loadExcelSheet(getClass().getResource("/database/UMS_Data.xlsx").getPath());
        FacultyManager.loadFaculty();
        StudentManager.loadStudents();

        //TODO: Load in background Threads?
        SubjectManager.loadSubjects();
        CourseManager.loadCourses();
        EventManager.loadCourses();

        //TODO: add admin to excel sheet
        Admin admin = new Admin("admin", "admin", "Test@uoguelph.ca", "test", "default");

        Image icon = new Image(getClass().getResourceAsStream("/assets/images/unilogoIcon.png"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/login.fxml"));
        Parent root = loader.load();

        // Set up the scene and stage
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("University Management System");
        primaryStage.setScene(scene);

        // Set minimum window size
        primaryStage.setMinWidth(1350);
        primaryStage.setMinHeight(750);
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(icon);

        primaryStage.setResizable(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
