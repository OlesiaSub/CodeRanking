package CodeRank.app.src.main.impl.pagerank;

import CodeRank.app.src.main.impl.graph.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PageGraph<T> {
    private static int index = 0;
    private static final double dampingFactor = 0.15;
    private static double pageSetSize;
    private final HashMap<PageNode, Node<T>> revStorage = new HashMap<>();
    public HashSet<PageNode> nodes = new HashSet<>();

    public PageGraph(HashSet<PageNode> nodes) {
        this.nodes = nodes;
        pageSetSize = nodes.size();
    }

    public PageGraph(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                     HashMap<Node<T>, List<Node<T>>> parents) {
        HashMap<Node<T>, PageNode> storage = new HashMap<>();
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
        Exception e = new Exception();
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("result.txt"), StandardCharsets.UTF_8))) {
            nodes.stream()
                    .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                    .forEach(x -> {
                        try {
                            fileWriter.write(revStorage.get(x).getName() + " ");
                            fileWriter.write(String.valueOf(x.pagerank) + " ");
                            fileWriter.write("\n");
                        } catch (IllegalArgumentException | IOException ex) {
                            e.addSuppressed(ex);
                        }
                    });
        } catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }
}
