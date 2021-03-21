package ru.hse.coderank.analysis.graph;

import ru.hse.coderank.analysis.asm.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph<T> {
    public HashMap<Node<T>, Boolean> storage = new HashMap<>();
    public HashMap<Node<T>, ArrayList<Node<T>>> edges = new HashMap<>();

    public void constructGraph() {
        for (Map.Entry<Node<T>, Boolean> entry : storage.entrySet()) {
            edges.put(entry.getKey(), new ArrayList<>());
        }
        for (Map.Entry<Node<T>, Boolean> entry : storage.entrySet()) {
            if (!entry.getValue()) {
                traverseChildren(entry.getKey(), null);
            }
        }
    }

    private void traverseChildren(Node<T> current, Node<T> parent) {
        for (Node<T> elem : storage.keySet()) {
            if (current.nodeEquals(elem)) {
                current = elem;
                if (parent != null) {
                    addEdge(parent, current);
                }
                break;
            }
        }
        if (storage.get(current)) {
            return;
        }
        storage.put(current, true);
        for (int i = 0; i < current.getChildren().size(); i++) {
            traverseChildren(current.getChildren().get(i), current);
        }
    }

    private void addEdge(Node<T> parent, Node<T> child) {
        edges.get(parent).add(child);
    }
}
