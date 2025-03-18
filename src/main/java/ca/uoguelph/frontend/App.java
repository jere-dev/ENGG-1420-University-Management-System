package ca.uoguelph.frontend;

import java.util.ArrayList;

import ca.uoguelph.backend.Admin;
import ca.uoguelph.backend.Database;
import ca.uoguelph.backend.Faculty;
import ca.uoguelph.backend.Student;
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
        SubjectManager.loadSubjects();

        //hardcoded users
        Admin admin = new Admin("admin", "admin", "fuck@uoguelph.ca", "fucker", "default");
        Student student = new Student("student", "student", "otherfucker@uoguelph.ca", "otherFuck", "default", "fuck street", "6473127895", "winter","undergrad", "fuckolegy", 0.8f);
        Faculty faculty = new Faculty("faculty", "faculty", "lastfucker@uoguelph.ca", "lastFucker", "default", "Comp Eng", "fuck", "ROZH");

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
