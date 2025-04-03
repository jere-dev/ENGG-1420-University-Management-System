package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Student; // Assuming Student backend class path
import ca.uoguelph.backend.StudentManager; // For saving/updating/finding
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane; // For navigating back
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Pair;

import java.util.List;
import java.util.ArrayList; // For new student courses

public class StudentEditorController {

    @FXML private TextField studentIdField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField semesterField;
    // Using a Label for now to display courses; editing would require a more complex control
    @FXML private Label coursesLabel;

    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;
    @FXML private Label titleLabel; // To set "Add Student" or "Edit Student"

    private Student currentStudent; // To hold the student being edited, null for new

    @FXML
    public void initialize() {
        errorLabel.setText(""); // Clear error label on init
        titleLabel.setText("Edit Student"); // Default title
    }

    /**
     * Loads student data into the editor fields.
     * If the provided ID is null or empty, prepares the form for adding a new student.
     * Otherwise, attempts to load data for the student with the given ID.
     *
     * @param id       The ID of the student to edit, or empty/null for a new student.
     * @param name     (Not used for loading existing students, fetched from backend)
     * @param email    (Not used for loading existing students, fetched from backend)
     * @param semester (Not used for loading existing students, fetched from backend)
     * @param progress (Currently unused in this editor form)
     * @param courses  (Not used for loading existing students, fetched from backend)
     */
    public void loadStudent(String id, String name, String email, String semester, String progress, List<Pair<String, String>> courses) {
         clearFields(); // Clear previous data
         errorLabel.setText(""); // Clear previous errors

         // If ID is empty, it's a new student
        if (id == null || id.isEmpty()) {
            this.currentStudent = null; // Indicate new student
            titleLabel.setText("Add New Student");
            studentIdField.setEditable(true); // Allow editing ID for new student
            studentIdField.requestFocus(); // Set focus to ID field
            coursesLabel.setText("Courses can be managed after creation."); // Placeholder
        } else {
             // Attempt to find the existing student to edit using the provided ID
             try {
                 this.currentStudent = StudentManager.getStudent(id); // Use getStudent

                 if (this.currentStudent != null) {
                     titleLabel.setText("Edit Student: " + this.currentStudent.getName());
                     studentIdField.setText(currentStudent.getID());
                     nameField.setText(currentStudent.getName());
                     emailField.setText(currentStudent.getEmail());
                     semesterField.setText(currentStudent.getCurrentSemester());
                     coursesLabel.setText(formatCourses(currentStudent.getCourses()));
                     studentIdField.setEditable(false); // Don't allow editing ID for existing student
                     nameField.requestFocus(); // Set focus to name field
                 } else {
                      // Should not happen if getStudent throws exception, but handle defensively
                      errorLabel.setText("Error: Student with ID '" + id + "' not found. Cannot edit.");
                      titleLabel.setText("Error Loading Student");
                 }
             } catch (IllegalArgumentException e) {
                 // Handle case where student ID is provided but not found by getStudent
                 errorLabel.setText("Error: Student with ID '" + id + "' not found. Cannot edit.");
                 titleLabel.setText("Error Loading Student");
                 this.currentStudent = null; // Ensure currentStudent is null if not found
                 // Optionally disable save button here
             }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        errorLabel.setText(""); // Clear previous errors
        String id = studentIdField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String semester = semesterField.getText().trim();

         // Basic Validation
         if (id.isEmpty() || name.isEmpty() || email.isEmpty() || semester.isEmpty()) {
            errorLabel.setText("Error: Student ID, Name, Email, and Semester are required.");
            return;
        }

        // TODO: Add more specific validation (e.g., email format, semester format, numeric ID?)

        try {
            if (currentStudent == null) { // Adding a new student
                // Check if ID already exists using getStudent
                 try {
                     Student existing = StudentManager.getStudent(id);
                     if (existing != null) {
                         errorLabel.setText("Error: Student ID '" + id + "' already exists.");
                         return;
                     }
                 } catch (IllegalArgumentException e) {
                     // ID does not exist, which is good for adding a new student
                 }

                // Call StudentManager.addStudent with all required fields
                // Provide defaults for fields not in the editor form
                String defaultAddress = "";
                String defaultTelephone = "";
                String defaultAcademicLevel = "Undergraduate"; // Or fetch/set a default
                String defaultProfilePhoto = ""; // Default photo path or identifier
                ArrayList<Pair<String, String>> defaultCourses = new ArrayList<>();
                String defaultThesisTitle = "";
                float defaultProgress = 0.0f;
                String defaultPassword = "password"; // Set a default or generate one

                StudentManager.addStudent(
                    id,
                    name,
                    defaultAddress,
                    defaultTelephone,
                    email,
                    defaultAcademicLevel,
                    semester, // Use semester from field
                    defaultProfilePhoto,
                    defaultCourses,
                    defaultThesisTitle,
                    defaultProgress,
                    defaultPassword
                );

                System.out.println("New student add request sent for: " + id);
                // Assuming addStudent doesn't return success/failure directly,
                // we navigate back optimistically. Add checks if needed.
                goBack(event); // Go back to manager view after successful add

            } else { // Updating existing student
                // Call specific edit methods from StudentManager
                boolean changed = false;
                if (!currentStudent.getName().equals(name)) {
                    StudentManager.editStudentName(currentStudent, name);
                    changed = true;
                }
                if (!currentStudent.getEmail().equals(email)) {
                    StudentManager.editStudentEmail(currentStudent, email);
                    changed = true;
                }
                if (!currentStudent.getCurrentSemester().equals(semester)) {
                    StudentManager.editStudentCurrentSemester(currentStudent, semester);
                    changed = true;
                }
                // Add calls here if other fields become editable (e.g., address, phone)
                // StudentManager.editStudentAddress(currentStudent, newAddress);
                // StudentManager.editStudentTelephone(currentStudent, newTelephone);

                if (changed) {
                    System.out.println("Student update request sent for: " + id);
                    // StudentManager edit methods call updateSheet, so no separate update call needed
                } else {
                    System.out.println("No changes detected for student: " + id);
                }
                 goBack(event); // Go back to manager view after update attempt
            }
        } catch (Exception e) {
            // Catch potential errors during backend operations or validation
            System.err.println("Error during save operation: " + e.getMessage());
            errorLabel.setText("An unexpected error occurred during save. Check console.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        goBack(event);
    }

    /**
     * Navigates back to the student manager view.
     * Assumes the student manager FXML is located at "/assets/fxml/admin/student_manager.fxml"
     * and the main content area is a StackPane with the ID "contentArea".
     */
    private void goBack(ActionEvent event) {
        try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/student_manager.fxml"));
             Parent studentManagerView = loader.load();

             // Refresh the student list in the manager view after returning
             StudentManagerController managerController = loader.getController();
             if (managerController != null) {
                 managerController.handleRefreshStudents(); // Call refresh method if exists
             }

             StackPane contentArea = findContentArea(event);
             if (contentArea != null) {
                 contentArea.getChildren().clear();
                 contentArea.getChildren().add(studentManagerView);
             } else {
                 System.err.println("Error: Could not find contentArea to navigate back.");
                 errorLabel.setText("Error navigating back."); // Show error in current view if possible
             }
        } catch (Exception e) {
             System.err.println("Error loading student manager view: " + e.getMessage());
             e.printStackTrace();
             errorLabel.setText("Error loading previous screen."); // Show error in current view
        }
    }

     /**
     * Helper method to find the main content area StackPane.
     * @param event The action event from the button press.
     * @return The StackPane if found, otherwise null.
     */
    private StackPane findContentArea(ActionEvent event) {
         Node source = (Node) event.getSource();
         if (source != null && source.getScene() != null) {
             return (StackPane) source.getScene().lookup("#contentArea");
         }
         return null;
    }


    // Helper to format courses for display (similar to StudentManagerController)
    private String formatCourses(List<Pair<String, String>> courses) {
        if (courses == null || courses.isEmpty()) {
            return "No courses assigned.";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Pair<String, String> course : courses) {
             String courseCode = course.getKey();
             // Assuming invalid-key check is relevant here too
             if (courseCode != null && !courseCode.equals("invalid-key")) {
                if (!first) sb.append(", ");
                sb.append(courseCode);
                first = false;
             }
        }
        return sb.length() > 0 ? sb.toString() : "No valid courses assigned.";
    }

     // Helper to clear all input fields and reset state
     private void clearFields() {
        studentIdField.clear();
        nameField.clear();
        emailField.clear();
        semesterField.clear();
        coursesLabel.setText(""); // Clear course display
        this.currentStudent = null; // Reset current student reference
    }
} 