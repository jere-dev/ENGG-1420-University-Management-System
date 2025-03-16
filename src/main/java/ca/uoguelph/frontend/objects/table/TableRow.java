package ca.uoguelph.frontend.objects.table;


import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;

import java.util.*;
import java.util.function.Predicate;

/**
 * Packages the elements of a row to add to a ScaledTable.
 *
 * @author 180Sai, FruitiousPotato
 */
public class TableRow implements Collection<Node> {
    private final List<Node> elements = new ArrayList<>();
    private final HashMap<Node, TableMemberConfig> nodePropertyMap = new HashMap<>();

    private boolean isNode(Object obj) {
        return Node.class.isAssignableFrom(obj.getClass());
    }

    private boolean isTextInputControl(Node node) {
        return TextInputControl.class.isAssignableFrom(node.getClass());
    }

    // Constructor to add multiple elements
    public TableRow(Object... objects) {
        for (Object obj : objects) if (isNode(obj)) {
            elements.add((Node) obj);
            nodePropertyMap.put((Node) obj, new TableMemberConfig());
        } else {
            Label textWrap = new Label(obj.toString());
            elements.add(textWrap);
            nodePropertyMap.put(textWrap, new TableMemberConfig());
            // TODO: add formatting if required
        }
    }

    // TODO: allow for custom functionality when adding row to table

    /**
     * Returns the {@code TableMemberConfig} object to configure specific layout properties.
     * @param node  the node to retrieve the configurator.
     * @return  the configuration object.
     */
    public TableMemberConfig config(Node node) {
        return nodePropertyMap.get(node);
    }

    // ArrayList useful methods

    public int indexOf(Node o) {
        return elements.indexOf(o);
    }

    public int indexOf(Object o) {
        for (int i = 0; i < size(); i++) {
            Node node = elements.get(i);
            // check if node has text
            if (TextInputControl.class.isAssignableFrom(node.getClass())) {
                if (((TextInputControl) node).getText().equals(o.toString()))
                    return i;
            }
        }
        return -1;
    }

    public Node get(int index) {
        return elements.get(index);
    }

    public Node remove(int index) {
        return elements.remove(index);
    }

    public boolean contains(Node o) {
        return (indexOf(o) >= 0);
    }

    public boolean removeIf(Predicate<? super Node> filter) {
        return elements.removeIf(filter);
    }

    public Node set(int index, Node node) {
        return elements.set(index, node);
    }

    public void addFirst(Node node) {
        elements.addFirst(node);
    }

    public void removeFirst() {
        elements.removeFirst();
    }

    /**
     * Searches the row for all occurrences of the provided text within {@code TextInputControl} objects.
     * <p>
     * If the text is equal to the provided text, then the index of the object is recorded.
     * <p>
     * @param str the object to search for.
     * @return  all indices of occurring objects.
     */
    public int[] searchText(String str) {
        int[] indices = new int[size()];
        Arrays.fill(indices, -1);

        int count = 0;
        for (int i = 0; i < size(); i++) {
            Node node = get(i);
            if (isTextInputControl(node)) {
                if (((TextInputControl) node).getText().equals(str)) {
                    indices[count] = i;
                    count++;
                }
            }
        }
        return indices;
    }

    /**
     * Searches the row for a specific object. If the object is matched then its index is returned.
     * @param obj   the object to search for.
     * @return  the index of the object, or -1 if it is not found.
     */
    public int searchObjects(Object obj) {
        for (int i = 0; i < size(); i++) {
            if (System.identityHashCode(obj) == System.identityHashCode(elements.get(i)))
                return i;
        }
        return -1;
    }

    // Collection implementations uses ArrayList

    @Override
    public Iterator<Node> iterator() {
        return elements.iterator();
    }

    @Override
    public Spliterator<Node> spliterator() {
        return elements.spliterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (isNode(o))
            return contains((Node) o);
        return indexOf(o) >= 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements.toArray(), size());
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean add(Node node) {
        return elements.add(node);
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return new HashSet<>(elements).containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        return elements.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return elements.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return elements.retainAll(c);
    }

    @Override
    public void clear() {
        elements.clear();
    }
}
