package ca.uoguelph.frontend.admin;

import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ca.uoguelph.backend.FacultyManager;
import ca.uoguelph.backend.Faculty;
import javafx.event.ActionEvent;
import java.util.ArrayList;

public class FacultyEditorController extends AbstractAdminEditorController {

    @FXML
    private Button saveButton, deleteButton;
    @FXML
    private TextField nameField, idField, degreeField, researchInterestField, emailField, officeLocationField, passwordField;
    @FXML
    private Label nameErrorLabel, idErrorLabel, degreeErrorLabel, researchInterestErrorLabel, emailErrorLabel, officeLocationErrorLabel, passwordErrorLabel;
    private String id, name, degree, researchInterest, email, officeLocation, password;
    private boolean skipValidation = false;
    @FXML
    public void loadFaculty(String id, String name, String degree, String researchInterest, String email, String officeLocation, String password) {
        idField.setText(id);
        nameField.setText(name);
        degreeField.setText(degree);
        researchInterestField.setText(researchInterest);
        emailField.setText(email);
        officeLocationField.setText(officeLocation);
        passwordField.setText(password);
        System.out.println("DEBUG: Faculty data loaded!");
    }


    @FXML
    private void initialize() {
        System.out.println("DEBUG: FacultyEditorController initialized!");
        updateButtonStates(false);

        nameField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
        idField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
        degreeField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
        researchInterestField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
        emailField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
        officeLocationField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
        passwordField.textProperty().addListener((obs, old, newValue) -> { if (!skipValidation) validateFields(); });
    }


    private void validateFields() {
        // Implement validation logic for faculty attributes
        boolean isValid = true;

        if (nameField.getText().isEmpty()) {
            nameErrorLabel.setText("Name cannot be empty");
            isValid = false;
        } else {
            nameErrorLabel.setText("");
        }

        if (idField.getText().isEmpty()) {
            idErrorLabel.setText("ID cannot be empty");
            isValid = false;
        } else {
            idErrorLabel.setText("");
        }

        if (emailField.getText().isEmpty() || !emailField.getText().matches("[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+")) {
            emailErrorLabel.setText("Invalid email format");
            isValid = false;
        } else {
            emailErrorLabel.setText("");
        }

        updateButtonStates(isValid);
    }

    private void updateButtonStates(boolean isValid) {
        if (saveButton != null) saveButton.setDisable(!isValid);
        if (deleteButton != null) deleteButton.setDisable(id == null || id.isEmpty());
    }


    @FXML
    @Override
    protected void handleSave(ActionEvent event) {
        System.out.println("DEBUG: Save button clicked!");

        if (!nameErrorLabel.getText().isEmpty() || !idErrorLabel.getText().isEmpty() || !emailErrorLabel.getText().isEmpty()) {
            System.out.println("ERROR: Validation errors prevent saving.");
            return;
        }

        try {
            Faculty faculty = new Faculty(
                    idField.getText(), nameField.getText(), degreeField.getText(),
                    researchInterestField.getText(), emailField.getText(),
                    officeLocationField.getText(), null, passwordField.getText(), ""
            );

            System.out.println("DEBUG: Creating faculty object - " + faculty);

            if (FacultyManager.getFaculty(idField.getText()) != null) {
                System.out.println("DEBUG: Updating existing faculty!");
                FacultyManager.editFacultyName(faculty, nameField.getText());
                FacultyManager.editFacultyDegree(faculty, degreeField.getText());
                FacultyManager.editFacultyResearchInterest(faculty, researchInterestField.getText());
                FacultyManager.editFacultyEmail(faculty, emailField.getText());
                FacultyManager.editFacultyOfficeLocation(faculty, officeLocationField.getText());
                FacultyManager.editFacultyPassword(faculty, passwordField.getText());
            } else {
                System.out.println("DEBUG: Adding new faculty!");
                FacultyManager.addFaculty(
                        idField.getText(), nameField.getText(), degreeField.getText(),
                        researchInterestField.getText(), emailField.getText(),
                        officeLocationField.getText(), new ArrayList<>(), passwordField.getText(), ""
                );
            }

            System.out.println("DEBUG: Faculty successfully saved!");

            // Navigate back to faculty list after saving
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/faculty_manager.fxml"));
            Parent facultyListScreen = loader.load();

            StackPane contentArea = (StackPane) ((Node) event.getSource()).getScene().lookup("#contentArea");
            if (contentArea == null) {
                System.out.println("ERROR: Could not find contentArea.");
                return;
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(facultyListScreen);
            System.out.println("DEBUG: Navigated back to Faculty List!");

        } catch (Exception e) {
            System.out.println("ERROR: Failed to save faculty.");
            e.printStackTrace();
        }
    }



    @FXML
    @Override
    protected void handleDelete(ActionEvent event) {
        System.out.printf("\nDelete Faculty with ID: %s\nName: %s", idField.getText(), nameField.getText());
    }
}
