package ca.uoguelph.frontend;

import ca.uoguelph.backend.Admin;
import ca.uoguelph.backend.CourseManager;
import ca.uoguelph.backend.Database;
import ca.uoguelph.backend.EventManager;
import ca.uoguelph.backend.FacultyManager;
import ca.uoguelph.backend.StudentManager;
import ca.uoguelph.backend.SubjectManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private class backgroundLoad extends Thread {
        public void run() {
            try {
                // Load database first since other operations depend on it
                Database.loadExcelSheet(getClass().getResource("/database/UMS_Data.xlsx").getPath());
                
                // Load all managers in background
                SubjectManager.loadSubjects();
                CourseManager.loadCourses();
                EventManager.loadCourses();
                StudentManager.loadStudents();
                FacultyManager.loadFaculty();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Start background loading immediately
        backgroundLoad dataLoader = new backgroundLoad();
        dataLoader.setDaemon(true); // Makes thread close when app closes
        dataLoader.start();

        // Load UI elements immediately
        Image icon = new Image(getClass().getResourceAsStream("/assets/images/unilogoIcon.png"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/login.fxml"));
        Parent root = loader.load();

        //TODO: add admin to excel sheet (keeping this as it might be important)
        new Admin("admin", "admin", "Test@uoguelph.ca", "test", "default");

        // Set up the scene and stage
        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setTitle("University Management System");
        primaryStage.setScene(scene);
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
