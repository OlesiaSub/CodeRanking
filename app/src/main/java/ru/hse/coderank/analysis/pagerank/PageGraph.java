package ru.hse.coderank.analysis.pagerank;

import ru.hse.coderank.analysis.graph.Node;

import java.util.*;

public class PageGraph<T> {
    public static int index = 0;
    public static final double dampingFactor = 0.15;
    public static double pageSetSize;
    public HashMap<Node<T>, PageNode> storage = new HashMap<>();
    public HashMap<PageNode, Node<T>> revStorage = new HashMap<>();
    public HashSet<PageNode> nodes = new HashSet<>();

    public PageGraph(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                     HashMap<Node<T>, List<Node<T>>> parents) {
        for (Node<T> node : initStorage) {
            PageNode currentNode = new PageNode(index++);
            storage.put(node, currentNode);
            revStorage.put(currentNode, node);
            nodes.add(currentNode);
        }

        for (Node<T> node : edges.keySet()) {
            PageNode pageNode = storage.get(node);
            for (Node<T> edgeNode : edges.get(node)) {
                PageNode pageEdgeNode = storage.get(edgeNode);
                pageNode.neighbours.add(pageEdgeNode);
            }
        }

        for (Node<T> node : parents.keySet()) {
            PageNode pageNode = storage.get(node);
            for (Node<T> edgeNode : parents.get(node)) {
                PageNode pageEdgeNode = storage.get(edgeNode);
                pageNode.parents.add(pageEdgeNode);
            }
        }

        pageSetSize = nodes.size();
    }

    public void launchPageRank(int iterations) {
        for (int i = 0; i < iterations; i++) {
            pageRankIteration();
        }
    }

    private void pageRankIteration() {
        for (PageNode node : nodes) {
            updatePageRank(node);
        }
    }

    private void updatePageRank(PageNode currentNode) {
        double sum = 0;
        for (PageNode node : currentNode.parents) {
            sum += node.pagerank / node.neighbours.size();
        }
        double randomFactor = dampingFactor / pageSetSize;
        currentNode.pagerank = randomFactor + (1 - dampingFactor) * sum;
    }

    public void getPageRank() {
        ArrayList<PageNode> sortedNodes = new ArrayList<>(nodes);
        Comparator<PageNode> comparator = (o1, o2) -> o1.pagerank > o2.pagerank ? 1 : 0;
        sortedNodes.sort(comparator);
        nodes.stream()
                .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                .forEach(x -> {
                    System.out.println(revStorage.get(x).getName());
                    System.out.println(x.pagerank);
                    System.out.print("\n");
                });
    }

}
