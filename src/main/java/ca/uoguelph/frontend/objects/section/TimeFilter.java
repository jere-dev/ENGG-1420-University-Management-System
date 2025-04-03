package ca.uoguelph.frontend.objects.section;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * @author 180Sai
 */
public class TimeFilter implements UnaryOperator<TextFormatter.Change> {
    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        return null;
    }
}
