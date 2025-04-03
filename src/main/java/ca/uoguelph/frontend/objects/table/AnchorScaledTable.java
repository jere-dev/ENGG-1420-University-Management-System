package ca.uoguelph.frontend.objects.table;

import ca.uoguelph.frontend.objects.table.row.TableRow;
import javafx.scene.layout.AnchorPane;

/**
 * A specialized {@link ScaledTable} implementation that uses an {@link AnchorPane} as its container.
 * This class provides convenient constructors for creating scaled tables within AnchorPane layouts.
 *
 * <p>The table automatically adds itself to the provided AnchorPane's children during construction.
 * Two constructors are provided for different initialization scenarios:
 * <ul>
 *   <li>Empty table with specified column count</li>
 *   <li>Pre-populated table from an array of {@link TableRow} objects</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>
 * {@code
 * AnchorPane pane = new AnchorPane();
 * AnchorScaledTable table = new AnchorScaledTable(pane, 3);
 * table.addRow(new TableRow("Header1", "Header2", "Header3"));
 * }
 * </pre>
 *
 * @see ScaledTable
 * @see TableRow
 * @author 180Sai
 */
public final class AnchorScaledTable extends ScaledTable<AnchorPane> {
    /**
     * Constructs an empty AnchorScaledTable with the specified number of columns.
     * The table will be automatically added to the provided AnchorPane's children.
     *
     * @param pane the AnchorPane container for this table
     * @param columnCount the number of columns in the table
     */
    public AnchorScaledTable(AnchorPane pane, int columnCount) {
        super(pane, columnCount);

        pane.getChildren().add(tableGrid);
    }

    /**
     * Constructs an AnchorScaledTable pre-populated with the provided rows.
     * The column count is determined by the first row's size.
     *
     * @param pane the AnchorPane container for this table
     * @param rows array of TableRow objects to populate the table
     */
    public AnchorScaledTable(AnchorPane pane, TableRow[] rows) {
        this(pane, rows[0].size());
        for (TableRow row : rows) {
            addRow(row);
        }
    }
}