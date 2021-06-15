package coderank.impl.dynamicanalysis;

import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;
import coderank.impl.pagerank.PageRankLauncher;

import java.io.*;
import java.util.HashMap;

public class ResultsHandler {

    public HashMap<InvocationData, Integer> storage = new HashMap<>();
    private static final String separator = java.io.File.separator;
    private static final String storageFileName = "storageContainer.txt";

    @SuppressWarnings("unchecked")
    public void getData() throws DynamicAnalysisException {
        String currentDir = System.getProperty("user.dir") + separator + storageFileName;
        FileInputStream fileInput;
        ObjectInputStream objectInput;
        try {
            fileInput = new FileInputStream(currentDir);
            objectInput = new ObjectInputStream(fileInput);
            storage = (HashMap<InvocationData, Integer>) objectInput.readObject();
            objectInput.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new DynamicAnalysisException("Unable to restore data.");
        }
    }

    public void createStorage() {
        Graph<MethodNode> graph = new Graph<>();
        for (InvocationData data : storage.keySet()) {
            Node<MethodNode> parent = MethodNode.createNode();
            parent.payload = new MethodNode(data.source, "parent");
            Node<MethodNode> child = MethodNode.createNode();
            child.payload = new MethodNode(data.target, "child");
            boolean flag = false;
            for (Node<MethodNode> node : graph.getGraphStorage()) {
                if (node.nodeEquals(parent)) {
                    parent = node;
                }
                if (node.nodeEquals(child)) {
                    flag = true;
                }
            }
            for (int i = storage.get(data); i >= 0; i--) {
                parent.getChildren().add(child);
            }
            if (!flag) graph.getGraphStorage().add(child);
            graph.getGraphStorage().add(parent);
        }
        graph.constructGraph();
        PageRankLauncher launcher = new PageRankLauncher<>();
        launcher.launch(graph.getGraphStorage(), graph.getGraphEdges(), graph.getGraphParents(), "dynamic");
    }
}
