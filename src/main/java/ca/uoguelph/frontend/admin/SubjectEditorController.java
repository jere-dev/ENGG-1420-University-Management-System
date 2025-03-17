package ca.uoguelph.frontend.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ca.uoguelph.backend.SubjectManager;
import javafx.event.ActionEvent;

public class SubjectEditorController {
    @FXML private Button saveButton, deleteButton;
    @FXML private TextField nameField, codeField;
    @FXML private Label nameErrorLabel, codeErrorLabel;
    private String name, code;
    private boolean skipValidation = false;

    void loadSubject(String name, String code) {
        // Handle null inputs
        this.name = (name != null) ? name : "";
        this.code = (code != null) ? code.toUpperCase() : "";
        
        // Temporarily disable validation
        skipValidation = true;
        
        // Set values
        nameField.setText(this.name);
        codeField.setText(this.code);
        
        // Re-enable validation and validate
        skipValidation = false;
        validateName();
        validateCode();
    }

    @FXML
    private void initialize() {
        // Set error label colors
        nameErrorLabel.setStyle("-fx-text-fill: #941b0c");
        codeErrorLabel.setStyle("-fx-text-fill: #941b0c");
        
        // Set initial button states
        updateButtonStates(false);
        
        // Add listeners after FXML initialization
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (skipValidation) return;
            validateName();
        });
        
        codeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (skipValidation) return;
            
            if (!newValue.equals(newValue.toUpperCase())) {
                codeField.setText(newValue.toUpperCase());
            } else {
                validateCode();
            }
        });
    }

    private void validateName() {
        String name = nameField.getText();
        boolean isValid = true;

        if (name.isEmpty()) {
            nameErrorLabel.setText("");
            isValid = false;
        } else if (!name.matches("[a-zA-Z0-9\\s\\-]+")) {
            nameErrorLabel.setText("Only letters, numbers, spaces and hyphens allowed");
            isValid = false;
        } else {
            try {
                // Don't check for duplicates if it's the same as original name
                if (!name.equals(this.name)) {
                    if (SubjectManager.getSubjectByName(name) != null) {
                        nameErrorLabel.setText("Subject name already exists");
                        isValid = false;
                    } else {
                        nameErrorLabel.setText("");
                    }
                } else {
                    nameErrorLabel.setText("");
                }
            } catch (IllegalArgumentException e) {
                nameErrorLabel.setText("");
            }
        }

        updateButtonStates(isValid && codeField.getText().matches("[A-Z0-9\\-]+"));
    }

    private void validateCode() {
        String code = codeField.getText().toUpperCase();
        boolean isValid = true;

        if (code.isEmpty()) {
            codeErrorLabel.setText("");
            isValid = false;
        } else if (!code.matches("[A-Z0-9\\-]+")) {
            codeErrorLabel.setText("Only uppercase letters, numbers and hyphens allowed");
            isValid = false;
        } else {
            try {
                // Don't check for duplicates if it's the same as original code
                if (!code.equals(this.code)) {
                    if (SubjectManager.getSubject(code) != null) {
                        codeErrorLabel.setText("Subject code already exists");
                        isValid = false;
                    } else {
                        codeErrorLabel.setText("");
                    }
                } else {
                    codeErrorLabel.setText("");
                }
            } catch (IllegalArgumentException e) {
                codeErrorLabel.setText("");
            }
        }

        updateButtonStates(isValid && nameField.getText().matches("[A-Z0-9\\s\\-]+"));
    }

    private void updateButtonStates(boolean isValid) {
        if (saveButton != null) {
            saveButton.setDisable(!isValid);
        }
        if (deleteButton != null) {
            deleteButton.setDisable(name == null || name.isEmpty());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String subjectName = nameField.getText(),
                    subjectCode = codeField.getText();

            if (name != null && !name.isEmpty()) {
                SubjectManager.editSubjectName(SubjectManager.getSubject(code), subjectName);
                SubjectManager.editSubjectCode(SubjectManager.getSubject(code), subjectCode);
            } else {
                SubjectManager.addSubject(subjectCode, subjectName);
            }
        } catch (Exception e) {
            // Handle any errors here
            System.err.println("Error saving subject: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        System.out.printf("\nDelete Subject with properties\nName:%s\nCode:%s", subjectName, subjectCode);
    }
}