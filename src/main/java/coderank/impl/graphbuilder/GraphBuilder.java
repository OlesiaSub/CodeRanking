package coderank.impl.graphbuilder;

import coderank.impl.javagraph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface GraphBuilder<T> {

    HashSet<Node<T>> getGraphStorage();

    HashMap<Node<T>, List<Node<T>>> getGraphEdges();

    HashMap<Node<T>, List<Node<T>>> getGraphParents();

    HashSet<Node<T>> constructGraph();
}
