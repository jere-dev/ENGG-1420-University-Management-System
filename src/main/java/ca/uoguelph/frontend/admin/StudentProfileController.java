package ca.uoguelph.frontend.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class StudentProfileController {

    @FXML
    private ImageView profilePhoto;

    @FXML
    private TextField addressField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField currentSemesterField;

    @FXML
    private TextField thesisTitleField;

    @FXML
    private TextField tuitionField;

    @FXML
    private ListView<String> registeredCoursesList;

    @FXML
    private TableView<?> gradesTable;

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard - Admin");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        String address = addressField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        String currentSemester = currentSemesterField.getText();
        String thesisTitle = thesisTitleField.getText();
        String tuition = tuitionField.getText();

        System.out.println("Saving changes:");
        System.out.println("Address: " + address);
        System.out.println("Telephone: " + telephone);
        System.out.println("Email: " + email);
        System.out.println("Current Semester: " + currentSemester);
        System.out.println("Thesis Title: " + thesisTitle);
        System.out.println("Tuition: " + tuition);

    }
}