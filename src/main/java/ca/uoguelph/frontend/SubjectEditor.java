package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SubjectEditor {
    @FXML private Button saveButton, deleteButton; // TODO: remove unnecessary button declarations
    @FXML private TextField nameField, codeField;

    // Loads a subject name and code for admin to edit
    void loadSubject(String name, String code) {
        nameField.setText(name);
        codeField.setText(code);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        System.out.printf("\nSave Subject with properties\nName:%s\nCode:%s", subjectName, subjectCode);
        // TODO: create new subject in database
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        System.out.printf("\nDelete Subject with properties\nName:%s\nCode:%s", subjectName, subjectCode);
        // TODO: delete subject in database
    }
}