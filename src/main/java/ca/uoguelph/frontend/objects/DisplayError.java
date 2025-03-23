package ca.uoguelph.frontend.objects;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.BooleanSupplier;

/**
 * Displays an error message.
 * <p>
 * By implementing this function, the controller is guaranteed to verify and
 * display errors to a user elsewhere than the terminal.
 * @author  180Sai
 */
public interface DisplayError {
    Logger log = LogManager.getLogger(DisplayError.class);

    /**
     * Displays an error. This function is left to be implemented
     * however is most suitable for the controller.
     */
    void displayError(String err);

    /**
     * Assumes a specific labelled target to display an error and displays it for a length of time.
     * <p>
     * Recommended to be used with or within {@code displayError}.
     * @param err   a one-line error message
     * @param errLabeled    the labelled target to display the error message
     */
    default void displayShortError(String err, Labeled errLabeled, double secondsDuration) {
        new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(errLabeled.textProperty(), err, Interpolator.DISCRETE)),
                new KeyFrame(Duration.seconds(secondsDuration), new KeyValue(errLabeled.textProperty(), "", Interpolator.DISCRETE)))
                .play();
    }

    /**
     * Tests and displays an error by calling {@code displayError} if the condition is true.
     * @param err   a one-line message
     * @param sup   the test functionality
     * @return  whether the function has produced an error.
     */
    default boolean errorIf(String err, BooleanSupplier sup) {
        if (sup.getAsBoolean()) displayError(err);
        return sup.getAsBoolean();
    }

    /**
     * Creates a popup error window with the thread name that lists the error trace.
     * Meant for significant errors that requires a restrictive notification.
     */
    static void createPopup(Thread t, Throwable e) {
        createPopup("Error thrown in thread from " + t.getThreadGroup().getName(), e);
    }

    /**
     * Creates a popup error window that lists the error trace.
     * Meant for significant errors that requires a restrictive notification.
     */
    static void createPopup(String title, Throwable e) {
        StringWriter s = new StringWriter();
        e.printStackTrace(new PrintWriter(s));
        createPopup(title, s.toString());

        log.error("", e);
    }

    /**
     * Creates a popup error window that lists the error trace.
     * Meant for significant errors that requires a restrictive notification.
     * @author  ASHRREAL
     */
    static void createPopup(String title, String message) {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 10; -fx-background-color: white;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold;");
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialog.close());

        Label captionLabel = new Label("An exception has occurred.");
        captionLabel.setStyle("-fx-font-weight: bold;");

        root.getChildren().addAll(titleLabel, messageLabel, captionLabel, closeButton);

        ScrollPane superRoot = new ScrollPane(root);
        superRoot.setPrefSize(800, 500);

        dialog.setScene(new Scene(superRoot));
        dialog.setTitle(DisplayError.class.getName());

        dialog.show();
    }
}
