package ca.uoguelph.frontend;

import ca.uoguelph.backend.login.LoginManager;
import ca.uoguelph.backend.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

public class LoginController {
    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView backgroundSharp;

    @FXML
    private ImageView backgroundBlurred;

    @FXML
    private VBox loginBox;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        // Wait until the rootPane is added to a scene
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Bind the image size to the larger dimension of the scene
                newScene.widthProperty().addListener((obs2, oldWidth, newWidth) -> {
                    scaleImageToCover(backgroundSharp, newScene.getWidth(), newScene.getHeight());
                    scaleImageToCover(backgroundBlurred, newScene.getWidth(), newScene.getHeight());
                    centerLoginBox(newScene.getWidth(), newScene.getHeight());
                });

                newScene.heightProperty().addListener((obs2, oldHeight, newHeight) -> {
                    scaleImageToCover(backgroundSharp, newScene.getWidth(), newScene.getHeight());
                    scaleImageToCover(backgroundBlurred, newScene.getWidth(), newScene.getHeight());
                    centerLoginBox(newScene.getWidth(), newScene.getHeight());
                });

                // Initial scaling and centering
                scaleImageToCover(backgroundSharp, newScene.getWidth(), newScene.getHeight());
                scaleImageToCover(backgroundBlurred, newScene.getWidth(), newScene.getHeight());
                centerLoginBox(newScene.getWidth(), newScene.getHeight());

                // Clipping rectangle for blurred background
                Rectangle clipRect = new Rectangle();
                clipRect.widthProperty().bind(loginBox.widthProperty());
                clipRect.heightProperty().bind(loginBox.heightProperty());
                clipRect.layoutXProperty().bind(loginBox.layoutXProperty());
                clipRect.layoutYProperty().bind(loginBox.layoutYProperty());
                backgroundBlurred.setClip(clipRect);
            }
        });
    }

    /**
     * Scales the image to cover the entire window while maintaining aspect ratio.
     */
    private void scaleImageToCover(ImageView imageView, double sceneWidth, double sceneHeight) {
        if (imageView.getImage() == null)
            return;

        double imageWidth = imageView.getImage().getWidth();
        double imageHeight = imageView.getImage().getHeight();

        // Calculate the scaling factor
        double widthRatio = sceneWidth / imageWidth;
        double heightRatio = sceneHeight / imageHeight;

        // Use the larger scaling factor to ensure the image covers the entire window
        double scale = Math.max(widthRatio, heightRatio);

        // Apply the scaling
        imageView.setFitWidth(imageWidth * scale);
        imageView.setFitHeight(imageHeight * scale);
    }

    /**
     * Centers the login box within the window.
     */
    private void centerLoginBox(double sceneWidth, double sceneHeight) {
        double loginBoxWidth = loginBox.getWidth();
        double loginBoxHeight = loginBox.getHeight();

        // Calculate the X and Y positions to center the login box
        double x = (sceneWidth - loginBoxWidth) / 2;
        double y = (sceneHeight - loginBoxHeight) / 2;

        // Set the position of the login box
        loginBox.setLayoutX(x);
        loginBox.setLayoutY(y);
    }

    private HBox createLoadingDots() {
        HBox dotsContainer = new HBox(5);
        dotsContainer.setAlignment(javafx.geometry.Pos.CENTER);
        
        for (int i = 0; i < 3; i++) {
            Circle dot = new Circle(3, Color.WHITE);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.4), dot);
            tt.setByY(-10);
            tt.setCycleCount(Timeline.INDEFINITE);
            tt.setAutoReverse(true);
            tt.setInterpolator(Interpolator.EASE_BOTH);
            tt.setDelay(Duration.seconds(i * 0.1));
            tt.play();
            dotsContainer.getChildren().add(dot);
        }
        
        return dotsContainer;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String originalText = loginButton.getText();
        
        // Disable form
        loginButton.setDisable(true);
        usernameField.setDisable(true);
        passwordField.setDisable(true);
        
        // Show loading animation
        loginButton.setText("");
        loginButton.setGraphic(createLoadingDots());

        // Process login after a short delay to show animation
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            try {
                User user = LoginManager.login(username, password);
                
                // Load dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/dashboard.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Dashboard - Admin");
                stage.setMaximized(true);
                stage.setResizable(true);
                stage.show();
                
            } catch (IllegalArgumentException ex) {
                // Reset form on error
                loginButton.setText(originalText);
                loginButton.setGraphic(null);
                loginButton.setDisable(false);
                usernameField.setDisable(false);
                passwordField.setDisable(false);
                errorLabel.setText(ex.getMessage());
                errorLabel.setTextFill(Color.color(1, 0, 0));
            } catch (Exception ex) {
                // Reset form on error
                loginButton.setText(originalText);
                loginButton.setGraphic(null);
                loginButton.setDisable(false);
                usernameField.setDisable(false);
                passwordField.setDisable(false);
                ex.printStackTrace();
                System.out.println("Error loading Dashboard: " + ex.getMessage());
            }
        }));
        
        timeline.play();
    }

    @FXML
    public void keyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }
}
