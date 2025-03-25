package ca.uoguelph.frontend.user;

import java.util.ArrayList;

import ca.uoguelph.backend.*;
import ca.uoguelph.backend.login.LoginManager;
import ca.uoguelph.frontend.admin.CourseEditorController;
import ca.uoguelph.frontend.objects.DisplayError;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import ca.uoguelph.backend.CourseManager;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import javafx.event.ActionEvent;

public class FacultyCourseCatalogController implements DisplayError {
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Label errorLabel;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox screenBox;
    private GridPane tableGrid;
    private Database database;

    @FXML
    public void initialize() {
        tableGrid = new GridPane();
        scrollPane.setContent(tableGrid);
        setupGridPane();
        
        database = Database.getInstance();
        loadCourses();

        searchField.setPromptText("Search by Name or Code...");
        searchField.setStyle("-fx-prompt-text-fill: #757575; -fx-background-radius: 3; " +
                           "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                           "-fx-border-width: 1; -fx-background-color: white; " +
                           "-fx-font-family: System; -fx-font-size: 13px;");

        searchField.textProperty().addListener((obs, old, newText) -> {
            if (searchField.isFocused()) {
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
                pause.setOnFinished(e -> loadCourses(newText.trim()));
                pause.play();
            }
        });
    }

    private void setupGridPane() {
        tableGrid.setHgap(20);
        tableGrid.setVgap(0);
        tableGrid.setPadding(new Insets(5, 15, 15, 15));
        tableGrid.setStyle("-fx-background-color: white; -fx-background-radius: 3; " +
                          "-fx-border-radius: 3; -fx-border-color: #E0E0E0; " +
                          "-fx-border-width: 1;");

        tableGrid.setMaxWidth(Double.MAX_VALUE);
        tableGrid.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(tableGrid, Priority.ALWAYS);

        ColumnConstraints subjectCol = new ColumnConstraints();
        subjectCol.setPercentWidth(10);
        ColumnConstraints nameCol = new ColumnConstraints();
        nameCol.setPercentWidth(20);
        ColumnConstraints descriptionCol = new ColumnConstraints();
        descriptionCol.setPercentWidth(45);
        ColumnConstraints creditsCol = new ColumnConstraints();
        creditsCol.setPercentWidth(10);
        ColumnConstraints actionCol = new ColumnConstraints();
        actionCol.setPercentWidth(15);

        tableGrid.getColumnConstraints().addAll(subjectCol, nameCol, descriptionCol, creditsCol, actionCol);
    }

    private void loadCourses() {
        loadCourses("");
    }

    private void loadCourses(String searchTerm) {
        updateTable(searchTerm);
    }

    private void updateTable(String searchTerm) {
        tableGrid.getChildren().clear();
        addHeaderRow();
        
        if (LoginManager.getCurrentUser() instanceof Faculty faculty) {
            ArrayList<Pair<String, String>> facultyCourses = faculty.getCourses();
            System.out.println("Faculty ID: " + faculty.getID());
            System.out.println("Number of courses: " + (facultyCourses != null ? facultyCourses.size() : "null"));
            
            if (facultyCourses != null && !facultyCourses.isEmpty()) {
                int rowIndex = 1;
                for (Pair<String, String> courseInfo : facultyCourses) {
                    String subjectCode = courseInfo.getKey();
                    String courseNumber = courseInfo.getValue();
                    System.out.println("Processing course: Subject=" + subjectCode + ", Number=" + courseNumber);
                    
                    try {
                        Course course = CourseManager.getCourse(subjectCode, courseNumber);
                        String fullCourseCode = subjectCode + "*" + courseNumber;
                        if (!searchTerm.isEmpty() && 
                        courseNumber = codeParts[1];
                    } else {
                        subjectCode = fullCourseCode;
                        courseNumber = "0000"; // Default course number if not provided
                        System.out.println("Using default format for: " + fullCourseCode);
                    }
                    
                    try {
                        Course course = CourseManager.getCourse(subjectCode, courseNumber);
                        if (!searchTerm.isEmpty() && 
                            !fullCourseCode.toLowerCase().contains(searchTerm.toLowerCase()) &&
                            !course.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
                            continue;
                        }
                        addCourseRow(course, subjectCode, courseNumber, rowIndex++);
                        System.out.println("Added course row: " + subjectCode + "*" + courseNumber);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error loading course: " + subjectCode + "*" + courseNumber + " - " + e.getMessage());
                        if (searchTerm.isEmpty() || 
                            fullCourseCode.toLowerCase().contains(searchTerm.toLowerCase())) {
                            addCourseRow(null, subjectCode, courseNumber, rowIndex++);
                        }
                    }
                }
                errorLabel.setText("");
            } else {
                errorLabel.setText("No courses found for faculty: " + faculty.getID());
            }
        } else {
            errorLabel.setText("Current user is not a faculty member");
        }
    }

    private void addHeaderRow() {
        Label[] headers = {
            new Label("Subject"),
            new Label("Code"),
            new Label("Name"),
            new Label("Credits"),
            new Label("Actions")
        };

        String headerStyle = "-fx-font-weight: bold; -fx-font-size: 14px; " +
                           "-fx-text-fill: #941B0C; -fx-padding: 12 5 12 5; " +
                           "-fx-font-family: System; -fx-background-color: transparent;";

        for (int i = 0; i < headers.length; i++) {
            headers[i].setStyle(headerStyle);
            headers[i].setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(headers[i], Priority.ALWAYS);
            tableGrid.add(headers[i], i, 0);
        }

        Separator headerSeparator = new Separator();
        headerSeparator.setPadding(new Insets(5, 0, 5, 0));
        tableGrid.add(headerSeparator, 0, 1, tableGrid.getColumnCount(), 1);
    }

    private void addCourseRow(Course course, String subjectCode, String courseNumber, int rowIndex) {
        String name = course != null ? course.getTitle() : "Course not found";
        String credits = course != null ? String.valueOf(course.getCredits()) : "0.0";
        
        Label[] labels = {
            new Label(subjectCode),
            new Label(courseNumber),
            new Label(name),
            new Label(credits)
        };

        String labelStyle = "-fx-padding: 10 5 10 5; -fx-font-size: 13px; -fx-text-fill: #333333; -fx-font-family: System;";
        
        HBox rowContainer = new HBox();
        rowContainer.setMaxWidth(Double.MAX_VALUE);
        rowContainer.setStyle("-fx-background-color: transparent;");

        Button profileButton = new Button("Course Profile");
        profileButton.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; " + 
                             "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");
        
        profileButton.setOnMouseEntered(e -> {
            profileButton.setStyle("-fx-background-color: #F6AA1C; -fx-text-fill: #941B0C; " + 
                                 "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");
            profileButton.setEffect(new DropShadow(3, 0, 0, Color.rgb(0, 0, 0, 0.2)));
        });
        
        profileButton.setOnMouseExited(e -> {
            profileButton.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; " + 
                                 "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");
            profileButton.setEffect(null);
        });

        if (course != null) {
            profileButton.setOnAction(e -> handleViewCourseProfile(e, course));
        } else {
            profileButton.setDisable(true);
        }
        
        rowContainer.setOnMouseEntered(e -> 
            rowContainer.setStyle("-fx-background-color: #F6F6F6;"));
        
        rowContainer.setOnMouseExited(e -> 
            rowContainer.setStyle("-fx-background-color: transparent;"));

        tableGrid.add(rowContainer, 0, rowIndex * 2, tableGrid.getColumnCount(), 1);

        for (int i = 0; i < labels.length; i++) {
            labels[i].setStyle(labelStyle);
            labels[i].setMaxWidth(Double.MAX_VALUE);
            labels[i].setWrapText(i == 2); // Only wrap name column
            GridPane.setHgrow(labels[i], Priority.ALWAYS);
            tableGrid.add(labels[i], i, rowIndex * 2);
        }

        tableGrid.add(profileButton, 4, rowIndex * 2);

        Separator rowSeparator = new Separator();
        rowSeparator.setPadding(new Insets(0));
        rowSeparator.setStyle("-fx-background-color: #E0E0E0;");
        tableGrid.add(rowSeparator, 0, rowIndex * 2 + 1, tableGrid.getColumnCount(), 1);
    }

    private void handleViewCourseProfile(ActionEvent event, Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/admin/course_editor.fxml"));
            Parent content = loader.load();

            StackPane contentArea = null;
            Parent parent = ((Node) event.getSource()).getParent();
            while (parent != null) {
                if (parent.getId() != null && parent.getId().equals("contentArea")) {
                    contentArea = (StackPane) parent;
                    break;
                }
                parent = parent.getParent();
            }

            CourseEditorController controller = loader.getController();
            controller.loadDetails(null, course);
            controller.disableEditing();

            if (contentArea != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(content);
            }
        } catch (Exception e) {
            displayError(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().trim().toLowerCase();
        loadCourses(searchTerm);
    }

    @Override
    public void displayError(String err) {
        if (errorLabel != null) {
            errorLabel.setText(err);
            errorLabel.setVisible(true);
        }
    }
}
