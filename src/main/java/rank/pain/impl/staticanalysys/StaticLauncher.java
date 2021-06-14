package rank.pain.impl.staticanalysys;

import rank.pain.impl.asm.ClassDescriptor;
import rank.pain.impl.asm.Configuration;
import rank.pain.impl.graph.Graph;
import rank.pain.impl.graph.MethodNode;
import rank.pain.impl.graphbuilder.GraphBuilderLoader;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class StaticLauncher {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchStatic(String[] args, Enumeration<JarEntry> entries, JarFile jarFile) throws Exception {

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

        loader.loadGraphBuilder();
        loader.applyParameters();
    }
}
