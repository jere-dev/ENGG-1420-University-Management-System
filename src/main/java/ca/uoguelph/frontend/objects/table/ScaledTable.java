package ca.uoguelph.frontend.objects.table;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;

/**
 * Encapsulates the features of JavaFX {@code GridPane} to display a table of interactive content inside a JavaFX container object.
 * <p>
 * A {@code TablePane} receives {@code TableRow} objects including JavaFX nodes and dynamically
 * updates when {@code TableRow} objects are added, edited, or removed. However, dynamic updates do not exist
 * for objects inside a {@code TableRow} that are added, edited, or removed.
 *
 * @see TableRow
 * @author Eric Cao
 */
public class ScaledTable {
    private final GridPane tableGrid;

    private final int columnCount;
    private int rowCount = 0;

    private final DoubleProperty[] minWidths;
    private final DoubleProperty[] prefWidths;
    private final DoubleProperty[] maxWidths;
    private final DoubleProperty[] percentWidths;

    public ScaledTable(Pane pane, int columnCount) {
        this.columnCount = columnCount;

        tableGrid = new GridPane();

        // constraints
        minWidths = new DoubleProperty[columnCount];
        prefWidths = new DoubleProperty[columnCount];
        maxWidths = new DoubleProperty[columnCount];
        percentWidths = new DoubleProperty[columnCount];

        for (int i = 0; i < columnCount; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            tableGrid.getColumnConstraints().add(columnConstraints);
            minWidths[i] = columnConstraints.minWidthProperty();
            maxWidths[i] = columnConstraints.maxWidthProperty();
            prefWidths[i] = columnConstraints.prefWidthProperty();
            percentWidths[i] = columnConstraints.percentWidthProperty();
        }

        pane.getChildren().add(tableGrid);
    }


    public ScaledTable(Pane pane, TableRow[] rows) {
        this(pane, rows.length);
        for (TableRow row : rows) {
            addRow(row);
        }
    }

    private void configure(TableRow row) {
        for (Node node : row) {
            TableMemberConfig config = row.config(node);
            config.setFinal();

            GridPane.setMargin(node, config.getMargin());
            GridPane.setFillHeight(node, config.getFillHeight());
            GridPane.setFillWidth(node, config.getFillWidth());
            GridPane.setHgrow(node, config.getHGrow());
            GridPane.setVgrow(node, config.getVGrow());
            GridPane.setHalignment(node, config.getHAlignment());
            GridPane.setValignment(node, config.getVAlignment());
        }
    }

    public Insets padding() {
        return tableGrid.getPadding();
    }

    public double getHGap() {
        return tableGrid.getHgap();
    }

    public void setHGap(double hGap) {
        tableGrid.setHgap(hGap);
    }

    public double getVGap() {
        return tableGrid.getVgap();
    }

    public void setVGap(double vGap) {
        tableGrid.setVgap(vGap);
    }

    public int columnOf(Node node) {
        return GridPane.getColumnIndex(node);
    }

    public int rowOf(Node node) {
        return GridPane.getRowIndex(node);
    }

    public double[] getMinWidths() {
        double[] minWidths = new double[this.minWidths.length];
        for (int i = 0; i < minWidths.length; i++) minWidths[i] = this.minWidths[i].get();
        return minWidths;
    }

    public void setMinWidth(int column, double width) {
        minWidths[column].set(width);
    }

    public void setMinWidths(double width) {
        for (int i = 0; i < columnCount; i++) setMinWidth(i, width);
    }

    public double[] getPrefWidths() {
        double[] prefWidths = new double[this.prefWidths.length];
        for (int i = 0; i < prefWidths.length; i++) prefWidths[i] = this.prefWidths[i].get();
        return prefWidths;
    }

    public void setPrefWidth(int column, double width) {
        prefWidths[column].set(width);
    }

    public void setPrefWidths(double width) {
        for (int i = 0; i < columnCount; i++) setPrefWidth(i, width);
    }

    public double[] getMaxWidths() {
        double[] maxWidths = new double[this.maxWidths.length];
        for (int i = 0; i < maxWidths.length; i++) maxWidths[i] = this.maxWidths[i].get();
        return maxWidths;
    }

    public void setMaxWidth(int column, double width) {
        maxWidths[column].set(width);
    }

    public void setMaxWidths(double width) {
        for (int i = 0; i < columnCount; i++) setMaxWidth(i, width);
    }

    public double[] getPercentWidths() {
        double[] percentWidths = new double[this.percentWidths.length];
        for (int i = 0; i < percentWidths.length; i++) percentWidths[i] = this.percentWidths[i].get();
        return percentWidths;
    }

    public void setPercentWidth(int column, double percentWidth) {
        percentWidths[column].set(percentWidth);
    }

    public void setPercentWidths(double width) {
        for (int i = 0; i < columnCount; i++) setPercentWidth(i, width);
    }

    public void addRow(TableRow row) {
        for (Node node : row) {
            tableGrid.add(node, rowCount, columnCount);
            configure(row);
        }
        rowCount++;
    }
}