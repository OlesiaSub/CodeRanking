package CodeRank.app.src.main.impl.dynamic;

import CodeRank.app.src.main.impl.asm.ClassDescriptor;
import CodeRank.app.src.main.impl.asm.Configuration;
import CodeRank.app.src.main.impl.graph.Graph;
import CodeRank.app.src.main.impl.graph.MethodNode;
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderException;
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderLoader;
import CodeRank.app.src.main.impl.pagerank.PageGraph;
import CodeRank.app.src.main.impl.pagerank.PageNode;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DynamicLauncher {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchDynamic(String[] args) throws DynamicAnalysisException, Exception {

        long time = System.currentTimeMillis();
        String jarPath = args[0];
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        // костыль to launch without plugin installation
        Configuration.setConfigProperty("/home/olesya/HSE_2020-1/CodeRank/src/main/java/CodeRank/app/src/main/resources/analysis.properties");

        new Configuration();
        loader = new GraphBuilderLoader<>(args[1], args[2]);
        loader.createInstance();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class") && Configuration.processPackage(name)) {
                String className = (name.substring(0, name.length() - 6)).replace('/', '.');
                System.out.println(className);
                CtClass res = InformationCollector.processMethod(className);
//                debug
//                CtClass res = InformationCollector.processMethod("CodeRank.app.src.main.impl.pagerank.PageGraph");
                if (res != null) res.toClass();
                else {
                    continue;
                }

                try {
                    Class<?> classDesc = PageGraph.class;
                    Method meth = classDesc.getDeclaredConstructor().newInstance().getClass().getDeclaredMethod("updatePageRank", PageNode.class);
                    meth.setAccessible(true);
                    PageNode node = new PageNode(1);
                    meth.invoke(classDesc.getDeclaredConstructor().newInstance(), node);
                } catch (Exception ex) {
                    throw new DynamicAnalysisException("беда");
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
