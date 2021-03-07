package ru.hse.coderank.analysis.graph;

import ru.hse.coderank.analysis.asm.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph<T extends Node> {
    public HashMap <T, Boolean> storage = new HashMap<>();
    public HashMap<T, ArrayList<T>> edges = new HashMap<>();

    public void constructGraph() {
        for (Map.Entry<T, Boolean> entry : storage.entrySet()) {
            edges.put(entry.getKey(), new ArrayList<>());
        }
        for (Map.Entry<T, Boolean> entry : storage.entrySet()) {
            if (!entry.getValue()) {
                traverseChildren(entry.getKey());
            }
        }
    }

    private void traverseChildren(T current) {
        if (storage.get(current)) {
            return;
        }
        // TODO : fix generics
        for (int i = 0; i < current.getChildren().size(); i++) {
            addEdge(current, (T) current.getChildren().get(i));
            traverseChildren((T) current.getChildren().get(i));
        }
    }

    private void addEdge(T parent, T child) {
        edges.get(parent).add(child);
    }
}
