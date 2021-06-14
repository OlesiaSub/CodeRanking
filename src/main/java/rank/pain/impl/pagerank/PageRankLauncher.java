package rank.pain.impl.pagerank;

import rank.pain.impl.graph.MethodNode;
import rank.pain.impl.graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PageRankLauncher<T> {

    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode) {
        PageGraph<MethodNode> pageGraph = new PageGraph(initStorage, edges, parents);
        pageGraph.launchPageRank(30);
        if (mode.equals("static_classes")) {
            pageGraph.getDistinctPageRank();
            pageGraph.rankClasses();
        } else if (mode.equals("dynamic")) {
            pageGraph.getDistinctPageRank();
        }
    }

}
