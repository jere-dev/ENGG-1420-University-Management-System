package ca.uoguelph.frontend;

// Add these imports
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class DashboardController {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @FXML private VBox sideNav;
    @FXML private StackPane contentArea;
    @FXML private HBox dashboardNav;
    @FXML private HBox subjectNav;
    @FXML private HBox courseNav;
    @FXML private HBox studentNav;
    @FXML private HBox facultyNav;
    @FXML private HBox eventNav;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private VBox menuIcon;
    
    private String currentUser;
    private String userRole;
    private HBox activeNav;
    private boolean isExpanded = true;

    @FXML
    private void initialize() {
        // Set up hover effects for navigation
        setupHoverEffects(dashboardNav);
        setupHoverEffects(subjectNav);
        setupHoverEffects(courseNav);
        setupHoverEffects(studentNav);
        setupHoverEffects(facultyNav);
        setupHoverEffects(eventNav);

        // Set up event handlers using lambda expressions
        dashboardNav.setOnMouseClicked(e -> handleNavigation(e, "dashboard_content.fxml"));
        subjectNav.setOnMouseClicked(e -> handleNavigation(e, "subject_manager_admin.fxml"));
        courseNav.setOnMouseClicked(e -> handleNavigation(e, "course_manager_admin.fxml"));
        studentNav.setOnMouseClicked(e -> handleNavigation(e, "student_list.fxml"));
        facultyNav.setOnMouseClicked(e -> handleNavigation(e, "faculty_list.fxml"));
        eventNav.setOnMouseClicked(e -> handleNavigation(e, "event_manager_admin.fxml"));

        // Set initial active nav and content
        setActiveNav(dashboardNav);
        loadContent("dashboard_content.fxml");

        transformToCross(menuIcon.getChildren().toArray(new Rectangle[0])); // Start with cross
    }

    private void setupHoverEffects(HBox nav) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.WHITE);
        
        nav.setOnMouseEntered(e -> {
            if (nav != activeNav) {
                nav.setStyle("-fx-background-color: #990000; -fx-cursor: hand;");
                nav.setEffect(shadow);
            }
        });
        
        nav.setOnMouseExited(e -> {
            if (nav != activeNav) {
                nav.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                nav.setEffect(null);
            }
        });
    }

    private void setActiveNav(HBox nav) {
        if (activeNav != null) {
            activeNav.setStyle("-fx-background-color: transparent;");
            activeNav.setEffect(null);
        }
        
        activeNav = nav;
        activeNav.setStyle("-fx-background-color: #990000;");
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.WHITE);
        activeNav.setEffect(shadow);
    }

    private void handleNavigation(MouseEvent event, String fxmlPath) {
        HBox clickedNav = (HBox) event.getSource();
        setActiveNav(clickedNav);
        loadContent(fxmlPath);
    }

    @FXML
    private void toggleNavigation(ActionEvent event) {
        double expandedWidth = 200.0;
        double collapsedWidth = 60.0;
        
        Rectangle[] bars = menuIcon.getChildren().toArray(new Rectangle[0]);
        
        if (isExpanded) {
            // Collapse - transform to cross
            sideNav.setPrefWidth(collapsedWidth);
            transformToCross(bars);
        } else {
            // Expand - transform to bars
            sideNav.setPrefWidth(expandedWidth);
            transformToBar(bars);
        }
        
        // Toggle visibility of other elements
        ImageView universityLogo = (ImageView) sideNav.lookup("#universityLogo");
        Label dashboardLabel = (Label) sideNav.lookup("#dashboardLabel");
        Label subjectLabel = (Label) sideNav.lookup("#subjectLabel");
        Label courseLabel = (Label) sideNav.lookup("#courseLabel");
        Label studentLabel = (Label) sideNav.lookup("#studentLabel");
        Label facultyLabel = (Label) sideNav.lookup("#facultyLabel");
        Label eventLabel = (Label) sideNav.lookup("#eventLabel");
        Label userNameLabel = (Label) sideNav.lookup("#userNameLabel");
        Label userRoleLabel = (Label) sideNav.lookup("#userRoleLabel");
        
        if (sideNav.getPrefWidth() == expandedWidth) {
            sideNav.setPrefWidth(collapsedWidth);
            universityLogo.setManaged(false);
            universityLogo.setVisible(false);
            setLabelsVisibility(false, dashboardLabel, subjectLabel, courseLabel, 
                              studentLabel, facultyLabel, eventLabel, 
                              userNameLabel, userRoleLabel);
        } else {
            sideNav.setPrefWidth(expandedWidth);
            universityLogo.setManaged(true);
            universityLogo.setVisible(true);
            setLabelsVisibility(true, dashboardLabel, subjectLabel, courseLabel, 
                              studentLabel, facultyLabel, eventLabel, 
                              userNameLabel, userRoleLabel);
        }
        
        isExpanded = !isExpanded;
    }

    private void transformToCross(Rectangle[] bars) {
        // Middle bar fade out
        bars[1].setOpacity(0);
        
        // Rotate top bar
        RotateTransition rotateTop = new RotateTransition(Duration.millis(300), bars[0]);
        rotateTop.setToAngle(45);
        rotateTop.setOnFinished(e -> bars[0].setTranslateY(6));
        rotateTop.play();
        
        // Rotate bottom bar
        RotateTransition rotateBottom = new RotateTransition(Duration.millis(300), bars[2]);
        rotateBottom.setToAngle(-45);
        rotateBottom.setOnFinished(e -> bars[2].setTranslateY(-6));
        rotateBottom.play();
    }

    private void transformToBar(Rectangle[] bars) {
        // Restore middle bar
        bars[1].setOpacity(1);
        
        // Reset top bar
        RotateTransition rotateTop = new RotateTransition(Duration.millis(300), bars[0]);
        rotateTop.setToAngle(0);
        rotateTop.setOnFinished(e -> bars[0].setTranslateY(0));
        rotateTop.play();
        
        // Reset bottom bar
        RotateTransition rotateBottom = new RotateTransition(Duration.millis(300), bars[2]);
        rotateBottom.setToAngle(0);
        rotateBottom.setOnFinished(e -> bars[2].setTranslateY(0));
        rotateBottom.play();
    }

    private void setLabelsVisibility(boolean visible, Label... labels) {
        for (Label label : labels) {
            if (label != null) {
                label.setVisible(visible);
            }
        }
    }

    private void loadContent(String fxmlFile) {
        try {
            String resourcePath = "/assets/fxml/" + fxmlFile;
            LOGGER.info("Loading resource: " + resourcePath);
            
            // Try different ways to load the resource
            URL resource = DashboardController.class.getResource(resourcePath);
            if (resource == null) {
                resource = DashboardController.class.getClassLoader().getResource(resourcePath.substring(1));
            }
            if (resource == null) {
                LOGGER.severe("Resource not found: " + resourcePath);
                LOGGER.info("Class loader: " + DashboardController.class.getClassLoader());
                LOGGER.info("Working directory: " + System.getProperty("user.dir"));
                throw new IOException("Cannot find resource: " + resourcePath);
            }
            
            LOGGER.info("Resource found at: " + resource.toString());
            FXMLLoader loader = new FXMLLoader(resource);
            Parent content = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading content", e);
            showErrorDialog("Error loading content", e.getMessage() + "\nResource path: " + fxmlFile);
        }
    }
    private void showErrorDialog(String title, String message) {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10; -fx-background-color: white;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label messageLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());
        
        root.getChildren().addAll(titleLabel, messageLabel, closeButton);
        dialog.setScene(new Scene(root));
        dialog.show();
    }

    public void setUserInfo(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        
        if (userNameLabel != null) userNameLabel.setText(username);
        if (userRoleLabel != null) userRoleLabel.setText(role);
    }
}