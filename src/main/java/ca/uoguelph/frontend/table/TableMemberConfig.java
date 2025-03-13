package ca.uoguelph.frontend.table;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Priority;


/**
 * The configuration properties of a JavaFX node to be presented inside a {@code ScaledTable}.
 * <p>
 * <b>These properties are meant to mimic both the control-specific and the layout-specific properties
 * that would be encountered within JavaFX SceneBuilder.</b>
 * <p>
 * They are all set here and read from the table to configure all layout or other properties before being added.
 * Once the member is added to the table, the values can no longer be set, and any attempt to set them will result
 * in an {@code IllegalStateException}.
 * <p>
 * To be used exclusively within the bounds of {@code TableRow} and {@code ScaledTable}.
 * @author FruitiousPotato, 180Sai
 */
public final class TableMemberConfig {
    private boolean isFinal = false;

    // default values
    private Insets margin = new Insets(0);
    private boolean isFillWidth = false;
    private boolean isFillHeight = false;
    private HPos hAlignment = HPos.LEFT;
    private VPos vAlignment = VPos.TOP;
    private Priority hGrow = Priority.SOMETIMES;
    private Priority vGrow = Priority.SOMETIMES;

    private void assertValues() {
        if (isFinal) throw new IllegalStateException("Properties have already been set in table");
    }

    /**
     * Checks if the properties of this {@code TableMemberConfig} object have been finalized.
     * @return whether the configurator is finalized.
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Finalizes the values of this {@code TableMemberConfig} object.
     * <p>
     * Meant to be used by a table importing the member's properties. Any attempt
     * to then set values then result in an {@code IllegalStateException}.
     */
    public void setFinal() {
        isFinal = false;
    }

    /**
     * Sets the filling property of the node.
     * <p>
     * Setting the fill width/height to {@code true} maximizes the width/height size of the
     * control member to the margins within the {@code ScaledTable} grid.
     *
     * @param fillWidth   maximize the width of the member.
     * @param fillHeight  maximize the height of the node.
     */
    public void setFill(Boolean fillWidth, Boolean fillHeight) {
        setFillHeight(fillHeight);
        setFillWidth(fillWidth);
    }

    /**
     * Sets the alignment property of the node.
     * <p>
     * For example, setting {@code hAlignment} to {@code HPos.LEFT}
     * aligns the node to the left side of its column, and setting {@code vAlignment}
     * to {@code VPos.TOP} aligns the node to the top of its row.
     *
     * @param hAlignment    the horizontal alignment of the node.
     * @param vAlignment    the vertical alignment of the node.
     */
    public void setAlignment(HPos hAlignment, VPos vAlignment) {
        setHAlignment(hAlignment);
        setVAlignment(vAlignment);
    }

    /**
     * Sets the horizontal and vertical grow properties of the node.
     * <p>
     * When the table has excess size, the node dictates whether the row or column expands until the node reaches its maximum size.
     * For example: <blockquote><pre>
     *     setGrowth(Priority.ALWAYS, Priority.NEVER);
     * </pre></blockquote>
     *
     * This instructs the column to always prioritize and the row to never prioritize the node's maximum size.
     *
     * @param hGrow horizontal growth priority.
     * @param vGrow vertical growth priority.
     */
    public void setGrowth(Priority hGrow, Priority vGrow) {
        setHGrow(hGrow);
        setVGrow(vGrow);
    }

    /**
     * Returns the {@code Insets} object of the node's margins.
     * @return  the margins of the node in the table.
     */
    public Insets getMargin() {
        return margin;
    }

    /**
     * Sets the margins of the node in the table.
     * @param margin    the margins of the node.
     */
    public void setMargin(Insets margin) {
        assertValues();
        this.margin = margin;
    }

    /**
     * Sets the margins of the node in the table.
     * @param top   margin from the top of the node
     * @param right margin from the right of the node
     * @param bottom    margin from the bottom of the node
     * @param left  margin from the left of the node
     */
    public void setMargin(double top, double right, double bottom, double left) {
        setMargin(new Insets(top, right, bottom, left));
    }

    /**
     * Sets equal margins of the node in the table.
     * @param allInset  margin from all sides
     */
    public void setMargin(double allInset) {
        setMargin(allInset, allInset, allInset, allInset);
    }

    /**
     * Gets the width filling property of the node.
     * @return  whether width fills to table column.
     */
    public Boolean getFillWidth() {
        return isFillWidth;
    }


    /**
     * Sets the fill width property of the node.
     * <p>
     * Setting the fill width to {@code true} maximizes the width size of the
     * node to the margins within the {@code ScaledTable} grid.
     *
     * @param fillWidth   maximize the width of the node.
     */
    public void setFillWidth(Boolean fillWidth) {
        assertValues();
        isFillWidth = fillWidth;
    }

    /**
     * Gets the height filling property of the node.
     * @return  whether height fills to table column.
     */
    public Boolean getFillHeight() {
        return isFillHeight;
    }

    /**
     * Sets the fill height property of the node.
     *
     * @param fillHeight    maximize the width of the node.
     */
    public void setFillHeight(Boolean fillHeight) {
        assertValues();
        isFillHeight = fillHeight;
    }

    /**
     * The horizontal alignment property of the node.
     *
     * @return  the horizontal alignment of the node.
     */
    public HPos getHAlignment() {
        return hAlignment;
    }

    /**
     * Sets the horizontal alignment property of the node.
     *
     * @param hAlignment the horizontal alignment of the node.
     */
    public void setHAlignment(HPos hAlignment) {
        assertValues();
        this.hAlignment = hAlignment;
    }

    /**
     * The vertical alignment property of the node.
     *
     * @return  the vertical alignment of the node.
     */
    public VPos getVAlignment() {
        return vAlignment;
    }

    /**
     * Sets the vertical alignment property of the node.
     *
     * @param vAlignment    the vertical alignment of the node.
     */
    public void setVAlignment(VPos vAlignment) {
        assertValues();
        this.vAlignment = vAlignment;
    }

    /**
     * The horizontal growth property of the node.
     *
     * @return  the horizontal growth property of the node.
     */
    public Priority getHGrow() {
        return hGrow;
    }

    /**
     * Sets the horizontal growth property of the node.
     *
     * @param hGrow the new horizontal growth property of the node.
     */
    public void setHGrow(Priority hGrow) {
        assertValues();
        this.hGrow = hGrow;
    }

    /**
     * The vertical growth property of the node.
     *
     * @return  the vertical growth property of the node.
     */
    public Priority getVGrow() {
        return vGrow;
    }

    /**
     * Sets the vertical growth property of the node.
     *
     * @param vGrow the new growth property of the node.
     */
    public void setVGrow(Priority vGrow) {
        assertValues();
        this.vGrow = vGrow;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": " +
                "[isFinal: " + isFinal() + ", " +
                "margin: " + getMargin().toString() + ", " +
                "fillWidth: " + getFillWidth() + ", " +
                "fillHeight: " + getFillHeight() + ", " +
                "hAlignment: " + getHAlignment().toString() + ", " +
                "vAlignment: " + getVAlignment().toString() + ", " +
                "hGrow: " + getHGrow().toString() + ", " +
                "vGrow: " + getVGrow().toString() + "]";
    }
}
