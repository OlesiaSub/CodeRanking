package ru.hse.coderank.analysis.main;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import ru.hse.coderank.analysis.asm.ClassDescriptor;
import ru.hse.coderank.analysis.asm.Configuration;
import ru.hse.coderank.analysis.graph.Graph;
import ru.hse.coderank.analysis.graph.MethodNode;
import ru.hse.coderank.analysis.graph.Node;
import ru.hse.coderank.analysis.pagerank.PageGraph;

public class Main {

    public static Graph<MethodNode> graph = new Graph<>();

    public static void main(String[] args) throws IOException {
        String jarPath = args[0];
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        new Configuration();

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

        graph.constructGraph();
//        System.out.println("FIN");
        for (Node<MethodNode> m : graph.storage) {
            System.out.println("\nNEW METHOD");
            System.out.println(m.payload.getName());
            if (!graph.edges.get(m).isEmpty()) {
                System.out.println("EDGES");

                for (Node<MethodNode> me : graph.edges.get(m)) {
                    System.out.println(me.payload.getName());
                }
            }
        }

        System.out.println("\nPAGERANK:");
        PageGraph<MethodNode> pageGraph = new PageGraph<>(graph.storage, graph.edges, graph.parents);
        pageGraph.launchPageRank(100);
        pageGraph.getPageRank();
    }
}

