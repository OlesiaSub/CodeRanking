package CodeRank.app.src.main.impl.main;

import CodeRank.app.src.main.impl.asm.Configuration;
import CodeRank.app.src.main.impl.dynamic.DynamicLauncher;
import CodeRank.app.src.main.impl.staticanalysys.StaticLauncher;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*

temporary arguments:

"/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar"
"/home/olesya/Downloads/junit-4.13.2.jar"
"/home/olesya/HSE_2020-1/JARsmth/scala-library-2.12.13.jar"

"/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar" "/home/olesya/HSE_2020-1/newestCodeRank/src/main/java/CodeRank/app/src/main/impl/javaLoader.jar" "CodeRank.app.src.main.impl.graph.Graph"

 */

public class Main {

    /**
     *
     * @param args
     * args[0] - inputJarFileName
     * args[1] - graphBuilderLocation
     * args[2] - graphBuilderName
     * args[3] - classFilesLocation
     * args[4] - mode
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        String jarPath = args[0];
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        // to launch without plugin installation
        Configuration.setConfigProperty("/home/olesya/HSE_2020-1/newestCodeRank/CodeRanking/src/main/java/CodeRank/app/src/main/resources/analysis.properties");
        new Configuration();

        String mode = args[4];
        if (mode.equals("Static")) {
            StaticLauncher.launchStatic(args, entries, jarFile);
        } else  if (mode.equals("Dynamic")) {
            DynamicLauncher.launchDynamic(args, entries);
        } else {
            throw new RuntimeException("Incorrect mode provided.");
        }

        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.print("FINAL TIME: ");
        System.out.println(System.currentTimeMillis() - time);
        System.out.print("FINAL SPACE: ");
        System.out.println(usedBytes);
    }
}

