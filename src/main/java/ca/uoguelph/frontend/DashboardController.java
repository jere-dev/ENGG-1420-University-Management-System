package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class DashboardController {

    @FXML
    private VBox sideNav;

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox dashboardNav;

    @FXML
    private HBox subjectNav;

    @FXML
    private HBox courseNav;

    @FXML
    private HBox studentNav;

    @FXML
    private HBox facultyNav;

    @FXML
    private HBox eventNav;

    @FXML
    private void initialize() {
        // Set up navigation handlers
        dashboardNav.setOnMouseClicked(event -> navigateToDashboard());
        subjectNav.setOnMouseClicked(event -> navigateToSubject());
        courseNav.setOnMouseClicked(event -> navigateToCourse());
        studentNav.setOnMouseClicked(event -> navigateToStudent());
        facultyNav.setOnMouseClicked(event -> navigateToFaculty());
        eventNav.setOnMouseClicked(event -> navigateToEvent());
    }

    @FXML
    private void toggleNavigation(ActionEvent event) {
        double expandedWidth = 200.0;
        double collapsedWidth = 60.0;
        
        // Get the logo and other elements
        ImageView universityLogo = (ImageView) sideNav.lookup("#universityLogo");
        Label dashboardLabel = (Label) sideNav.lookup("#dashboardLabel");
        Label subjectLabel = (Label) sideNav.lookup("#subjectLabel");
        Label courseLabel = (Label) sideNav.lookup("#courseLabel");
        Label studentLabel = (Label) sideNav.lookup("#studentLabel");
        Label facultyLabel = (Label) sideNav.lookup("#facultyLabel");
        Label eventLabel = (Label) sideNav.lookup("#eventLabel");
        Label userNameLabel = (Label) sideNav.lookup("#userNameLabel");
        Label userRoleLabel = (Label) sideNav.lookup("#userRoleLabel");
        
        // Toggle the width of sideNav
        if (sideNav.getPrefWidth() == expandedWidth) {
            sideNav.setPrefWidth(collapsedWidth);
            // Hide logo and labels completely
            universityLogo.setManaged(false);
            universityLogo.setVisible(false);
            dashboardLabel.setVisible(false);
            subjectLabel.setVisible(false);
            courseLabel.setVisible(false);
            studentLabel.setVisible(false);
            facultyLabel.setVisible(false);
            eventLabel.setVisible(false);
            userNameLabel.setVisible(false);
            userRoleLabel.setVisible(false);
        } else {
            sideNav.setPrefWidth(expandedWidth);
            // Show logo and labels
            universityLogo.setManaged(true);
            universityLogo.setVisible(true);
            dashboardLabel.setVisible(true);
            subjectLabel.setVisible(true);
            courseLabel.setVisible(true);
            studentLabel.setVisible(true);
            facultyLabel.setVisible(true);
            eventLabel.setVisible(true);
            userNameLabel.setVisible(true);
            userRoleLabel.setVisible(true);
        }
    }

    @FXML
    private void navigateToDashboard() {
        loadContent("/assets/fxml/dashboard_content.fxml");
    }

    @FXML
    private void navigateToSubject() {
        loadContent("/assets/fxml/subject_manager.fxml");
    }

    @FXML
    private void navigateToCourse() {
        loadContent("/assets/fxml/course_manager.fxml");
    }

    @FXML
    private void navigateToStudent() {
        loadContent("/assets/fxml/student_manager.fxml");
    }

    @FXML
    private void navigateToFaculty() {
        loadContent("/assets/fxml/faculty_manager.fxml");
    }

    @FXML
    private void navigateToEvent() {
        loadContent("/assets/fxml/event_manager.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();

            // Set the content in the contentArea
            contentArea.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading content: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            // Load the Dashboard FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/dashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene (Dashboard)
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard - Admin");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Dashboard: " + e.getMessage());
        }
    }
}