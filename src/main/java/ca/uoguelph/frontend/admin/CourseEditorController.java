package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Course;
import ca.uoguelph.backend.Section;
import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class CourseEditorController extends AbstractAdminEditorController {
    @FXML private TextField courseNameField, courseCodeField, locationField,
            offeredField, departField, creditField;
    @FXML private TextArea descArea, requisiteArea;
    @FXML private ComboBox<String> sbjCodeCombo;
    @FXML private TableView<Section> sectionTable;

    private Course originalCourse;

    private CourseManagerController parentController;

    public void loadCourse(CourseManagerController parentCont, Course c) {
        courseNameField.setText(c.getTitle()); courseNameField.setPromptText(c.getTitle());
        courseCodeField.setText(c.getCourseCode()); courseCodeField.setText(c.getCourseCode());
        locationField.setText(c.getLocations()); locationField.setPromptText(c.getLocations());
        offeredField.setText(c.getOffered()); offeredField.setPromptText(c.getOffered());
        departField.setText(c.getDepartment()); departField.setPromptText(c.getDepartment());
        creditField.setText(String.valueOf(c.getCredits())); creditField.setPromptText(String.valueOf(c.getCredits()));

        descArea.setText(c.getDescription()); descArea.setPromptText(c.getDescription());
        requisiteArea.setText(c.getRequisites()); requisiteArea.setPromptText(c.getRequisites());
        sbjCodeCombo.setPromptText(c.getSubjectCode());

        if (originalCourse == null) originalCourse = c;
        if (parentController == null) parentController = parentCont;
        loadSections();
    }

    private void loadSections() {

    }

    @Override
    protected void handleSave(ActionEvent event) {

    }

    @Override
    protected void handleDelete(ActionEvent event) {

    }

//    private int editedRow;
//
//    public void loadCourse(String courseCode, String sectionNumber, String professorName,
//                           String capacity, String lectureTime, String lectureLocation,
//                           String examDate, String examLocation) {
//        courseCodeField.setText(courseCode);
//        sectionNumberField.setText(sectionNumber);
//        professorNameField.setText(professorName);
//        capacityField.setText(capacity);
//        lectureTimeField.setText(lectureTime);
//        lectureLocationField.setText(lectureLocation);
//        examDateField.setText(examDate);
//        examLocationField.setText(examLocation);
//    }
//
//    @FXML
//    private void saveChanges(ActionEvent event) {
//        if (parentController != null) {
//            parentController.updateRow(editedRow,
//                    courseCodeField.getText(),
//                    sectionNumberField.getText(),
//                    professorNameField.getText(),
//                    capacityField.getText(),
//                    lectureTimeField.getText(),
//                    lectureLocationField.getText(),
//                    examDateField.getText(),
//                    examLocationField.getText());
//            closeEditor(event);
//        } else {
//            System.out.println("Error: parentController is null");
//        }
//    }
//
//    @FXML
//    private void cancel(ActionEvent event) {
//        closeEditor(event);
//    }

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
//
//    public void setParentAndRow(CourseManagerController parent, int row) {
//        this.parentController = parent;
//        this.editedRow = row;
//    }
}