package ca.uoguelph.frontend.objects.filter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

/**
 * A {@code ChangeListener} implementation that filters items in a {@code ComboBox} based on user input.
 * This class provides real-time filtering of combo box items as the user types, showing only items
 * that start with the entered text (case-insensitive comparison).
 *
 * <p>The filter maintains the original list of items while displaying only matching items in the
 * combo box dropdown. When an item is selected and matches the current input exactly, the filter
 * is temporarily disabled to show all items.
 */
public class ComboBoxFilter implements ChangeListener<String> {
    private final ComboBox<String> box;
    private final FilteredList<String> items;

    /**
     * Constructs an {@code InputFilter} with the specified combo box and filtered list.
     * The combo box will display items from the provided filtered list.
     *
     * @param box the combo box to which this filter will be applied
     * @param items the filtered list containing items to be displayed in the combo box
     */
    public ComboBoxFilter(ComboBox<String> box, FilteredList<String> items) {
        this.box = box;
        this.items = items;
        box.setItems(items);
    }

    /**
     * Constructs an {@code InputFilter} with the specified combo box and observable list.
     * The observable list is wrapped in a {@code FilteredList} object to filter items in the {@code ComboBox}.
     *
     * @param box the combo box to which this filter will be applied
     * @param items the observable list containing items to be filtered
     * @throws NullPointerException if either parameter is null
     */
    public ComboBoxFilter(ComboBox<String> box, ObservableList<String> items) {
        this(box, new FilteredList<>(items));
    }

    /**
     * Invoked when the text in the combo box editor changes and performs the following:
     * <ul>
     *   <li>If an item is selected and matches the current input exactly, perform no filtering.</li>
     *   <li>Otherwise, filter items to show only those starting with the input text (case-insensitive).</li>
     *   <li>Show the dropdown with filtered results.</li>
     * </ul>
     *
     * @param observable the observable string value (combo box editor text)
     * @param oldValue the previous text value
     * @param value the new text value
     */
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String value) {
        // If any item is selected we get the first word of that item.
        String selected = box.getSelectionModel().getSelectedItem() != null
                ? box.getSelectionModel().getSelectedItem() : null;

        // remove listeners or otherwise cause stack overflow error
        box.getEditor().textProperty().removeListener(this);

        // If an item is selected and the value of in the editor is the same
        // as the selected item we don't filter the list.
        if (value.equals(selected)) {
            items.setPredicate(item -> true);
        } else {
            items.setPredicate(item -> {
                if (item.toUpperCase().startsWith(value.toUpperCase())) {
                    return true;
                } else {
                    return false;
                }
            });

            box.getEditor().setText(value);
        }

        box.getEditor().textProperty().addListener(this);

        box.show();
    }
}