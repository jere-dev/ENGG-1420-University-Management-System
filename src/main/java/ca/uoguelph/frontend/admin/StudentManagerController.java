package ca.uoguelph.frontend.admin;

import ca.uoguelph.backend.Student;
import ca.uoguelph.backend.StudentManager;
import ca.uoguelph.frontend.admin.StudentEditorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentManagerController {
    private final List<Student> studentList = new ArrayList<>();
    private final HashMap<Button, Student> buttonMap = new HashMap<>();
    private int page = 0;
    private int pageRowCount = 20;

    @FXML private GridPane tableGrid;
    @FXML private TextField searchField;
    @FXML private Button leftPageButton, rightPageButton;
    @FXML private TextField pageText, rowCountText;
    @FXML private ComboBox<String> programFilter;
    @FXML private ComboBox<String> yearFilter;
    @FXML private ScrollPane scrollPane;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        setupGridPane();
        studentList.addAll(StudentManager.getStudents());
        updateTable("");

        // Setup scroll pane
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            scrollPane.getStyleClass().add("edge-to-edge");
        }

        // Style the search field
        searchField.setPromptText("Search by ID, Name or Email...");
        searchField.setStyle("-fx-prompt-text-fill: #757575; -fx-background-radius: 3; " +
                           "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                           "-fx-border-width: 1; -fx-background-color: white;");
        
        // Enable dynamic search with small delay
        searchField.textProperty().addListener((obs, old, newText) -> {
            if (searchField.isFocused()) {
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
                pause.setOnFinished(e -> updateTable(newText.trim()));
                pause.play();
            }
        });
    }

    private void setupGridPane() {
        tableGrid.setHgap(20);
        tableGrid.setVgap(5);
        tableGrid.setPadding(new Insets(5, 15, 15, 15));
        tableGrid.setStyle("-fx-background-color: white; -fx-background-radius: 3; " +
                          "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                          "-fx-border-width: 1; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.05), 3, 0, 0, 0);");

        // Make grid fill parent
        tableGrid.setMaxWidth(Double.MAX_VALUE);
        tableGrid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tableGrid, Priority.ALWAYS);
        HBox.setHgrow(tableGrid, Priority.ALWAYS);

        // Adjust percentages for Action column
        ColumnConstraints idCol = new ColumnConstraints();
        idCol.setPercentWidth(13); // Adjust
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(17); // Adjust
        ColumnConstraints emailCol = new ColumnConstraints();
        emailCol.setPercentWidth(25);
        ColumnConstraints semesterCol = new ColumnConstraints();
        semesterCol.setPercentWidth(13); // Adjust
        ColumnConstraints progressCol = new ColumnConstraints();
        progressCol.setPercentWidth(10);
        ColumnConstraints coursesCol = new ColumnConstraints();
        coursesCol.setPercentWidth(12); // Adjust
        ColumnConstraints actionCol = new ColumnConstraints(); // New Action Column
        actionCol.setPercentWidth(10); // Adjust width

        tableGrid.getColumnConstraints().clear(); // Clear existing
        tableGrid.getColumnConstraints().addAll(idCol, nameCol, emailCol, semesterCol, progressCol, coursesCol, actionCol);
    }

    private void updateTable(String search) {
        studentList.clear();
        page = 0;

        if (search.isEmpty()) {
            studentList.addAll(StudentManager.getStudents());
        } else {
            // Normalize search text
            String normalizedSearch = search.toLowerCase()
                .replaceAll("\\s+", " ")
                .replaceAll("\\.", " ")
                .trim();

            // Search with normalized comparison across multiple fields
            studentList.addAll(StudentManager.getStudents().stream()
                .filter(s -> {
                    String normalizedID = s.getID().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .trim();
                    String normalizedName = s.getName().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .trim();
                    String normalizedEmail = s.getEmail().toLowerCase()
                        .replaceAll("\\s+", " ")
                        .trim();
                    
                    return normalizedID.contains(normalizedSearch) || 
                           normalizedName.contains(normalizedSearch) ||
                           normalizedEmail.contains(normalizedSearch);
                })
                .toList());
        }

        loadTable();
    }

    private void loadTable() {
        tableGrid.getChildren().clear();
        buttonMap.clear();

        addHeaderRow();

        Separator headerSeparator = new Separator();
        headerSeparator.setPadding(new Insets(5, 0, 5, 0));
        tableGrid.add(headerSeparator, 0, 1, tableGrid.getColumnConstraints().size(), 1); // Span all columns

        int startIndex = page * pageRowCount;
        int endIndex = Math.min((page + 1) * pageRowCount, studentList.size());

        for (int i = startIndex; i < endIndex; i++) {
            Student student = studentList.get(i);
            int rowIndex = (i - startIndex) * 2 + 2;

            addStudentRow(student, rowIndex);
        }

        updatePageButtons();
    }

    private void addHeaderRow() {
        Label[] headers = {
            new Label("Student ID"),
            new Label("Name"),
            new Label("Email"),
            new Label("Current Semester"),
            new Label("Progress"),
            new Label("Courses"),
            new Label("Actions") // Add Actions header
        };

        String headerStyle = "-fx-font-weight: bold; -fx-font-size: 14px; " +
                           "-fx-text-fill: #941B0C; -fx-padding: 12 5 12 5; " +
                           "-fx-background-color: transparent;";
        for (int i = 0; i < headers.length; i++) {
            headers[i].getStyleClass().add("table-header");
            headers[i].setStyle(headerStyle);
            headers[i].setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(headers[i], Priority.ALWAYS);
            tableGrid.add(headers[i], i, 0);
        }
    }

    private void addStudentRow(Student student, int rowIndex) {
        // Create a container for the entire row
        HBox rowContainer = new HBox();
        rowContainer.setMaxWidth(Double.MAX_VALUE);
        rowContainer.getStyleClass().add("table-row");
        rowContainer.setStyle("-fx-background-color: transparent;");
        
        Label[] labels = {
            new Label(student.getID()),
            new Label(student.getName()),
            new Label(student.getEmail()),
            new Label(student.getCurrentSemester()),
            new Label(String.format("%.1f%%", student.getProgress())),
            new Label(formatCourses(student.getCourses()))
        };

        String labelStyle = "-fx-padding: 10 5 10 5; -fx-font-size: 13px; -fx-text-fill: #333333;";
        
        // Add hover effect to the entire row
        rowContainer.setOnMouseEntered(e -> {
            rowContainer.setStyle("-fx-background-color: #F6F6F6;");
            for (Label label : labels) {
                label.setStyle(labelStyle);
            }
        });
        
        rowContainer.setOnMouseExited(e -> {
            rowContainer.setStyle("-fx-background-color: transparent;");
            for (Label label : labels) {
                label.setStyle(labelStyle);
            }
        });
        
        // Add the row container to the grid
        tableGrid.add(rowContainer, 0, rowIndex, tableGrid.getColumnCount(), 1);
        
        // Add labels to the row container
        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            labels[i].setMaxWidth(Double.MAX_VALUE);
            labels[i].setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(labels[i], Priority.ALWAYS);
            GridPane.setVgrow(labels[i], Priority.ALWAYS);
            tableGrid.add(labels[i], i, rowIndex);
        }

        // --- Add Edit Button ---
        Button editButton = createStyledButton("Edit"); // Use existing styled button method
        editButton.setOnAction(event -> handleEdit(student, event)); // Pass student to handler
        buttonMap.put(editButton, student); // Map button if needed elsewhere

        // Add button(s) to an HBox for layout in the cell
        HBox actionBox = new HBox(editButton);
        actionBox.setPadding(new Insets(5)); // Padding around the button
        actionBox.setAlignment(javafx.geometry.Pos.CENTER);
        GridPane.setHalignment(actionBox, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(actionBox, javafx.geometry.VPos.CENTER);

        // Add the actionBox to the last column
        tableGrid.add(actionBox, labels.length, rowIndex);

        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(0));
        rowSeparator.setStyle("-fx-background-color: #E0E0E0;");
        rowSeparator.setMaxWidth(Double.MAX_VALUE);
        tableGrid.add(rowSeparator, 0, rowIndex + 1, tableGrid.getColumnCount(), 1); // Span all columns
    }

    private String formatCourses(List<Pair<String, String>> courses) {
        if (courses == null || courses.isEmpty()) {
            return "No courses";
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Pair<String, String> course : courses) {
            String courseCode = course.getKey();
            if (courseCode != null && !courseCode.equals("invalid-key")) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(courseCode); // Use the full course code directly
                first = false;
            }
        }
        return sb.toString();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setMinWidth(60);
        button.setMinHeight(30);
        button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
        button.getStyleClass().add("action-button");
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; -fx-font-size: 14px; -fx-font-family: 'Graduate'; -fx-cursor: hand;");
            button.setEffect(new javafx.scene.effect.DropShadow(3, 0, 0, javafx.scene.paint.Color.rgb(0, 0, 0, 0.2)));
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
            button.setEffect(null);
        });
        
        button.setOnMousePressed(e -> 
            button.setStyle("-fx-background-color: #7B1609; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';"));
        
        button.setOnMouseReleased(e -> {
            if (button.isHover()) {
                button.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
            } else {
                button.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; -fx-font-size: 14px; -fx-font-family: 'Graduate';");
            }
        });
        
        return button;
    }

    private void handleEdit(Student student, ActionEvent event) {
        if (student == null) {
            System.err.println("ERROR: Cannot edit null student.");
            if (errorLabel != null) errorLabel.setText("Error: No student selected for editing.");
            return;
        }
        System.out.println("Edit student button clicked for: " + student.getName() + " (ID: " + student.getID() + ")");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/student_editor.fxml"));
            Parent content = loader.load();

            StudentEditorController controller = loader.getController();
            if (controller == null) {
                System.err.println("ERROR: StudentEditorController is NULL.");
                if (errorLabel != null) errorLabel.setText("Error: Could not load student editor controller.");
                return;
            }

            // Load the selected student's data into the editor
            controller.loadStudent(
                student.getID(),
                student.getName(),
                student.getEmail(),
                student.getCurrentSemester(),
                String.valueOf(student.getProgress()), // Pass progress as string
                student.getCourses()
            );
            System.out.println("DEBUG: StudentEditorController load method called successfully for editing.");

            // Find the content area to replace
            StackPane contentArea = (StackPane) ((Node) event.getSource()).getScene().lookup("#contentArea");
            if (contentArea == null) {
                System.err.println("ERROR: Could not find contentArea.");
                if (errorLabel != null) errorLabel.setText("Error: Could not find main content area.");
                return;
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            System.out.println("DEBUG: Student Editor UI loaded for editing!");

        } catch (NullPointerException npe) {
             System.err.println("ERROR: Could not find student_editor.fxml. Ensure the path is correct.");
             if (errorLabel != null) errorLabel.setText("Error: Student editor file not found.");
             npe.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load Student Editor for editing.");
            if (errorLabel != null) errorLabel.setText("Error loading editor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRefreshStudents() {
        updateTable("");
    }

    @FXML
    private void handlePrintList() {
        System.out.println("Print button clicked");
    }

    @FXML
    private void handleStudentDetails() {
        Student selectedStudent = buttonMap.values().stream()
            .filter(student -> student.getID().equals(searchField.getText()))
            .findFirst()
            .orElse(null);
            
        if (selectedStudent != null) {
            System.out.println("View details for: " + selectedStudent.getName());
        } else {
            System.out.println("Please select a student");
        }
    }

    @FXML
    private void handleExportList() {
        System.out.println("Export list button clicked");
    }

    @FXML
    private void handlePrevPage(ActionEvent event) {
        if (page > 0) {
            page--;
            loadTable();
        }
    }

    @FXML
    private void handleNextPage(ActionEvent event) {
        if ((page + 1) * pageRowCount < studentList.size()) {
            page++;
            loadTable();
        }
    }

    @FXML
    private void handleKeyPage(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        try {
            String text = pageText.getText();
            pageText.setText("");
            
            int newPage = Integer.parseInt(text) - 1;
            if (newPage >= 0 && newPage * pageRowCount < studentList.size()) {
                page = newPage;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }

    @FXML
    private void handleKeyCount(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER) return;

        try {
            String text = rowCountText.getText();
            rowCountText.setText("");
            
            int newCount = Integer.parseInt(text);
            if (newCount > 5) {
                page = Math.min(page * pageRowCount / newCount, studentList.size() / newCount);
                pageRowCount = newCount;
                loadTable();
            }
        } catch (NumberFormatException ignored) {}
    }

    private void updatePageButtons() {
        if (leftPageButton != null) {
            leftPageButton.setDisable(page <= 0);
        }
        if (rightPageButton != null) {
            rightPageButton.setDisable((page + 1) * pageRowCount >= studentList.size());
        }
        if (pageText != null) {
            pageText.setPromptText(String.valueOf(page + 1));
        }
        if (rowCountText != null) {
            rowCountText.setPromptText(pageRowCount + " rows per page");
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = searchField.getText();
        if (searchText.isEmpty()) {
            // TODO: load default settings
        }
        updateTable(searchText);
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        System.out.println("Add Student clicked");
        try {
            // Assuming the editor FXML path is similar to faculty editor
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/student_editor.fxml"));
            Parent content = loader.load();

            // Assuming StudentEditorController exists and has a similar load method
            StudentEditorController controller = loader.getController();
            if (controller == null) {
                System.err.println("ERROR: StudentEditorController is NULL.");
                 if (errorLabel != null) errorLabel.setText("Error: Could not load student editor controller.");
                return;
            }

            // Call a method to load an empty student form
            // Adjust method name and parameters based on StudentEditorController implementation
            controller.loadStudent("", "", "", "", "", new ArrayList<>()); // Example parameters
            System.out.println("DEBUG: StudentEditorController load method called successfully for new student.");

            // Find the content area to replace
            StackPane contentArea = (StackPane) ((Node) event.getSource()).getScene().lookup("#contentArea");
            if (contentArea == null) {
                System.err.println("ERROR: Could not find contentArea.");
                 if (errorLabel != null) errorLabel.setText("Error: Could not find main content area.");
                return;
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            System.out.println("DEBUG: Student Editor UI added to scene!");

        } catch (NullPointerException npe) {
             System.err.println("ERROR: Could not find student_editor.fxml. Please ensure the path is correct.");
             if (errorLabel != null) errorLabel.setText("Error: Student editor file not found.");
             npe.printStackTrace();
        } catch (Exception e) {
            System.err.println("ERROR: Failed to load Student Editor.");
             if (errorLabel != null) errorLabel.setText("Error loading student editor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}