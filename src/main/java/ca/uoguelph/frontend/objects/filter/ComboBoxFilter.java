package ca.uoguelph.frontend.objects.filter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

import java.util.function.Function;

/**
 * A {@link ChangeListener} implementation that provides real-time filtering for items in a {@link ComboBox}
 * as the user types. The filter matches items based on whether they contain the input text (case-insensitive),
 * while maintaining the original list of items.
 *
 * <p>Key features:
 * <ul>
 *   <li>Dynamic filtering as the user types</li>
 *   <li>Case-insensitive matching</li>
 *   <li>Preservation of original item list</li>
 *   <li>Automatic dropdown management</li>
 *   <li>Bypass capability for programmatic changes</li>
 * </ul>
 *
 * <p>When an exact match exists between the input text and an item, the filter is temporarily disabled
 * to show all items. The dropdown visibility and row count are automatically adjusted based on the
 * number of matching items.
 *
 * @param <T> the type of elements in the ComboBox
 * @author  180Sai
 */
public class ComboBoxFilter<T> implements ChangeListener<String> {
    private final ComboBox<T> box;
    private final Function<T, String> searchTerm;
    private final int maxVisibleRowCount;
    private final FilteredList<T> items;

    /**
     * Constructs a {@code ComboBoxFilter} with the specified combo box, filtered list, and search term function.
     *
     * @param box the combo box to filter
     * @param items the pre-filtered list of items (will be wrapped in a FilteredList)
     * @param searchTerm function that extracts the searchable string from each item
     */
    public ComboBoxFilter(ComboBox<T> box, FilteredList<T> items, Function<T, String> searchTerm) {
        box.getEditor().textProperty().removeListener(this);

        this.box = box;
        box.setItems(items);
        maxVisibleRowCount = box.getVisibleRowCount();

        this.items = items;
        this.searchTerm = searchTerm;

        box.getEditor().textProperty().addListener(this);
    }

    /**
     * Constructs a {@code ComboBoxFilter} with the specified combo box, observable list, and search term function.
     * The observable list will be automatically wrapped in a {@link FilteredList}.
     *
     * @param box the combo box to filter
     * @param items the observable list of items to filter
     * @param searchTerm function that extracts the searchable string from each item
     */
    public ComboBoxFilter(ComboBox<T> box, ObservableList<T> items, Function<T, String> searchTerm) {
        this(box, new FilteredList<>(items), searchTerm);
    }

    /**
     * Temporarily bypasses the filter listener to execute the given action without triggering filtering.
     * This is useful for programmatic changes that shouldn't invoke the filter logic.
     *
     * <p>After executing the action, the listener is re-enabled and the combo box state is refreshed.
     *
     * @param r the action to execute while the listener is disabled
     */
    public void bypassListener(Runnable r) {
        box.getEditor().textProperty().removeListener(this);
        r.run();

        // refresh selection and text value
        box.setValue(box.getSelectionModel().getSelectedItem());
        box.getEditor().textProperty().set(box.getEditor().textProperty().get());

        box.getEditor().textProperty().addListener(this);
    }

    /**
     * Handles text changes in the combo box editor by:
     * <ol>
     *   <li>Removing the listener to prevent recursion</li>
     *   <li>Applying the filter if no exact match exists</li>
     *   <li>Restoring the listener</li>
     *   <li>Adjusting dropdown visibility and row count</li>
     * </ol>
     *
     * <p>The filter shows items containing the input text (case-insensitive). If all items are filtered out,
     * the dropdown is hidden. Otherwise, it's shown with an appropriate number of visible rows.
     *
     * @param observable the observed text property
     * @param oldValue the previous text value
     * @param value the new text value
     */
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String value) {
//        System.out.println("change detected: " + oldValue + " | " + value + " | " + box.getSelectionModel().getSelectedItem());
        // remove listeners or otherwise cause stack overflow error
        box.getEditor().textProperty().removeListener(this);

        // If the value of the text exists exactly in the filter then don't filter
        if (box.getItems().stream().noneMatch(item -> searchTerm.apply(item).equals(value))) {
            items.setPredicate(item -> searchTerm.apply(item).toUpperCase().contains(value.toUpperCase()));

            box.getEditor().setText(value);
        }

        // reset observable
        box.getEditor().textProperty().set(box.getEditor().textProperty().get());
        box.getEditor().textProperty().addListener(this);

        // refresh and reset row count if more things appear
        box.setVisibleRowCount(Math.min(box.getItems().size(), maxVisibleRowCount));
        if (box.getVisibleRowCount() < box.getItems().size()) {
            box.hide();
        }

        if (box.isFocused() && !box.isShowing())
            box.show();
    }
}