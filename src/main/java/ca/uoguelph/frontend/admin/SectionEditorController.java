package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.*;
import ca.uoguelph.frontend.objects.DisplayError;
import ca.uoguelph.frontend.objects.controller.AbstractAdminEditorController;
import ca.uoguelph.frontend.objects.filter.ComboBoxStringFilter;
import ca.uoguelph.frontend.objects.table.AnchorScaledTable;
import ca.uoguelph.frontend.objects.table.ScaledTable;
import ca.uoguelph.frontend.objects.table.ScrollScaledTable;
import ca.uoguelph.frontend.objects.table.row.SectionEditorRowPresets;
import ca.uoguelph.frontend.objects.table.row.TableRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Stream;

import static ca.uoguelph.frontend.objects.table.row.SectionEditorRowPresets.*;

public class SectionEditorController extends AbstractAdminEditorController implements DisplayError {
    @FXML private Label errorLabel, courseCodeLabel;
    @FXML private TextField codeField, seatsField, semesterField;

    @FXML private ScrollPane instructorPane, enrollmentPane;
    @FXML private AnchorPane meetingPane;

    private ScaledTable<ScrollPane> instrTable, enrollmentTable;
    private ScaledTable<AnchorPane> meetingTable;

    // TODO: add page control for enrollment

    @FXML private ComboBox<String> instructorSelect, meetingSelect, studentSelect;
    @FXML private Button instructorCreate, meetingCreate, studentEnroll,
            saveButton, cancelButton, deleteButton;

    private Section thisSection;
    private Course thisCourse;

    private CourseEditorController parentController;

    private final ObservableList<String> stList = FXCollections.observableList(
            StudentManager.getStudents().stream().map(Student::getName).toList()
    );
    private final ObservableList<String> instrList = FXCollections.observableList(
            FacultyManager.getFaculties().stream().map(User::getName).toList()
    );

    @FXML
    private void initialize() {
        // set on-actions
        saveButton.setOnAction(this::handleSave);
        cancelButton.setOnAction(this::handleCancel);
        deleteButton.setOnAction(this::handleDelete);

        instructorCreate.setOnAction(e -> handleAddFaculty(e, instructorSelect.getValue()));
        meetingCreate.setOnAction(e -> handleAddMeeting(e, meetingSelect.getValue()));
        studentEnroll.setOnAction(e -> handleAddStudent(e, studentSelect.getValue()));

        // fill tables
        instrTable = new ScrollScaledTable(instructorPane, facultyRowCount);
        enrollmentTable = new ScrollScaledTable(enrollmentPane, enrollmentRowCount);
        meetingTable = new AnchorScaledTable(meetingPane, meetingRowCount);

        // set combo box filtering for instructor, enrollment (meeting?)
        new ComboBoxStringFilter(instructorSelect, instrList);
        new ComboBoxStringFilter(studentSelect, stList);
        instructorSelect.getEditor().textProperty().subscribe(this::handleEnrollmentSearch);

        meetingSelect.setItems(FXCollections.observableArrayList("LEC", "LAB", "EXAM"));
    }

    public void loadCourse(CourseEditorController parentController, Course c) {
        this.parentController = parentController;

        // load course name
        thisCourse = c;

        courseCodeLabel.setText(c.getSubjectCode() +
                "*" + c.getCourseCode() +
                " - " + c.getTitle());
    }

    public void loadSection(CourseEditorController parentController, Course c, Section s) {
        // load course name
        loadCourse(parentController, c);

        // load section details
        codeField.setText(s.getCode());
        seatsField.setText(s.getSeats());
        semesterField.setText(s.getTerm());

        // load existing instructors
        instrTable.addRow(SectionEditorRowPresets.getFacultyHeader());
        for (String fStr : s.getInstructors()) {
            TableRow row = SectionEditorRowPresets.getFacultyRow(fStr);
            ((Button) row.get(facultyRowCount-1)).setOnAction(e -> handleDeleteFaculty(e, fStr));
            instrTable.addRow(row);
        }

        // load existing meetings
        meetingTable.addRow(SectionEditorRowPresets.getMeetingHeader());
        for (Meeting m : s.getMeetings()) {
            TableRow row = SectionEditorRowPresets.getMeetingRow(m);
            ((Button) row.get(meetingRowCount-1)).setOnAction(e -> handleDeleteMeeting(e, m.getType()));
            meetingTable.addRow(SectionEditorRowPresets.getMeetingRow(m));
        }

        // load enrollments
        enrollmentTable.addRow(SectionEditorRowPresets.getEnrollmentHeader());
        for (Student st : StudentManager.getStudents().stream().filter(st -> {
            // get student if they have this section
            for (Pair<String, String> pair : st.getCourses()) {
                if (pair.getKey().equals(s.getCode())) return true;
            }
            return false;
        }).toList()) {
            TableRow row = SectionEditorRowPresets.getEnrollmentRow(st);
            // delete functionality
            ((Button) row.get(enrollmentRowCount-1)).setOnAction(e -> handleDeleteStudent(e, st.getName()));
            enrollmentTable.addRow(row);
        }

        thisSection = s;
    }

    private void handleEnrollmentSearch(String searchText) {
        if (thisSection == null) return;

        enrollmentTable.clear();

        List<Student> searchedStudentList = Stream
                .concat(StudentManager.searchByID(searchText).stream(), StudentManager.searchByCode(searchText).stream()).distinct()
                .filter(st -> {
                    // get student if they have this section
                    for (Pair<String, String> pair : st.getCourses()) {
                        if (pair.getKey().equals(thisSection.getCode())) return true;
                    }
                    return false;
                })
                .toList();

        for (Student st : searchedStudentList) {
            TableRow row = SectionEditorRowPresets.getEnrollmentRow(st);
            ((Button) row.get(enrollmentRowCount-1)).setOnAction(e -> handleDeleteStudent(e, st.getName()));
        }
    }

    // TODO: faculty can't be directly linked to a section since all the names are represented by initials, not full names (the name cannot be searched for with 1:1 match)
    private void handleAddFaculty(ActionEvent event, String fStr) {
        // add faculty to table
        // TODO: add faculty to temporary list
    }

    private void handleDeleteFaculty(ActionEvent event, String fStr) {
        // remove faculty from table
        // TODO: remove faculty from temporary list
    }

    private void handleAddMeeting(ActionEvent event, String meetingType) {
        // add meeting to table
        // TODO: add meeting to temporary list
    }

    private void handleDeleteMeeting(ActionEvent event, String meetingType) {
        // delete meeting from table
        // TODO: remove meeting from temporary list
    }

    private void handleAddStudent(ActionEvent event, String stStr) {
        // enroll student in table
        // TODO: add student to temporary list
    }

    private void handleDeleteStudent(ActionEvent event, String stStr) {
        // delete student in table
        // TODO: remove student from temporary list
    }

    private void handleCancel(ActionEvent event) {
        // TODO: revert to original values
    }

    @Override
    protected void handleSave(ActionEvent event) {
        // TODO: save to backend (section code, availability, semester, instructors, meetings, enrollment)
    }

    @Override
    protected void handleDelete(ActionEvent event) {
        // TODO: delete section from backend
    }

    @Override
    public void displayError(String err) {
        displayShortError(err, errorLabel, 2.0);
    }
}
