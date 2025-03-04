package ca.uoguelph.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "admin".equals(password)) {
            System.out.println("Login successful! Redirecting to Dashboard...");

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/dashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);


                stage.setScene(scene);
                stage.setTitle("Dashboard - Admin");
                stage.setMaximized(true);
                stage.setResizable(true);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading Dashboard: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid username or password!");
        }
    }
}
