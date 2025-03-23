package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.*;
import ca.uoguelph.frontend.objects.DisplayError;
import ca.uoguelph.frontend.objects.SectionEntry;
import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.SearchableComboBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseEditorController extends AbstractAdminEditorController implements DisplayError {
    @FXML private TextField courseNameField, courseCodeField, locationField,
            offeredField, departField, creditField;
    @FXML private TextArea descArea, requisiteArea;
    @FXML private SearchableComboBox<String> sbjComboBox;
    @FXML private Button deleteButton, cancelButton, saveButton;
    @FXML private TableView<SectionEntry> sectionTable;
    @FXML private TableColumn<SectionEntry, String> codeColumn,
            termColumn, instrColumn, seatsColumn, linkColumn;
    @FXML private Label errorLabel;

    private HashMap<Node, Section> linkSectionMap = new HashMap<>();
    private Course originalCourse;

    private final List<String> sbjCodeList = new ArrayList<>(SubjectManager.getSubjects()
            .stream().map(Subject::getCode).toList());

    private CourseManagerController parentController;

    public void loadEmpty(CourseManagerController parentCont) {
        parentController = parentCont;
        originalCourse = null;

        for (TextInputControl control : new TextInputControl[] {courseNameField, courseCodeField,
                locationField, offeredField, departField, creditField, descArea, requisiteArea})
            control.setText("");

        deleteButton.setDisable(true);
        cancelButton.setDisable(false);
        saveButton.setDisable(false);
    }

    public void loadDetails(CourseManagerController parentCont, Course c) {
        courseNameField.setText(c.getTitle()); courseNameField.setPromptText(c.getTitle());
        courseCodeField.setText(c.getCourseCode()); courseCodeField.setText(c.getCourseCode());
        locationField.setText(c.getLocations()); locationField.setPromptText(c.getLocations());
        offeredField.setText(c.getOffered()); offeredField.setPromptText(c.getOffered());
        departField.setText(c.getDepartment()); departField.setPromptText(c.getDepartment());
        creditField.setText(String.valueOf(c.getCredits())); creditField.setPromptText(String.valueOf(c.getCredits()));

        descArea.setText(c.getDescription()); descArea.setPromptText(c.getDescription());
        requisiteArea.setText(c.getRequisites()); requisiteArea.setPromptText(c.getRequisites());
        sbjComboBox.setValue(c.getSubjectCode()); sbjComboBox.setPromptText(c.getSubjectCode());

        if (parentController == null) parentController = parentCont;
        originalCourse = c;

        loadSections();
        resetButtons();
    }

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        termColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        instrColumn.setCellValueFactory(new PropertyValueFactory<>("instructors"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));

//        loadSbjCombo("");

        // set save button to enable, delete button to disable if data is changed
        ChangeListener<String> l = (obs, str1, str2) -> {
            if (saveButton.isDisabled()) saveButton.setDisable(false);
            if (cancelButton.isDisabled()) cancelButton.setDisable(false);
            if (!deleteButton.isDisabled()) deleteButton.setDisable(true);
        };

        for (TextInputControl control : new TextInputControl[]{courseNameField, courseCodeField,
                locationField, offeredField, departField, creditField, descArea, requisiteArea})
        {
            control.textProperty().addListener(l);
        }
        sbjComboBox.valueProperty().addListener(l);
        System.out.println("add listener");
        sbjComboBox.setItems(FXCollections.observableList(sbjCodeList));
    }

    private void resetButtons() {
        saveButton.setDisable(true);
        cancelButton.setDisable(true);
        deleteButton.setDisable(false);
    }

    private void loadSections() {
        ObservableList<SectionEntry> obsEntryList = FXCollections.observableArrayList();
        for (Section s : originalCourse.getSections())
            obsEntryList.add(new SectionEntry(s));

        sectionTable.setItems(obsEntryList);
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        if (originalCourse == null) loadEmpty(null);
        else loadDetails(null, originalCourse);
    }

    @FXML @Override
    protected void handleSave(ActionEvent event) {
        // TODO: this creates a new course, not saves it. fix implementation of saving the course
        try {
            CourseManager.addCourse(sbjComboBox.getValue(), courseCodeField.getText(), SubjectManager.getSubject(sbjComboBox.getValue()).getName(),
                    Float.parseFloat(creditField.getText()), descArea.getText(), requisiteArea.getText(), locationField.getText(),
                    offeredField.getText(), departField.getText(), new ArrayList<>());
            Course newC = CourseManager.getCourse(sbjComboBox.getValue(), courseCodeField.getText());
            loadDetails(null, newC);
        } catch (Exception e) {
            displayError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleEditSection(ActionEvent event) {
        // TODO: open section editor
    }

    @FXML @Override
    protected void handleDelete(ActionEvent event) {
        try {
            CourseManager.removeCourse(originalCourse);
            originalCourse = null;
            loadEmpty(null);
            displayError("Course deleted!");
        } catch (Exception e) {
            displayError(e.getMessage());
            e.printStackTrace();
        }
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

    @Override
    public void displayError(String err) {
        displayShortError(err, errorLabel, 2.0);
    }
}