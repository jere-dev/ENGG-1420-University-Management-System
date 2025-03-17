package ca.uoguelph.frontend.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class CourseEditorController {
    @FXML private TextField courseCodeField;
    @FXML private TextField sectionNumberField;
    @FXML private TextField professorNameField;
    @FXML private TextField capacityField;
    @FXML private TextField lectureTimeField;
    @FXML private TextField lectureLocationField;
    @FXML private TextField examDateField;
    @FXML private TextField examLocationField;

    private CourseManagerController parentController;
    private int editedRow;

    public void loadCourse(String courseCode, String sectionNumber, String professorName,
                           String capacity, String lectureTime, String lectureLocation,
                           String examDate, String examLocation) {
        courseCodeField.setText(courseCode);
        sectionNumberField.setText(sectionNumber);
        professorNameField.setText(professorName);
        capacityField.setText(capacity);
        lectureTimeField.setText(lectureTime);
        lectureLocationField.setText(lectureLocation);
        examDateField.setText(examDate);
        examLocationField.setText(examLocation);
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        if (parentController != null) {
            parentController.updateRow(editedRow,
                    courseCodeField.getText(),
                    sectionNumberField.getText(),
                    professorNameField.getText(),
                    capacityField.getText(),
                    lectureTimeField.getText(),
                    lectureLocationField.getText(),
                    examDateField.getText(),
                    examLocationField.getText());
            closeEditor(event);
        } else {
            System.out.println("Error: parentController is null");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        closeEditor(event);
    }

    private void closeEditor(ActionEvent event) {
        Node source = (Node) event.getSource();
        Parent parent = source.getParent().getParent();
        while (parent != null && !(parent instanceof StackPane)) {
            parent = parent.getParent();
        }
        if (parent instanceof StackPane) {
            ((StackPane) parent).getChildren().clear();
        }
    }

    public void setParentAndRow(CourseManagerController parent, int row) {
        this.parentController = parent;
        this.editedRow = row;
    }
}