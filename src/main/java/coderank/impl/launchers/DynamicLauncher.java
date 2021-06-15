package coderank.impl.launchers;

import coderank.impl.staticanalysis.Configuration;
import coderank.impl.dynamicanalysis.DynamicAnalysisException;
import coderank.impl.dynamicanalysis.InformationCollector;
import coderank.impl.dynamicanalysis.ResultsHandler;
import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.graphbuilder.GraphBuilderException;
import coderank.impl.graphbuilder.GraphBuilderLoader;

import javassist.CannotCompileException;
import javassist.CtClass;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;

public class DynamicLauncher {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchDynamic(String[] args, Enumeration<JarEntry> entries) throws DynamicAnalysisException, GraphBuilderException {

        loader = new GraphBuilderLoader<>(args[1], args[2]);
        loader.createInstance();
        ArrayList<String> names = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class") && Configuration.processPackage(name)) {
                names.add(name);
                String className = (name.substring(0, name.length() - ".class".length())).replace('/', '.');
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
    }

    public static void analyseDynamic(String[] args, Enumeration<JarEntry> entries) throws DynamicAnalysisException {
        ResultsHandler handler = new ResultsHandler();
        handler.getData();
        handler.createStorage();
    }
}
