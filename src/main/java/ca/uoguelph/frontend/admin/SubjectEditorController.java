package ca.uoguelph.frontend.admin;

import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ca.uoguelph.backend.SubjectManager;
import javafx.event.ActionEvent;

public class SubjectEditorController extends AbstractAdminEditorController {
    @FXML
    private Button saveButton, deleteButton;
    @FXML
    private TextField nameField, codeField;
    @FXML
    private Label nameErrorLabel, codeErrorLabel;
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
            nameErrorLabel.setText("Name cannot be empty");
            isValid = false;
        } else if (!name.matches("[a-zA-Z0-9\\s\\-\\(\\)\\,]+")) {
            nameErrorLabel.setText("Only letters, numbers, spaces, brackets and hyphens allowed");
            isValid = false;
        } else if (!name.equals(this.name) && SubjectManager.getSubjectByName(name) != null) {
            // TODO: does not follow implementation of SubjectManager, requires fix
            nameErrorLabel.setText("Subject name already exists");
            isValid = false;
        } else {
            nameErrorLabel.setText("");
        }

        updateButtonStates(nameErrorLabel.getText().isEmpty() && codeErrorLabel.getText().isEmpty());
    }

    private void validateCode() {
        String code = codeField.getText().toUpperCase();
        boolean isValid = true;

        if (code.isEmpty()) {
            codeErrorLabel.setText("Code cannot be empty");
            isValid = false;
        } else if (!code.matches("[A-Z0-9\\-]+")) {
            codeErrorLabel.setText("Only uppercase letters, numbers and hyphens allowed");
            isValid = false;
        } else if (!code.equals(this.code) && SubjectManager.getSubject(code) != null) {
            // TODO: does not follow implementation of SubjectManager.getSubject(code), needs fix
            codeErrorLabel.setText("Subject code already exists");
            isValid = false;
        } else {
            codeErrorLabel.setText("");
        }

        updateButtonStates(nameErrorLabel.getText().isEmpty() && codeErrorLabel.getText().isEmpty());
    }

    private void updateButtonStates(boolean isValid) {
        if (saveButton != null) {
            saveButton.setDisable(!isValid);
            // Make the button look disabled and prevent clicks
            if (!isValid) {
                saveButton.setStyle("-fx-opacity: 0.5; -fx-cursor: default;");
                saveButton.setOnAction(null);
            } else {
                saveButton.setStyle("");
                saveButton.setOnAction(this::handleSave);
            }
        }
        if (deleteButton != null) {
            deleteButton.setDisable(name == null || name.isEmpty());
        }
    }

    @FXML @Override
    protected void handleSave(ActionEvent event) {
        // Check if the fields are valid before proceeding
        if (!nameErrorLabel.getText().isEmpty() || !codeErrorLabel.getText().isEmpty()) {
            System.err.println("Cannot save: Validation errors present.");
            return;
        }

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

    @FXML @Override
    protected void handleDelete(ActionEvent event) {
        String subjectName = nameField.getText(),
                subjectCode = codeField.getText();

        System.out.printf("\nDelete Subject with properties\nName:%s\nCode:%s", subjectName, subjectCode);
    }
}