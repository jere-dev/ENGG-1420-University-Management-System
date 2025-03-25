package ca.uoguelph.frontend.objects.table;

import ca.uoguelph.frontend.objects.table.row.TableRow;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public final class ScaledTable {
    public final GridPane tableGrid; // TODO: remove
    private final int columnCount;
    private int rowCount = 0;

    private final ColumnConstraints[] constraints;

    private final List<TableRow> rowList = new ArrayList<>();

    public ScaledTable(ScrollPane pane, int columnCount) {
        if (!pane.isFitToWidth()) pane.setFitToWidth(true); // if user has not set option then it is automatically done

        this.columnCount = columnCount;

        tableGrid = new GridPane();
        tableGrid.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        tableGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);

        setHGap(8); setVGap(8);

        // constraints
        for (int i = 0; i < columnCount; i++)
            tableGrid.getColumnConstraints().add(new ColumnConstraints());

        constraints = tableGrid.getColumnConstraints().toArray(new ColumnConstraints[0]);

        pane.setContent(tableGrid);
    }

    public ScaledTable(ScrollPane pane, TableRow[] rows) {
        this(pane, rows[0].size());
        for (TableRow row : rows) {
            addRow(row);
        }
    }

    private void configure(TableRow row) {
        for (Node node : row) if (tableGrid.getChildren().contains(node)) {
            TableMemberConfig config = row.configure(node);
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

    public int length() {
        return rowCount;
    }

    public int width() {
        return columnCount;
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

    public boolean isGridLinesVisible() {
        return tableGrid.isGridLinesVisible();
    }

    public void setGridLinesVisible(boolean isVisible) {
        tableGrid.setGridLinesVisible(isVisible);
    }

    public TableRow getRow(Node node) { return rowList.get(rowOf(node)); }

    public double[] getMinWidths() {
        double[] minWidths = new double[columnCount];
        for (int i = 0; i < minWidths.length; i++) minWidths[i] = getMinWidth(i);
        return minWidths;
    }

    public double getMinWidth(int column) {
        return constraints[column].getMinWidth();
    }

    public void setMinWidth(int column, double width) {
        constraints[column].setMinWidth(width);
    }

    public void setMinWidths(double width) {
        for (int i = 0; i < columnCount; i++) setMinWidth(i, width);
    }

    public double[] getPrefWidths() {
        double[] prefWidths = new double[columnCount];
        for (int i = 0; i < prefWidths.length; i++) prefWidths[i] = getPrefWidth(i);
        return prefWidths;
    }

    public double getPrefWidth(int column) {
        return constraints[column].getPrefWidth();
    }

    public void setPrefWidth(int column, double width) {
        constraints[column].setPrefWidth(width);
    }

    public void setPrefWidths(double width) {
        for (int i = 0; i < columnCount; i++) setPrefWidth(i, width);
    }

    public double[] getMaxWidths() {
        double[] maxWidths = new double[columnCount];
        for (int i = 0; i < maxWidths.length; i++) maxWidths[i] = getMaxWidth(i);
        return maxWidths;
    }

    public double getMaxWidth(int column) {
        return constraints[column].getMaxWidth();
    }

    public void setMaxWidth(int column, double width) {
        constraints[column].setMaxWidth(width);
    }

    public void setMaxWidths(double width) {
        for (int i = 0; i < columnCount; i++) setMaxWidth(i, width);
    }

    public double[] getPercentWidths() {
        double[] percentWidths = new double[columnCount];
        for (int i = 0; i < percentWidths.length; i++) percentWidths[i] = getPercentWidth(i);
        return percentWidths;
    }

    public double getPercentWidth(int column) {
        return constraints[column].getPercentWidth();
    }

    public void setPercentWidth(int column, double percentWidth) {
        constraints[column].setPercentWidth(percentWidth);
    }

    public void setPercentWidths(double width) {
        for (int i = 0; i < columnCount; i++) setPercentWidth(i, width);
    }

    public Priority[] getHGrows() {
        Priority[] hGrows = new Priority[columnCount];
        for (int i = 0; i < columnCount; i++) hGrows[i] = getHGrow(i);
        return hGrows;
    }

    public Priority getHGrow(int column) {
        return constraints[column].getHgrow();
    }

    public void setHGrow(int column, Priority pr) {
        constraints[column].setHgrow(pr);
    }

    public HPos[] getHAlignments() {
        HPos[] hAligns = new HPos[columnCount];
        for (int i = 0; i < columnCount; i++) hAligns[i] = getHAlignment(i);
        return hAligns;
    }

    public HPos getHAlignment(int column) {
        return constraints[column].getHalignment();
    }

    public void setHAlignment(int column, HPos hPos) {
        constraints[column].setHalignment(hPos);
    }

    public void addRow(TableRow row) {
        Node[] nodes = new Node[columnCount];
        row.toArray(nodes);

        tableGrid.addRow(rowCount, row.toArray(nodes));
        configure(row);

        rowList.add(row);

        // background
        if (rowCount % 2 == 0) {
            Pane pane = new Pane();
            tableGrid.add(pane, 0, rowCount);
            GridPane.setColumnSpan(pane, GridPane.REMAINING);
            pane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            pane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            pane.setStyle("-fx-background-color: #dddddd");
            pane.toBack();
        }

        rowCount++;
    }

    public void clear() {
        tableGrid.getChildren().clear();
        rowList.clear();
        rowCount = 0;
    }

    @Override
    public String toString() {
        return "ScaledTable{" +
                "tableGrid=" + tableGrid +
                ", columnCount=" + columnCount +
                ", rowCount=" + rowCount +
                ", \nconstraints=" + Arrays.toString(constraints) +
                ", \nrowList=" + rowList +
                ", \ngridChildren=" + tableGrid.getChildren().toString() +
                '}';
    }
}