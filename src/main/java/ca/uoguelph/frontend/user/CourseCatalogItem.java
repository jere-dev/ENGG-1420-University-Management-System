package ca.uoguelph.frontend.user;

import ca.uoguelph.backend.Course;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CourseCatalogItem extends HBox {
    private final Label subjectLabel;
    private final Label codeLabel;
    private final Label nameLabel;
    private final Label creditsLabel;
    private final Button profileButton;

    public CourseCatalogItem(Course course, String subjectCode, String courseNumber) {
        super(10); // 10px spacing between elements
        
        subjectLabel = new Label(subjectCode);
        codeLabel = new Label(courseNumber);
        nameLabel = new Label(course != null ? course.getTitle() : "Course not found");
        creditsLabel = new Label(course != null ? String.valueOf(course.getCredits()) : "0.0");
        profileButton = new Button("Course Profile");

        setupStyles();
        setupLayout();
        
        if (course == null) {
            profileButton.setDisable(true);
        }
    }

    private void setupStyles() {
        String labelStyle = "-fx-padding: 10 5 10 5; -fx-font-size: 13px; -fx-text-fill: #333333; -fx-font-family: System;";
        subjectLabel.setStyle(labelStyle);
        codeLabel.setStyle(labelStyle);
        nameLabel.setStyle(labelStyle);
        creditsLabel.setStyle(labelStyle);
        
        profileButton.setStyle("-fx-background-color: #941B0C; -fx-text-fill: #F6AA1C; " + 
                             "-fx-background-radius: 3; -fx-font-family: System; -fx-font-size: 13px;");

        this.setStyle("-fx-background-color: transparent;");
    }

    private void setupLayout() {
        nameLabel.setWrapText(true);
        
        getChildren().addAll(subjectLabel, codeLabel, nameLabel, creditsLabel, profileButton);
        
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        
        // Set max width for labels
        subjectLabel.setMaxWidth(Double.MAX_VALUE);
        codeLabel.setMaxWidth(Double.MAX_VALUE);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        creditsLabel.setMaxWidth(Double.MAX_VALUE);
    }

    public Button getProfileButton() {
        return profileButton;
    }
}
