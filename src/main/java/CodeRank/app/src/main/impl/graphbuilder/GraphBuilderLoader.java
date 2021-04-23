package CodeRank.app.src.main.impl.graphbuilder;

import CodeRank.app.src.main.impl.graph.Node;
import CodeRank.app.src.main.impl.pagerank.PageRankLauncher;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GraphBuilderLoader<T> {
    private HashSet<Node<T>> builderStorage;
    private HashMap<Node<T>, List<Node<T>>> builderEdges;
    private HashMap<Node<T>, List<Node<T>>> builderParents;
    private final String graphBuilderLocation;
    private final String graphBuilderName;
    private Class<?> customGraphBuilder;

    public GraphBuilderLoader(String graphBuilderLocation, String graphBuilderName) {
        this.graphBuilderLocation = graphBuilderLocation;
        this.graphBuilderName = graphBuilderName;
    }

    public void loadGraphBuilder() throws GraphBuilderException {
        try {
            File inputDirectory = new File(graphBuilderLocation);
            ClassLoader classLoader = new URLClassLoader(new URL[]{inputDirectory.toURI().toURL()});
            customGraphBuilder = classLoader.loadClass(graphBuilderName);
            Method constructGraph = customGraphBuilder.getMethod("constructGraph");
            constructGraph.invoke(customGraphBuilder);
        } catch (Exception e) {
            // TODO: fix exceptions
            throw new GraphBuilderException("Problem :)");
        }
    }

    @SuppressWarnings("unchecked")
    public void applyParameters() throws GraphBuilderException {
        try {
            Method getStorage = customGraphBuilder.getMethod("getStorage");
            Object objectStorage = getStorage.invoke(customGraphBuilder);
            builderStorage = (HashSet<Node<T>>) objectStorage;

            Method getGraphEdges = customGraphBuilder.getMethod("getGraphEdges");
            Object objectEdges = getGraphEdges.invoke(customGraphBuilder);
            builderEdges = (HashMap<Node<T>, List<Node<T>>>) objectEdges;

            Method getGraphParents = customGraphBuilder.getMethod("getGraphParents");
            Object objectParents = getGraphParents.invoke(customGraphBuilder);
            builderParents = (HashMap<Node<T>, List<Node<T>>>) objectParents;
        } catch (Exception e) {
            throw new GraphBuilderException("Problem 2");
        }
    }
}
