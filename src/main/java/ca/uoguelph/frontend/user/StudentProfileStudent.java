package ca.uoguelph.frontend.user;

import ca.uoguelph.backend.StudentManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import ca.uoguelph.backend.Student;
public class StudentProfileStudent {
    //student editing they own profile.
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label phdStatusLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField idField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea thesisField;
    @FXML
    private Button changePasswordButton;
    @FXML
    private TableView<Pair<String, String>> coursesTable;
    @FXML
    private TableColumn<Pair<String, String>, String> courseNameColumn;
    @FXML
    private TableColumn<Pair<String, String>, String> courseCodeColumn;
    @FXML
    private TableColumn<Pair<String, String>, String> sectionNoColumn;
    @FXML
    private TableColumn<Pair<String, String>, String> gradeColumn;

    private Student currStudent;

  //  @FXML
   // public void initialize() {
        //currStudent = StudentManager.getStudent(c);
   // }
}
