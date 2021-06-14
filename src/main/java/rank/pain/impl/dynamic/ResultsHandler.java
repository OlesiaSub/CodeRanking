package rank.pain.impl.dynamic;

import rank.pain.impl.graph.Graph;
import rank.pain.impl.graph.MethodNode;
import rank.pain.impl.graph.Node;
import rank.pain.impl.pagerank.PageRankLauncher;

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
            for (InvocationData data : storage.keySet()) {
                System.out.println(data.invocationType + ' ' + data.source + ' ' + data.target);
                System.out.println(storage.get(data));
            }
            objectInput.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new DynamicAnalysisException("Unable to restore commitsInfo.");
        }
    }

    public void createStorage() {
        Graph<MethodNode> graph = new Graph<>();
        for (InvocationData data : storage.keySet()) {
            System.out.println(data.invocationType + ' ' + data.source + ' ' + data.target);
            System.out.println(storage.get(data));
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
