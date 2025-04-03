package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.*;
import ca.uoguelph.frontend.objects.DisplayError;
import ca.uoguelph.frontend.objects.filter.ComboBoxStringFilter;
import ca.uoguelph.frontend.objects.SectionEntry;
import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

/**
 * Controller class for managing course editing operations in the admin interface.
 * Provides functionality for creating, viewing, editing, and deleting courses,
 * including their associated sections.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Full CRUD operations for courses</li>
 *   <li>Section management through embedded table view</li>
 *   <li>Form validation and error handling</li>
 *   <li>Integration with backend CourseManager</li>
 *   <li>Two modes of operation: create new or edit existing</li>
 * </ul>
 *
 * <p>The controller implements {@link DisplayError} for consistent error reporting
 * and extends {@link AbstractAdminEditorController} for common admin editor functionality.
 *
 * <p>Layout is defined in corresponding FXML file and includes:
 * <ul>
 *   <li>Form fields for all course attributes</li>
 *   <li>Filterable subject code selection</li>
 *   <li>Section management table</li>
 *   <li>Action buttons (Save, Delete, Cancel)</li>
 * </ul>
 *
 * @see Course
 * @see CourseManager
 * @see SectionEntry
 * @see AbstractAdminEditorController
 * @see DisplayError
 */
public class CourseEditorController extends AbstractAdminEditorController implements DisplayError {
    private CourseManagerController parentController;

    @FXML private Region rootLayout;
    @FXML private TextField courseNameField, courseCodeField, locationField,
            offeredField, departField, creditField;
    @FXML private TextArea descArea, requisiteArea;
    @FXML private ComboBox<String> sbjComboBox;
    @FXML private Button deleteButton, cancelButton, saveButton;
    @FXML private TableView<SectionEntry> sectionTable;
    @FXML private TableColumn<SectionEntry, String> codeColumn,
            termColumn, instrColumn, seatsColumn;
    @FXML private Label errorLabel;

    private Course originalCourse;
    private final ObservableList<String> sbjCodeList = FXCollections.observableList(
            SubjectManager.getSubjects().stream().map(Subject::getCode).toList()
    );

    // wrap ComboBox in filter
    private ComboBoxStringFilter sbjFilter;
    private SectionEntry lastSelected = null;

    public void loadEmpty(CourseManagerController parentCont) {
        parentController = parentCont;
        originalCourse = null;

        for (TextInputControl control : new TextInputControl[] {
                courseNameField, courseCodeField, locationField, offeredField,
                departField, creditField, descArea, requisiteArea})
            control.setText("");

        disableButtons(false, false, true);
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

        // bypass listener so it doesn't open automatically
        sbjFilter.bypassListener(() -> {
            sbjComboBox.getSelectionModel().select(c.getSubjectCode());
            sbjComboBox.setPromptText(c.getSubjectCode());
        });

        if (parentController == null) parentController = parentCont;
        originalCourse = c;

        loadSections();
        disableButtons(true, true, false);
    }

    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        termColumn.setCellValueFactory(new PropertyValueFactory<>("term"));
        instrColumn.setCellValueFactory(new PropertyValueFactory<>("instructors"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("seats"));

        // add filtering in combo box
        sbjFilter = new ComboBoxStringFilter(sbjComboBox, sbjCodeList);

        // set save button to enable, delete button to disable if data is changed
        for (TextInputControl control : new TextInputControl[]{
                courseNameField, courseCodeField, locationField, offeredField,
                departField, creditField, descArea, requisiteArea
        }) {
            control.textProperty().addListener(o -> disableButtons(false, false, true));
        }
    }

    // Disable buttons as adjusted
    private void disableButtons(boolean save, boolean cancel, boolean delete) {
        if (saveButton.isDisabled() != save) saveButton.setDisable(save);
        if (cancelButton.isDisabled() != cancel) cancelButton.setDisable(cancel);
        if (deleteButton.isDisabled() != delete) deleteButton.setDisable(!delete);
    }

    // Load sections as view-only
    private void loadSections() {
        ObservableList<SectionEntry> obsEntryList = FXCollections.observableArrayList();
        for (Section s : originalCourse.getSections()) {
            SectionEntry sent  = new SectionEntry(s);

            obsEntryList.add(sent);
        }

        sectionTable.setItems(obsEntryList);

        // add on-action events for editing sections
        sectionTable.setOnMouseReleased(me -> handleSectionOnAction(me.getButton() == MouseButton.PRIMARY));
        sectionTable.setOnKeyReleased(ke -> handleSectionOnAction(
                ke.getCode() == KeyCode.ENTER || ke.getCode() == KeyCode.SPACE));
    }

    private void handleSectionOnAction(boolean isAction) {
//        System.out.println("attempt is " + isAction);
        SectionEntry ns = sectionTable.getSelectionModel().getSelectedItem();
        if (lastSelected != null && lastSelected.equals(ns) && isAction)
            handleEditSection(ns.getSection());

        lastSelected = ns;
    }

    @FXML
    private void handleCancel(ActionEvent ignored) {
        if (originalCourse == null) loadEmpty(null);
        else loadDetails(null, originalCourse);
    }

    @Override
    protected void handleSave(ActionEvent event) {
        try {
            if (originalCourse == null) {
                CourseManager.addCourse(sbjComboBox.getValue(), courseCodeField.getText(), SubjectManager.getSubject(sbjComboBox.getValue()).getName(),
                        Float.parseFloat(creditField.getText()), descArea.getText(), requisiteArea.getText(), locationField.getText(),
                        offeredField.getText(), departField.getText(), new ArrayList<>());

                displayError("Course created");
            } else {
                if (!courseNameField.getText().equals(originalCourse.getTitle())) CourseManager.editCourseTitle(originalCourse, courseNameField.getText());
                if (!courseCodeField.getText().equals(originalCourse.getCourseCode())) CourseManager.editCourseCourseCode(originalCourse, courseCodeField.getText());
                if (!sbjComboBox.getValue().equals(originalCourse.getSubjectCode())) CourseManager.editCourseSubjectCode(originalCourse, sbjComboBox.getValue());
                if (!creditField.getText().equals(String.valueOf(originalCourse.getCredits()))) CourseManager.editCourseCredits(originalCourse, Float.parseFloat(creditField.getText()));
                if (!descArea.getText().equals(originalCourse.getDescription())) CourseManager.editCourseDescription(originalCourse, descArea.getText());
                if (!requisiteArea.getText().equals(originalCourse.getRequisites())) CourseManager.editCourseRequisites(originalCourse, requisiteArea.getText());
                if (!offeredField.getText().equals(originalCourse.getOffered())) CourseManager.editCourseOffered(originalCourse, offeredField.getText());
                if (!locationField.getText().equals(originalCourse.getLocations())) CourseManager.editCourseLocations(originalCourse, locationField.getText());
                if (!departField.getText().equals(originalCourse.getDepartment())) CourseManager.editCourseDepartment(originalCourse, departField.getText());

                displayError("Edits saved");
            }

            Course newC = CourseManager.getCourse(sbjComboBox.getValue(), courseCodeField.getText());
            loadDetails(null, newC);
        } catch (Exception e) {
            displayError(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddSection(ActionEvent ignored) {
        handleEditSection(null);
    }

    private void handleEditSection(Section sec) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLPath.sectionEditor()));
            Parent content = loader.load();

            // search for content area
            StackPane contentArea = null;
            Parent parent = rootLayout.getParent();
            while (parent != null) {
                if (parent.getId() != null && parent.getId().equals("contentArea")) {
                    contentArea = (StackPane) parent;
                    break;
                }
                parent = parent.getParent();
            }
            if (contentArea == null) throw new NoSuchObjectException("Could not find parent StackPane");

            SectionEditorController controller = loader.getController();
            // TODO: connect to section editor
            if (sec == null) controller.loadCourse(this, originalCourse);
            else controller.loadSection(this, originalCourse, sec);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        } catch (Exception e) {
            displayError(e.getClass().getName() + ": see terminal for more information");
            e.printStackTrace();
        }
    }

    @Override
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

    // TODO: reminder to create back-navigation capabilities
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

    public void disableEditing() {
        courseNameField.setEditable(false);
        courseCodeField.setEditable(false);
        locationField.setEditable(false);
        offeredField.setEditable(false);
        departField.setEditable(false);
        creditField.setEditable(false);
        descArea.setEditable(false);
        requisiteArea.setEditable(false);
        sbjComboBox.setDisable(true);
        
        // Hide action buttons
        saveButton.setVisible(false);
        deleteButton.setVisible(false);
        cancelButton.setVisible(false);
        
        // Disable section table editing
        sectionTable.setOnMouseReleased(null);
        sectionTable.setOnKeyReleased(null);
    }
}