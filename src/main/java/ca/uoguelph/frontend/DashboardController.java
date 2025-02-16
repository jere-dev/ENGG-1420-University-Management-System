package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private Button dashboardButton;

    @FXML
    private Button subjectManagerButton;

    @FXML
    private Button studentListButton;

    @FXML
    private Button facultyListButton;

    @FXML
    private Button eventManagerButton;

    @FXML
    private Button courseCatalogButton;

    @FXML
    private Button profileButton;

    @FXML
    private void handleDashboard(ActionEvent event) {
        loadScreen("/assets/fxml/dashboard.fxml", event);
    }

    @FXML
    private void handleSubjectManager(ActionEvent event) {
        loadScreen("/assets/fxml/subject_manager_admin.fxml", event);
    }

    @FXML
    private void handleStudentList(ActionEvent event) {
        loadScreen("/assets/fxml/student_list.fxml", event);
    }

    @FXML
    private void handleFacultyList(ActionEvent event) {
        loadScreen("/assets/fxml/faculty_list.fxml", event);
    }

    @FXML
    private void handleEventManager(ActionEvent event) {
        loadScreen("/assets/fxml/event_manager_admin.fxml", event);
    }

    @FXML
    private void handleCourseCatalog(ActionEvent event) {
        loadScreen("/assets/fxml/course_catalog.fxml", event);
    }

    @FXML
    private void handleProfile(ActionEvent event) {
        loadScreen("/assets/fxml/faculty_profile_admin.fxml", event);
    }

    private void loadScreen(String fxmlFile, ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading screen: " + e.getMessage());
        }
    }
}