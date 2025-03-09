package ca.uoguelph.frontend.table;


import javafx.scene.Node;

import java.util.*;

public class TableRow implements Iterable<Node> {
    private List<Node> elements = new ArrayList<>();

    // Constructor to add multiple elements
    public TableRow(Node... values) {
        for (Node value : values) {
            elements.add(value);
        }
    }

    public Iterator<Node> iterator() {
        return elements.iterator();
    }
}
