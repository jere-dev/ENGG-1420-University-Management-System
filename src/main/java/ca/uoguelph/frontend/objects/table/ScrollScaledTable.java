package ca.uoguelph.frontend.objects.table;

import ca.uoguelph.frontend.objects.table.row.TableRow;
import javafx.scene.control.ScrollPane;

/**
 * Encapsulates the features of JavaFX {@code GridPane} to display a table of interactive content inside a JavaFX {@code ScrollPane}.
 * <p>
 * A {@code TablePane} receives {@code TableRow} objects including JavaFX nodes and dynamically
 * updates when {@code TableRow} objects are added, edited, or removed. However, dynamic updates do not exist
 * for objects inside a {@code TableRow} that are added, edited, or removed.
 *
 * @see TableRow
 * @author 180Sai
 */
public final class ScrollScaledTable extends ScaledTable<ScrollPane> {
    public ScrollScaledTable(ScrollPane pane, int columnCount) {
        super(pane, columnCount);

        if (!pane.isFitToWidth()) pane.setFitToWidth(true); // if user has not set option then it is automatically done

        pane.setContent(tableGrid);
    }

    public ScrollScaledTable(ScrollPane pane, TableRow[] rows) {
        this(pane, rows[0].size());
        for (TableRow row : rows) {
            addRow(row);
        }
    }
}