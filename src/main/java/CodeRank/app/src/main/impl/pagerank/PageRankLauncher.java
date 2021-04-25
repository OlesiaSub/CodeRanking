package CodeRank.app.src.main.impl.pagerank;

import CodeRank.app.src.main.impl.graph.MethodNode;
import CodeRank.app.src.main.impl.graph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PageRankLauncher<T> {

    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents) {
        PageGraph<MethodNode> pageGraph = new PageGraph(initStorage, edges, parents);
        pageGraph.launchPageRank(50);
        pageGraph.getPageRank();
    }

}
