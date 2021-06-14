package rank.pain.impl.launcher;

import rank.pain.impl.asm.Configuration;
import rank.pain.impl.dynamic.DynamicLauncher;
import rank.pain.impl.staticanalysys.StaticLauncher;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Launcher {

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
        Configuration.setConfigProperty("/home/olesya/HSE_2020-1/newestCodeRank/CodeRanking/src/main/java/rank/pain/resources/analysis.properties");
        new Configuration();

        String mode = args[4];
        if (mode.equals("static")) {
            StaticLauncher.launchStatic(args, entries, jarFile);
        } else  if (mode.equals("instrument_dynamic")) {
            DynamicLauncher.launchDynamic(args, entries);
        } else  if (mode.equals("analyse_dynamic")) {
            DynamicLauncher.analyseDynamic(args, entries);
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

