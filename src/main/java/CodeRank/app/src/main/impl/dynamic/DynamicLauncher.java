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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DynamicLauncher {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchDynamic(String[] args) throws DynamicAnalysisException, IOException, GraphBuilderException,
            CannotCompileException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

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
                CtClass processedClass = InformationCollector.processMethod(className);

//                это для запуска на файлах прямо из проекта, дебаг
//                CtClass res = InformationCollector.processMethod("CodeRank.app.src.main.impl.pagerank.PageGraph");

                if (processedClass != null) {
                    processedClass.toClass();
                } else {
                    // ?
                    continue;
                }
                if (name.contains("MazeSolver")) {

                    File file = new File("/home/olesya/HSE_2020-1/java/maze/out/production/maze/");
                    URL url = file.toURI().toURL();
                    URL[] urls = new URL[]{url};
                    ClassLoader currentLoader = new URLClassLoader(urls);
                    Class<?> loadedClass = currentLoader.loadClass(className);
                    Method meth = loadedClass.getDeclaredConstructor()
                            .newInstance()
                            .getClass().getDeclaredMethod("initializeFields", int.class, int.class);
//                    meth.setAccessible(true);
                    meth.invoke(loadedClass.getDeclaredConstructor().newInstance(), 5, 10);
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

        // TIME MEASUREMENT
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.print("FINAL TIME: ");
        System.out.println(System.currentTimeMillis() - time);
        System.out.print("FINAL SPACE: ");
        System.out.println(usedBytes);

    }
}
