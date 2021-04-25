package CodeRank.app.src.main.impl.main;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import CodeRank.app.src.main.impl.asm.Configuration;
import CodeRank.app.src.main.impl.graph.Node;
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderException;
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderLoader;
import CodeRank.app.src.main.impl.pagerank.PageGraph;
import org.objectweb.asm.ClassReader;
import CodeRank.app.src.main.impl.asm.ClassDescriptor;
import CodeRank.app.src.main.impl.graph.Graph;
import CodeRank.app.src.main.impl.graph.MethodNode;

/*

temporary arguments:

"/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar"
"/home/olesya/Downloads/junit-4.13.2.jar"
"/home/olesya/HSE_2020-1/JARsmth/scala-library-2.12.13.jar"

 */

public class Main {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void main(String[] args) throws IOException, GraphBuilderException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        long time = System.currentTimeMillis();
        String jarPath = args[0];
        System.out.println(jarPath);
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        // to launch without plugin installation
        // Configuration.setConfigProperty("/home/olesya/HSE_2020-1/CodeRank/src/main/java/CodeRank/app/src/main/resources/analysis.properties");

        new Configuration();
        loader = new GraphBuilderLoader<>(args[1], args[2]);
        loader.createInstance();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class") && Configuration.processPackage(name)) {
                try (InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024)) {
                    ClassReader re = new ClassReader(stream);
                    ClassDescriptor cv = new ClassDescriptor(stream);
                    re.accept(cv, 0);
                }
            }
        }


        /*

        // GRAPH CONSTRUCTION OUTPUT

        graph.constructGraph();

        for (Node<MethodNode> m : graph.getGraphStorage()) {
            System.out.println("\nNEW METHOD");
            System.out.println(m.payload.getName());
            if (!graph.getGraphEdges().get(m).isEmpty()) {
                System.out.println("EDGES");

                for (Node<MethodNode> me : graph.getGraphEdges().get(m)) {
                    System.out.println(me.payload.getName());
                }
            }
        }

        */

        loader.loadGraphBuilder();
        loader.applyParameters();

        // TIME MEASUREMENT
        
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.print("FINAL TIME: ");
        System.out.println(System.currentTimeMillis() - time);
        System.out.print("FINAL SPACE: ");
        System.out.println(usedBytes);
    }
}

