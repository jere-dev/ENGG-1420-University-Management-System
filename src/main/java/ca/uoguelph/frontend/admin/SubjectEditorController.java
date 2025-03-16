package ca.uoguelph.frontend.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class SubjectEditorController {
    @FXML private Button saveButton, deleteButton; // TODO: remove unnecessary button declarations
    @FXML private TextField nameField, codeField;

    // Original name, code
    private String name, code;

    // Loads a subject name and code for admin to edit
    void loadSubject(String name, String code) {
        nameField.setText(name);
        codeField.setText(code);

        this.name = name;
        this.code = code;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        if (name != null) {
            System.out.printf("\nSave Subject with properties\nName: %s -> %s\nCode: %s -> %s", subjectName, name, subjectCode, code);
        } else {
            System.out.printf("\nCreate Subject with properties\nName: %s\nCode: %s", subjectName, subjectCode);
        }

        // TODO: create or edit new subject in database
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        System.out.printf("\nDelete Subject with properties\nName:%s\nCode:%s", subjectName, subjectCode);
        // TODO: delete subject in database
    }
}