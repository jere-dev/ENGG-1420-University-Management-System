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
import javafx.scene.layout.AnchorPane;
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
import javafx.scene.shape.Line;  // Add this import
import javafx.animation.RotateTransition;
import javafx.util.Duration;

public class DashboardController {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @FXML private VBox sideNav;
    @FXML private VBox navItems;
    @FXML private StackPane contentArea;
    @FXML private Label dashboardLabel;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private VBox menuIcon;
    @FXML private Button toggleNavButton;
    @FXML private ImageView universityLogo;
    
    private String currentUser;
    private String userRole;
    private AnchorPane activeNav;
    private boolean isExpanded = true;

    @FXML
    private void initialize() {
        // Set up click handlers for navigation items
        navItems.getChildren().stream()
                .filter(node -> node instanceof AnchorPane)
                .map(node -> (AnchorPane)node)
                .forEach(this::setupNavItem);

        // Set initial state
        setActiveNav((AnchorPane)navItems.getChildren().get(0));
        loadContent("dashboard_content.fxml");
        
        // Start with bars instead of cross
        transformToBar(menuIcon.getChildren().toArray(new Rectangle[0]));
    }

    private void setupNavItem(AnchorPane nav) {
        nav.setOnMouseEntered(e -> {
            if (nav != activeNav) {
                nav.setStyle("-fx-background-color: #7B1609; -fx-cursor: hand;");
            }
        });
        
        nav.setOnMouseExited(e -> {
            if (nav != activeNav) {
                nav.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            }
        });

        nav.setOnMouseClicked(e -> {
            setActiveNav(nav);
            String contentFile = determineContentFile(nav);
            loadContent(contentFile);
        });
    }

    private String determineContentFile(AnchorPane nav) {
        Label label = (Label)nav.getChildren().stream()
                .filter(node -> node instanceof Label)
                .findFirst()
                .orElse(null);
        
        if (label != null) {
            switch (label.getText()) {
                case "DASHBOARD": return "dashboard_content.fxml";
                case "SUBJECTS": return "subject_manager_admin.fxml";
                case "COURSES": return "course_manager_admin.fxml";
                case "STUDENT": return "student_list.fxml";
                case "Faculty": return "faculty_list.fxml";
                case "EVENTS": return "event_manager_admin.fxml";
                default: return "dashboard_content.fxml";
            }
        }
        return "dashboard_content.fxml";
    }

    private void setActiveNav(AnchorPane nav) {
        if (activeNav != null) {
            activeNav.setStyle("-fx-background-color: transparent;");
        }
        activeNav = nav;
        activeNav.setStyle("-fx-background-color: #941B0C;");
    }

    @FXML
    private void toggleNavigation(ActionEvent event) {
        double expandedWidth = 200.0;
        double collapsedWidth = 60.0;
        
        if (isExpanded) {
            sideNav.setPrefWidth(collapsedWidth);
            universityLogo.setVisible(false);
            universityLogo.setManaged(false);
            navItems.getChildren().forEach(node -> {
                if (node instanceof AnchorPane) {
                    AnchorPane anchor = (AnchorPane) node;
                    anchor.setBackground(null);  // Remove background when collapsed
                    anchor.getChildren().forEach(child -> {
                        if (child instanceof Label) {
                            child.setVisible(false);
                            child.setManaged(false);
                        } else if (child instanceof Line) {
                            // Check if it's a collapsed line
                            if (child.getId() != null && child.getId().contains("Collapsed")) {
                                child.setVisible(true);
                                child.setManaged(true);
                            } else {
                                child.setVisible(false);
                                child.setManaged(false);
                            }
                        } else if (child instanceof ImageView) {
                            // Keep images visible and in position
                            child.setVisible(true);
                            child.setManaged(true);
                        }
                    });
                }
            });
            userNameLabel.setVisible(false);
            userRoleLabel.setVisible(false);
        } else {
            sideNav.setPrefWidth(expandedWidth);
            universityLogo.setVisible(true);
            universityLogo.setManaged(true);
            navItems.getChildren().forEach(node -> {
                if (node instanceof AnchorPane) {
                    AnchorPane anchor = (AnchorPane) node;
                    anchor.getChildren().forEach(child -> {
                        if (child instanceof Label) {
                            child.setVisible(true);
                            child.setManaged(true);
                        } else if (child instanceof Line) {
                            // Show regular lines, hide collapsed ones
                            if (child.getId() != null && child.getId().contains("Collapsed")) {
                                child.setVisible(false);
                            } else {
                                child.setVisible(true);
                            }
                        }
                    });
                }
            });
            userNameLabel.setVisible(true);
            userRoleLabel.setVisible(true);
        }
        
        toggleMenuIcon();
        isExpanded = !isExpanded;
    }

    private void toggleMenuIcon() {
        Rectangle[] bars = menuIcon.getChildren().toArray(new Rectangle[0]);
        if (isExpanded) {
            transformToBar(bars);  // When collapsing, show bars
        } else {
            transformToCross(bars);  // When expanding, show cross
        }
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