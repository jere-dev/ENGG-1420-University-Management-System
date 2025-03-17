package ca.uoguelph.frontend.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ca.uoguelph.backend.SubjectManager;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;

public class SubjectEditorController {
    @FXML private Button saveButton, deleteButton;
    @FXML private TextField nameField, codeField;
    @FXML private Label nameErrorLabel, codeErrorLabel;
    private String name, code;
    void loadSubject(String name, String code) {
        nameField.setText(name);
        codeField.setText(code);

        this.name = name;
        this.code = code;
    }

    @FXML
    private void initialize() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> validateName());
        codeField.textProperty().addListener((observable, oldValue, newValue) -> validateCode());
    }

    private void validateName() {
        String name = nameField.getText();
        boolean isValid = name.matches("[a-zA-Z0-9]+");
        nameErrorLabel.setText(isValid ? "" : "Invalid characters in subject name");
        saveButton.setDisable(!isValid);
        deleteButton.setDisable(!isValid);
    }

    private void validateCode() {
        String code = codeField.getText();
        boolean isValid = code.matches("[a-zA-Z0-9]+");
        codeErrorLabel.setText(isValid ? "" : "Invalid characters in subject code");
        saveButton.setDisable(!isValid);
        deleteButton.setDisable(!isValid);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        if (name != null && !name.isEmpty()) {
            System.out.printf("\nSave Subject with properties\nName: %s -> %s\nCode: %s -> %s", subjectName, name, subjectCode, code);
            SubjectManager.editSubjectName(SubjectManager.getSubject(code), subjectName);
            SubjectManager.editSubjectCode(SubjectManager.getSubject(code), subjectCode);
        } else {
            System.out.printf("\nCreate Subject with properties\nName: %s\nCode: %s", subjectName, subjectCode);
            SubjectManager.addSubject(subjectCode, subjectName);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        System.out.printf("\nDelete Subject with properties\nName:%s\nCode:%s", subjectName, subjectCode);
    }
}