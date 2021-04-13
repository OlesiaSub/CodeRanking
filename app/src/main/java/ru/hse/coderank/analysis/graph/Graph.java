package ru.hse.coderank.analysis.graph;

import java.util.*;

public class Graph<T> {

    public HashSet<Node<T>> storage = new HashSet<>();
    public HashMap<Node<T>, List<Node<T>>> edges = new HashMap<>();
    public HashMap<Node<T>, List<Node<T>>> parents = new HashMap<>();

    public void constructGraph() {
        for (Node<T> entry : storage) {
            edges.put(entry, new LinkedList<>());
            parents.put(entry, new LinkedList<>());
        }
        for (Node<T> entry : storage) {
/*

            DEBUG
            System.out.println("ITERATION: " + entry.getName());
            System.err.flush();
            System.out.flush();
            if (entry.getName().contains("ErrorReportingRunner.runCause")) {
                System.out.println("Here");
            }

 */
            if (!entry.isUsed()) {
                traverseChildren(entry, null);
            }
        }
    }

    private void traverseChildren(Node<T> current, Node<T> parent) {
        for (Node<T> elem : storage) {
            if (current.nodeEquals(elem)) {
                current = elem;
                if (current.getName().contains("isPublic")) {
                    System.out.println("HERE " + current.getName());
                }
                if (parent != null && !current.nodeEquals(parent)) {
                    addEdge(parent, current);
                    addParent(parent, current);
                }
                break;
            }
        }
        if (current.isUsed()) {
            return;
        }
//        storage.add(current);
        current.setUsed();

        for (int i = 0; i < current.getChildren().size(); i++) {
            traverseChildren(current.getChildren().get(i), current);
        }
    }

    private void addEdge(Node<T> parent, Node<T> child) {
        edges.get(parent).add(child);
    }

    private void addParent(Node<T> parent, Node<T> child) {
/*

        DEBUG
        if (parents.get(child) == null) {
            System.out.println("ERR " + child.getName());
            for (Node<T> par : parents.keySet()) {
                if (par.getName().contains("isPublic")) {
                    System.out.println("HERE " + par.getName());
                    System.out.println(parents.get(par));
                }
            }
            for (Node<T> par : storage) {
                if (par.getName().contains("isPublic")) {
                    System.out.println("HERE2 " + par.getName());
                    System.out.println(parents.get(par));
                }
            }
        }

 */
        parents.get(child).add(parent);
    }
}
