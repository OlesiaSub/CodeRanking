package CodeRank.app.src.main.impl.pagerank;

import CodeRank.app.src.main.impl.graph.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class PageGraph<T> {
    private static int index = 0;
    private static final double dampingFactor = 0.15;
    private static double pageSetSize;
    private final HashMap<PageNode, Node<T>> revStorage = new HashMap<>();
    public HashSet<PageNode> nodes = new HashSet<>();

    public PageGraph() {
        // to debug
        this.nodes = null;
        pageSetSize = 0;
    }

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
        // to debug
        System.out.println("MIDDLE");
        double sum = 0;
        for (PageNode node : currentNode.parents) {
            sum += node.pagerank / node.neighbours.size();
        }
        double randomFactor = dampingFactor / pageSetSize;
        currentNode.pagerank = randomFactor + (1 - dampingFactor) * sum;
    }

    public void getDistinctPageRank() {
        Map<Double, List<PageNode>> distinctPageNodes =
                nodes.stream()
                        .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                        .collect(Collectors.groupingBy(x -> x.pagerank));

        List<Double> values =
                nodes.stream()
                        .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                        .map(x -> x.pagerank)
                        .distinct()
                        .collect(Collectors.toList());

        Exception e = new Exception();
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("result.txt"), StandardCharsets.UTF_8))) {
            for (Double rank : values) {
                fileWriter.write("Rank: " + rank + '\n');
                for (PageNode node : distinctPageNodes.get(rank)) {
                    fileWriter.write(revStorage.get(node).getName());
                    fileWriter.write("\n");
                }
                fileWriter.write('\n');
            }
        } catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }

    public void rankClasses() {
        Map<Object, List<PageNode>> classNameMap =
                nodes.stream()
                        .collect(Collectors.groupingBy(x -> {
                            String name = revStorage.get(x).getName();
                            int idx;
                            for (idx = name.length() - 1; idx >= 0; idx--) {
                                if (name.charAt(idx) == '.') {
                                    break;
                                }
                            }
                            return name.subSequence(0, idx);
                        }));

        Map<String, Double> classRanks = new HashMap<>();
        for (Object name : classNameMap.keySet()) {
            double value = 0.0;
            for (PageNode node : classNameMap.get(name)) {
                value += node.pagerank;
            }
            classRanks.put((String) name, value);
        }

        List<String> sortedClassNames = new ArrayList<>(classRanks.keySet());
        sortedClassNames = sortedClassNames
                .stream()
                .sorted(Comparator.comparingDouble(classRanks::get).reversed())
                .collect(Collectors.toList());


        Exception e = new Exception();
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("result.txt"), StandardCharsets.UTF_8))) {
            double currentRank = classRanks.get(sortedClassNames.get(0)) != null ? classRanks.get(sortedClassNames.get(0)) : 0;
            double diff = 0.001;
            boolean flag = true;
            for (String className : sortedClassNames) {
                if (classRanks.get(className) < currentRank - diff || flag) {
                    fileWriter.write("Class rank: " + classRanks.get(className) + '\n');
                    currentRank = classRanks.get(className);
                    flag = false;
                }
                fileWriter.write(className + "\n\n");
            }
        } catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }
}
