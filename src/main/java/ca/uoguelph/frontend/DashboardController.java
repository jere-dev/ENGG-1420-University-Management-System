package ca.uoguelph.frontend;

// Add these imports
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.logging.Level;

import ca.uoguelph.backend.*;
import ca.uoguelph.backend.login.LoginManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;  // Add this import
import javafx.animation.RotateTransition;
import javafx.util.Duration;

// Add these new imports
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.FadeTransition;

public class DashboardController {
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @FXML private VBox sideNav;
    @FXML private VBox navItems;
    @FXML private StackPane contentArea; // TODO: make contentArea visible to all controllers in order to clear content effectively
    @FXML private Label dashboardLabel;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private VBox menuIcon;
    @FXML private Button toggleNavButton;
    @FXML private ImageView universityLogo;

    private AnchorPane activeNav;
    private boolean isExpanded = false;  // Changed from true to false
    private Timeline sidebarAnimation;

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

        // Start with bars
        transformToBar(menuIcon.getChildren().toArray(new Rectangle[0]));
        
        // Trigger initial collapse
        sideNav.setPrefWidth(60.0);
        universityLogo.setVisible(false);
        universityLogo.setManaged(false);
        
        // Initially hide labels and show collapsed lines
        navItems.getChildren().forEach(node -> {
            if (node instanceof AnchorPane) {
                AnchorPane anchor = (AnchorPane) node;
                anchor.getChildren().forEach(child -> {
                    if (child instanceof Label) {
                        child.setVisible(false);
                        child.setManaged(false);
                    } else if (child instanceof Line) {
                        if (child.getId() != null && child.getId().contains("Collapsed")) {
                            child.setVisible(true);
                            child.setManaged(true);
                        } else {
                            child.setVisible(false);
                            child.setManaged(false);
                        }
                    }
                });
            }
        });
        
        // Hide user info labels
        userNameLabel.setVisible(false);
        userRoleLabel.setVisible(false);

        // Add clip to prevent overflow during animation
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(sideNav.widthProperty());
        clip.heightProperty().bind(sideNav.heightProperty());
        sideNav.setClip(clip);
        
        // Initialize the sidebar animation
        sidebarAnimation = new Timeline();
        sidebarAnimation.setOnFinished(e -> {
            if (!isExpanded) {
                universityLogo.setVisible(false);
                universityLogo.setManaged(false);
            }
        });
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
        
//        if (label != null) {
//            switch (label.getText()) {
//                case "DASHBOARD": return "dashboard_content.fxml";
//                case "SUBJECTS": return "subject_manager.fxml";
//                case "COURSES": return "course_manager.fxml";
//                case "STUDENT": return "student_manager.fxml";
//                case "FACULTY": return "faculty_manager.fxml";
//                case "EVENTS": return "event_manager.fxml";
//                default: return "dashboard_content.fxml";
//            }
//        }
        if (label == null) return "dashboard_content.fxml";

        return FXMLPath.getFXMLPath(switch (label.getText()) {
            case "SUBJECTS" -> FXMLPath.SUBJECTS;
            case "COURSES" -> FXMLPath.COURSES;
            case "STUDENT" -> FXMLPath.STUDENTS;
            case "FACULTY" -> FXMLPath.FACULTY;
            case "EVENTS" -> FXMLPath.EVENTS;
            default -> FXMLPath.DASHBOARD_CONTENT;
        });
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
        
        sidebarAnimation.stop();
        
        if (isExpanded) {
            // First fade out the text and logo
            universityLogo.setVisible(true); // Keep logo visible during animation
            universityLogo.setManaged(true);
            fadeTransition(universityLogo, false);
            toggleVisibility(false);
            
            // Then animate the width after a short delay
            Timeline delayedCollapse = new Timeline(
                new KeyFrame(Duration.millis(150),
                    e -> animateSidebar(sideNav.getPrefWidth(), collapsedWidth, 250)
                )
            );
            delayedCollapse.play();
        } else {
            // Show logo before expanding
            universityLogo.setVisible(true);
            universityLogo.setManaged(true);
            universityLogo.setOpacity(0);
            
            // Animate the width
            animateSidebar(sideNav.getPrefWidth(), expandedWidth, 250);
            
            // Fade in elements after width animation starts
            Timeline delayedShow = new Timeline(
                new KeyFrame(Duration.millis(100),
                    e -> {
                        fadeTransition(universityLogo, true);
                        toggleVisibility(true);
                    }
                )
            );
            delayedShow.play();
        }
        
        toggleMenuIcon();
        isExpanded = !isExpanded;
    }

    private void animateSidebar(double startWidth, double endWidth, double durationMs) {
        sidebarAnimation.getKeyFrames().clear();
        sidebarAnimation.getKeyFrames().add(
            new KeyFrame(Duration.ZERO,
                new KeyValue(sideNav.prefWidthProperty(), startWidth)
            )
        );
        sidebarAnimation.getKeyFrames().add(
            new KeyFrame(Duration.millis(durationMs),
                new KeyValue(sideNav.prefWidthProperty(), endWidth)
            )
        );
        sidebarAnimation.play();
    }

    private void toggleVisibility(boolean visible) {
        navItems.getChildren().forEach(node -> {
            if (node instanceof AnchorPane) {
                AnchorPane anchor = (AnchorPane) node;
                anchor.getChildren().forEach(child -> {
                    if (child instanceof Label) {
                        fadeTransition(child, visible);
                    } else if (child instanceof ImageView && child != universityLogo) {
                        // Keep navigation icons always visible
                        child.setVisible(true);
                        child.setManaged(true);
                    } else if (child instanceof Line) {
                        boolean isCollapsedLine = child.getId() != null && child.getId().contains("Collapsed");
                        fadeTransition(child, isCollapsedLine != visible);
                    }
                });
            }
        });
        
        fadeTransition(userNameLabel, visible);
        fadeTransition(userRoleLabel, visible);
    }

    private void setLabelsVisibility(boolean visible) {
        navItems.getChildren().forEach(node -> {
            if (node instanceof AnchorPane) {
                AnchorPane anchor = (AnchorPane) node;
                anchor.getChildren().forEach(child -> {
                    if (child instanceof Label) {
                        child.setVisible(visible);
                        child.setManaged(visible);
                    } else if (child instanceof ImageView) {
                        // Keep icons always visible and managed
                        child.setVisible(true);
                        child.setManaged(true);
                    } else if (child instanceof Line) {
                        boolean isCollapsedLine = child.getId() != null && child.getId().contains("Collapsed");
                        child.setVisible(isCollapsedLine != visible);
                        child.setManaged(isCollapsedLine != visible);
                    }
                });
            }
        });
        
        userNameLabel.setVisible(visible);
        userRoleLabel.setVisible(visible);
    }

    private void fadeTransition(Node node, boolean visible) {
        if (node == null) return;
        
        FadeTransition fade = new FadeTransition(Duration.millis(200), node);
        fade.setFromValue(node.getOpacity());
        fade.setToValue(visible ? 1.0 : 0.0);
        
        // Keep node visible during fade out
        if (!visible) {
            fade.setOnFinished(e -> {
                node.setVisible(false);
                node.setManaged(false);
            });
        } else {
            node.setVisible(true);
            node.setManaged(true);
        }
        
        fade.play();
    }

    private void setLabelsVisibility(boolean visible, Label... labels) {
        for (Label label : labels) {
            if (label != null) {
                label.setVisible(visible);
            }
        }
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

    private void loadContent(String fxmlFile) {
        try {
            String resourcePath = "/assets/fxml/" + fxmlFile;
//            LOGGER.info("Loading resource: " + resourcePath);
            
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

    public void setUserInfo() {
        userNameLabel.setText(LoginManager.getCurrentUser().getName());
        userRoleLabel.setText(switch (LoginManager.getCurrentUser()) {
            case Admin ignored -> "Administrator";
            case Faculty ignored1 -> "Faculty";
            case Student ignored2 -> "Student";
            default -> throw new IllegalStateException("Unexpected value: " + LoginManager.getCurrentUser());
        });
    }
}