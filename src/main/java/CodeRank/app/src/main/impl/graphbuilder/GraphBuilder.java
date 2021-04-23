package CodeRank.app.src.main.impl.graphbuilder;

import CodeRank.app.src.main.impl.graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface GraphBuilder<T> {

    HashSet<Node<T>> getGraphStorage();

    HashMap<Node<T>, List<Node<T>>> getGraphEdges();

    HashMap<Node<T>, List<Node<T>>> getGraphParents();

    void constructGraph();
}
