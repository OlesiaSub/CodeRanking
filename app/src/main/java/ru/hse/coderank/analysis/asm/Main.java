package ru.hse.coderank.analysis.asm;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import ru.hse.coderank.analysis.graph.Graph;

public class Main {

    public static Graph <MethodNode> graph = new Graph<>();

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        graph.constructGraph();

        // Print graph
        System.out.println("FIN");
        for (MethodNode m : graph.storage.keySet()) {
            System.out.println("\nNEW METHOD");
            System.out.println(m.name);
            if (!graph.edges.get(m).isEmpty()) {
                System.out.println("EDGES");

                for (MethodNode me : graph.edges.get(m)) {
                    System.out.println(me.name);
                }
            }
        }
    }
}

