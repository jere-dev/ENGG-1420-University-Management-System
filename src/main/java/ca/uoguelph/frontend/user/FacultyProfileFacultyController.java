package ca.uoguelph.frontend.user;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;


public class FacultyProfileFacultyController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleBackToDashboard(ActionEvent event){

    }

    @FXML
    private void handleEditProfile(ActionEvent event) {
        passwordField.setEditable(true);
        // Add logic to handle editing an event
    }
}
