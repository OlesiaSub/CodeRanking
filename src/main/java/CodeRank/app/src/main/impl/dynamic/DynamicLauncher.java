package CodeRank.app.src.main.impl.dynamic;

import CodeRank.app.src.main.impl.asm.Configuration;
import CodeRank.app.src.main.impl.graph.Graph;
import CodeRank.app.src.main.impl.graph.MethodNode;
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderException;
import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderLoader;
import javassist.CannotCompileException;
import javassist.CtClass;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;

public class DynamicLauncher {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchDynamic(String[] args, Enumeration<JarEntry> entries) throws DynamicAnalysisException,
            IOException, GraphBuilderException {

        loader = new GraphBuilderLoader<>(args[1], args[2]);
        loader.createInstance();

        File file = new File(args[3]);
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        ClassLoader currentLoader = new URLClassLoader(urls);
        ArrayList<String> names = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class") && Configuration.processPackage(name)) {
                names.add(name);
                String className = (name.substring(0, name.length() - ".class".length())).replace('/', '.');
                System.out.println("Processing " + className);
                CtClass processedClass = InformationCollector.processClass(className, args[3]);
                if (processedClass != null) {
                    try {
                        processedClass.toClass();
                    } catch (CannotCompileException e) {
                        throw new DynamicAnalysisException("Unable to perform toClass operation on the class " + name);
                    }
                }
            }
        }

        for (String name : names) {
            String className = (name.substring(0, name.length() - 6)).replace('/', '.');
            if (name.contains("MazeSolver")) {
                try {
                    Class<?> loadedClass = currentLoader.loadClass(className);
                    System.out.println("IN INVOCATION");
                    Method meth = loadedClass.getDeclaredConstructor()
                            .newInstance()
                            .getClass().getDeclaredMethod("traverseMatrix");
                    meth.setAccessible(true);
                    meth.invoke(loadedClass.getDeclaredConstructor().newInstance());
                    break;
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException
                        | InvocationTargetException e) {
                    throw new DynamicAnalysisException("Error during invocation of method " + name);
                }
            }
        }
    }
}
