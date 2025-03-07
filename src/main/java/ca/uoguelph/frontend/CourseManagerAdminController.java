package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class CourseManagerAdminController {
    private static final Logger log = LogManager.getLogger(CourseManagerAdminController.class);

    // Data class to hold course information with 8 fields
    private static class Entry {
        private final SimpleStringProperty courseCode, sectionNumber, professorName, capacity, lectureTime, lectureLocation, examDate, examLocation;

        private Entry(String courseCode, String sectionNumber, String professorName, String capacity,
                      String lectureTime, String lectureLocation, String examDate, String examLocation) {
            this.courseCode = new SimpleStringProperty(courseCode);
            this.sectionNumber = new SimpleStringProperty(sectionNumber);
            this.professorName = new SimpleStringProperty(professorName);
            this.capacity = new SimpleStringProperty(capacity);
            this.lectureTime = new SimpleStringProperty(lectureTime);
            this.lectureLocation = new SimpleStringProperty(lectureLocation);
            this.examDate = new SimpleStringProperty(examDate);
            this.examLocation = new SimpleStringProperty(examLocation);
        }

        private String getCourseCode() { return courseCode.get(); }
        private String getSectionNumber() { return sectionNumber.get(); }
        private String getProfessorName() { return professorName.get(); }
        private String getCapacity() { return capacity.get(); }
        private String getLectureTime() { return lectureTime.get(); }
        private String getLectureLocation() { return lectureLocation.get(); }
        private String getExamDate() { return examDate.get(); }
        private String getExamLocation() { return examLocation.get(); }
    }

    // Hardcoded dataset converted to Entry array with 8 fields
    private final Entry[] entries = {
            new Entry("Course Code", "Section Number", "Teacher Name", "Capacity", "Lecture Time", "Lecture Location", "Exam Date", "Exam Location"),
            new Entry("1234 AGAR", "010101", "J Ballin", "707", "m/w/f (08:30 - 09:20)", "ROZ*104", "2026/04/11 (11:30 - 12:30)", "ROZ*104")
    };

    @FXML private GridPane tableGrid;

    @FXML
    public void initialize() {
        // Clear any existing constraints to avoid duplication
        tableGrid.getColumnConstraints().clear();
        tableGrid.getRowConstraints().clear();

        // Ensure GridPane fills its ScrollPane
        tableGrid.setMaxWidth(Double.MAX_VALUE);
        tableGrid.setMaxHeight(Double.MAX_VALUE);

        // Set up column constraints to allow dynamic resizing
        int totalColumns = 9; // 8 data columns + 1 button column
        for (int i = 0; i < totalColumns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(100); // Minimum width to ensure readability
            col.setHgrow(Priority.ALWAYS);
            tableGrid.getColumnConstraints().add(col);
        }
        System.out.println("Columns set: " + tableGrid.getColumnConstraints().size());

        // Populate the GridPane
        for (int i = 0; i < entries.length; i++) {
            tableGrid.addRow(i + 1); // Start at row 1 like example

            // Set row constraint to allow dynamic height
            RowConstraints row = new RowConstraints();
            row.setMinHeight(30);
            row.setPrefHeight(60);
            row.setVgrow(Priority.ALWAYS);
            tableGrid.getRowConstraints().add(row);

            // Create TextAreas for each field
            TextArea courseCode = new TextArea(entries[i].getCourseCode());
            TextArea sectionNumber = new TextArea(entries[i].getSectionNumber());
            TextArea professorName = new TextArea(entries[i].getProfessorName());
            TextArea capacity = new TextArea(entries[i].getCapacity());
            TextArea lectureTime = new TextArea(entries[i].getLectureTime());
            TextArea lectureLocation = new TextArea(entries[i].getLectureLocation());
            TextArea examDate = new TextArea(entries[i].getExamDate());
            TextArea examLocation = new TextArea(entries[i].getExamLocation());
            Button editButton = new Button("✎");

            // Add to GridPane
            tableGrid.add(courseCode, 0, i + 1);
            tableGrid.add(sectionNumber, 1, i + 1);
            tableGrid.add(professorName, 2, i + 1);
            tableGrid.add(capacity, 3, i + 1);
            tableGrid.add(lectureTime, 4, i + 1);
            tableGrid.add(lectureLocation, 5, i + 1);
            tableGrid.add(examDate, 6, i + 1);
            tableGrid.add(examLocation, 7, i + 1);
            tableGrid.add(editButton, 8, i + 1);

            // Configure TextAreas to prevent overlap
            configureTextArea(courseCode);
            configureTextArea(sectionNumber);
            configureTextArea(professorName);
            configureTextArea(capacity);
            configureTextArea(lectureTime);
            configureTextArea(lectureLocation);
            configureTextArea(examDate);
            configureTextArea(examLocation);

            // Button constraints
            GridPane.setHgrow(editButton, Priority.NEVER);
            GridPane.setVgrow(editButton, Priority.NEVER);
            GridPane.setFillWidth(editButton, false);
            GridPane.setFillHeight(editButton, false);
            GridPane.setMargin(editButton, new Insets(5));
            editButton.setPadding(new Insets(5, 10, 5, 10));
            editButton.setStyle("-fx-font-size: 14px;");

            // Attach edit handler
            editButton.setOnAction(event -> handleEditCourse(event));
        }
    }

    private void configureTextArea(TextArea textArea) {
        textArea.setWrapText(false);
        textArea.setStyle("-fx-font-size: 14px; -fx-vbar-policy: never; -fx-hbar-policy: never;");
        textArea.setPrefRowCount(1);
        textArea.setPrefColumnCount(1);
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            double textWidth = computeTextWidth(newText, textArea.getFont());
            textArea.setPrefColumnCount((int) (textWidth / 7) + 1);
            textArea.setPrefWidth(textWidth + 20);
        });
        GridPane.setHgrow(textArea, Priority.ALWAYS);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setFillWidth(textArea, true);
        GridPane.setFillHeight(textArea, true);
        GridPane.setMargin(textArea, new Insets(5));
    }

    private double computeTextWidth(String text, javafx.scene.text.Font font) {
        javafx.scene.text.Text tempText = new javafx.scene.text.Text(text);
        tempText.setFont(font);
        return tempText.getBoundsInLocal().getWidth();
    }

    private Node searchObjects(int row, int col) {
        for (Node node : tableGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == null || GridPane.getColumnIndex(node) == null) continue;
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                System.out.println("Found child " + node.getClass() + " at (" + row + ", " + col + ")");
                return node;
            }
        }
        System.out.println("Child not found at (" + row + ", " + col + ")");
        return null;
    }

    @FXML
    private void handleEditCourse(ActionEvent event) {
        System.out.println("Edit Course clicked");
        try {
            int sourceRow = GridPane.getRowIndex((Button) event.getSource());
            TextArea courseCode = (TextArea) searchObjects(sourceRow, 0);
            TextArea sectionNumber = (TextArea) searchObjects(sourceRow, 1);
            TextArea professorName = (TextArea) searchObjects(sourceRow, 2);
            TextArea capacity = (TextArea) searchObjects(sourceRow, 3);
            TextArea lectureTime = (TextArea) searchObjects(sourceRow, 4);
            TextArea lectureLocation = (TextArea) searchObjects(sourceRow, 5);
            TextArea examDate = (TextArea) searchObjects(sourceRow, 6);
            TextArea examLocation = (TextArea) searchObjects(sourceRow, 7);

            if (courseCode == null || sectionNumber == null || professorName == null ||
                    capacity == null || lectureTime == null || lectureLocation == null ||
                    examDate == null || examLocation == null) {
                handleLoadEditor(event);
            } else {
                handleLoadEditor(courseCode.getText(), sectionNumber.getText(), professorName.getText(),
                        capacity.getText(), lectureTime.getText(), lectureLocation.getText(),
                        examDate.getText(), examLocation.getText(), event, sourceRow);
            }
        } catch (Exception e) {
            log.error("Error in handleEditCourse", e);
        }
    }

    private void handleLoadEditor(ActionEvent event) {
        handleLoadEditor("", "", "", "", "", "", "", "", event, -1);
    }

    private void handleLoadEditor(String courseCode, String sectionNumber, String professorName,
                                  String capacity, String lectureTime, String lectureLocation,
                                  String examDate, String examLocation, ActionEvent event, int row) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/course_editor.fxml"));
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
            if (contentArea == null) throw new RuntimeException("Could not find parent StackPane");

            CourseEditorController controller = loader.getController();
            controller.loadCourse(courseCode, sectionNumber, professorName, capacity,
                    lectureTime, lectureLocation, examDate, examLocation);
            controller.setParentAndRow(this, row);

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        } catch (IOException e) {
            log.error("Error loading course editor", e);
        }
    }

    public void updateRow(int row, String courseCode, String sectionNumber, String professorName,
                          String capacity, String lectureTime, String lectureLocation,
                          String examDate, String examLocation) {
        TextArea courseCodeText = (TextArea) searchObjects(row, 0);
        TextArea sectionNumberText = (TextArea) searchObjects(row, 1);
        TextArea professorNameText = (TextArea) searchObjects(row, 2);
        TextArea capacityText = (TextArea) searchObjects(row, 3);
        TextArea lectureTimeText = (TextArea) searchObjects(row, 4);
        TextArea lectureLocationText = (TextArea) searchObjects(row, 5);
        TextArea examDateText = (TextArea) searchObjects(row, 6);
        TextArea examLocationText = (TextArea) searchObjects(row, 7);

        if (courseCodeText != null) courseCodeText.setText(courseCode);
        if (sectionNumberText != null) sectionNumberText.setText(sectionNumber);
        if (professorNameText != null) professorNameText.setText(professorName);
        if (capacityText != null) capacityText.setText(capacity);
        if (lectureTimeText != null) lectureTimeText.setText(lectureTime);
        if (lectureLocationText != null) lectureLocationText.setText(lectureLocation);
        if (examDateText != null) examDateText.setText(examDate);
        if (examLocationText != null) examLocationText.setText(examLocation);
    }
}