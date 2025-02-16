package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class DashboardController {

    @FXML
    private Button logoutButton;

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Load the Login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/login.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene (Login)
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("University Management System - Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading Login screen: " + e.getMessage());
        }
    }
}