package ca.uoguelph.frontend.objects.section;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

/**
 * A text formatter filter for date input that enforces a specific format pattern.
 * Implements {@link UnaryOperator} to modify {@link TextFormatter.Change} objects
 * for consistent date formatting.
 *
 * <p>The filter automatically:
 * <ul>
 *   <li>Converts various separator inputs (" ", "/", "\n") to " / "</li>
 *   <li>Adjusts caret position when navigating between components</li>
 *   <li>Prevents invalid input patterns</li>
 * </ul>
 *
 * @author 180Sai
 * @see TextFormatter
 * @see UnaryOperator
 */
public class DateFilter implements UnaryOperator<TextFormatter.Change> {
    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        if (change.isAdded() || change.isReplaced()) {
            onAdd(change); setRightEnd(change);
            return change;
        } else if (change.isDeleted()) {
            onDelete(change); setLeftEnd(change);
            return change;
        }

        // do not allow more than three data points
        String[] data = change.getControlNewText().split("/");
        int subLength = 0;
        if (data.length > 3) for (String datum : data)
            subLength += datum.length();
        change.setText(change.getControlNewText().substring(0, subLength));

        return change;
    }

    private void setRightEnd(TextFormatter.Change c) {
        String result = c.getControlNewText();
        int right = Math.max(c.getCaretPosition(), c.getAnchor());

        // Move to end of " / " group
        if (result.charAt(right) == ' ' && result.charAt(right+1) == '/') {
            // Case 1: left-side " "
            right += 2;
        } else if (result.charAt(right) == '/') {
            // Case 2: middle "/"
            right += 1;
        }

        if (c.getCaretPosition() == c.getAnchor()) c.setAnchor(right);
        c.setCaretPosition(right);
    }

    private void setLeftEnd(TextFormatter.Change c) {
        String result = c.getControlNewText();
        int left = Math.min(c.getCaretPosition(), c.getAnchor());

        // Move to start of " / " group
        if (result.charAt(left) == ' ' && result.charAt(left-1) == '/') {
            // Case 1: right-side " "
            left -= 2;
        } else if (result.charAt(left) == '/') {
            // Case 2: middle "/"
            left -= 1;
        }

        if (c.getCaretPosition() == c.getAnchor()) c.setAnchor(left);
        c.setCaretPosition(left);
    }

    private void onDelete(TextFormatter.Change c) {
        String initial = c.getControlText();

        int newStart = c.getRangeStart(),
                newEnd = c.getRangeEnd();

        // Capture group on left side
        if (initial.charAt(c.getRangeStart()) == ' ' && initial.charAt(c.getRangeStart()-1) == '/') {
            // Case 1: right-side " " - delete two extra positions
            newStart -= 2;
        } else if (initial.charAt(c.getRangeStart()) == '/') {
            // Case 2: middle "/"
            newStart -= 1;
        }

        // Capture group on right side
        if (initial.charAt(c.getRangeStart()) == ' ' && initial.charAt(c.getRangeStart()+1) == '/') {
            // Case 1: left-side " " - delete two extra positions
            newEnd += 2;
        } else if (initial.charAt(c.getRangeStart()) == '/') {
            // Case 2: middle "/"
            newEnd += 1;
        }

        c.setRange(newStart, newEnd);
    }

    private void onAdd(TextFormatter.Change change) {
        change.setRange(0, 0);

        // convert " ", "\n", and "/" to " / " (see regex101.com with pattern for full details)
        change.setText(change.getText().replaceAll("\n+?| / |/| ( +|/)", " / "));
    }
}
