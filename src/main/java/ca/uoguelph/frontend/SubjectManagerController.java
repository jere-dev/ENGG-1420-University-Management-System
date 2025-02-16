package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class SubjectManagerController {

    @FXML
    private Button addSubjectButton;

    @FXML
    private Button editSubjectButton;

    @FXML
    private Button deleteSubjectButton;

    @FXML
    private void handleAddSubject(ActionEvent event) {
        System.out.println("Add Subject clicked");
    }

    @FXML
    private void handleEditSubject(ActionEvent event) {
        System.out.println("Edit Subject clicked");
    }

    @FXML
    private void handleDeleteSubject(ActionEvent event) {
        System.out.println("Delete Subject clicked");
    }
}