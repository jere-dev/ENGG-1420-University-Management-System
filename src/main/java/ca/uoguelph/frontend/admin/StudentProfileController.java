package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.GradeRecord;
import ca.uoguelph.backend.Student;
import ca.uoguelph.frontend.objects.GradeRecordProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class StudentProfileController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label studentIDLabel;

    @FXML
    private Label AcademicLevelLabel;

    @FXML
    private Label ProgressLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ImageView profilePhoto;

    @FXML
    private TableColumn<GradeRecord, SimpleStringProperty> courseNameColumn;

    @FXML
    private TableColumn<GradeRecord, SimpleStringProperty> courseCodeColumn;

    @FXML
    private TableColumn<GradeRecord, SimpleStringProperty> sectionColumn;

    @FXML
    private TableColumn<GradeRecord, SimpleStringProperty> professorColumn;

    @FXML
    private TableColumn<GradeRecord, SimpleStringProperty> finalGradeColumn;

    @FXML
    private TextField addressField;

    @FXML
    private TextField telephoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField currentSemesterField;

    @FXML
    private TextField thesisTitleField;

    @FXML
    private TextField tuitionField;

    @FXML
    private ListView<String> registeredCoursesList;

    @FXML
    private TableView<GradeRecord> gradesTable;

    @FXML
    private void handleBackToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/dashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard - Admin");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        String address = addressField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        String currentSemester = currentSemesterField.getText();
        String thesisTitle = thesisTitleField.getText();
        String tuition = tuitionField.getText();

        System.out.println("Saving changes:");
        System.out.println("Address: " + address);
        System.out.println("Telephone: " + telephone);
        System.out.println("Email: " + email);
        System.out.println("Current Semester: " + currentSemester);
        System.out.println("Thesis Title: " + thesisTitle);
        System.out.println("Tuition: " + tuition);
    }
    public void loadStudent(Student stu){
        nameLabel.setText(stu.getName());
        studentIDLabel.setText(stu.getID());
        AcademicLevelLabel.setText(stu.getAcademicLevel());
        ProgressLabel.setText(String.valueOf(stu.getProgress()));
        progressBar.setProgress(stu.getProgress());
        ObservableList<GradeRecord> gradeRecords = FXCollections.observableArrayList(stu.getGradeRecordList());
        gradesTable.setItems(gradeRecords);
        addressField.setText(stu.getAddress());
        telephoneField.setText(stu.getTelephone());
        emailField.setText(stu.getEmail());
        currentSemesterField.setText(stu.getCurrentSemester());
        thesisTitleField.setText(stu.getThesisTitle());
        //TODO add tuition shit
    }

   // @FXML ListView<Entry> gradeList;
    @FXML
    public void initialize (){
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<GradeRecord, SimpleStringProperty>("courseName"));
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<GradeRecord, SimpleStringProperty>("courseCode"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<GradeRecord, SimpleStringProperty>("sectionNo"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<GradeRecord, SimpleStringProperty>("professors"));
        finalGradeColumn.setCellValueFactory(new PropertyValueFactory<GradeRecord, SimpleStringProperty>("finalGrade"));
    }

}