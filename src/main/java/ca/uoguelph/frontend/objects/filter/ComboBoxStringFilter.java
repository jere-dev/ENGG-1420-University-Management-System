package ca.uoguelph.frontend.objects.filter;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

import java.util.function.Function;

/**
 * A specialized {@link ComboBoxFilter} implementation for filtering {@link String} items in a {@link ComboBox}.
 * Provides case-insensitive filtering by default when constructed with an {@link ObservableList}.
 *
 * <p>This class simplifies string filtering by:
 * <ul>
 *   <li>Providing a default case-insensitive search term function</li>
 *   <li>Specializing the generic ComboBoxFilter for String types</li>
 *   <li>Maintaining all functionality from the parent class</li>
 * </ul>
 *
 * <p>Typical usage:
 * <pre>
 * {@code
 * ComboBox<String> comboBox = new ComboBox<>();
 * ObservableList<String> items = FXCollections.observableArrayList("Apple", "Banana", "Cherry");
 * ComboBoxStringFilter filter = new ComboBoxStringFilter(comboBox, items);
 * }
 * </pre>
 *
 * @see ComboBoxFilter
 * @author 180Sai
 */
public class ComboBoxStringFilter extends ComboBoxFilter<String> {
    /**
     * Constructs a {@code ComboBoxStringFilter} with the specified combo box, filtered list,
     * and custom search term function.
     *
     * @param box the combo box to filter
     * @param items the pre-filtered list of string items
     * @param searchTerm function that transforms items for comparison (e.g., case normalization)
     * @throws NullPointerException if any parameter is null
     */
    public ComboBoxStringFilter(ComboBox<String> box, FilteredList<String> items, Function<String, String> searchTerm) {
        super(box, items, searchTerm);
    }

    /**
     * Constructs a {@code ComboBoxStringFilter} with the specified combo box and observable list,
     * using direct string comparison (case-sensitive) by default.
     *
     * <p>This constructor is equivalent to:
     * <pre>
     * new ComboBoxStringFilter(box, items, s -> s)
     * </pre>
     *
     * @param box the combo box to apply filtering to (must not be null)
     * @param items the observable list of strings to filter (must not be null)
     */
    public ComboBoxStringFilter(ComboBox<String> box, ObservableList<String> items) {
        super(box, items, s -> s);
    }
}