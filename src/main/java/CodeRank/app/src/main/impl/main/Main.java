package CodeRank.app.src.main.impl.main;

import CodeRank.app.src.main.impl.dynamic.DynamicLauncher;
import CodeRank.app.src.main.impl.staticanalysys.StaticLauncher;

/*

temporary arguments:

"/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar"
"/home/olesya/Downloads/junit-4.13.2.jar"
"/home/olesya/HSE_2020-1/JARsmth/scala-library-2.12.13.jar"

"/home/olesya/HSE_2020-1/java/maze/out/artifacts/maze_jar/maze.jar" "/home/olesya/HSE_2020-1/CodeRank/src/main/java/CodeRank/app/src/main/impl/javaLoader.jar" "CodeRank.app.src.main.impl.graph.Graph"

 */

public class Main {

    public static void main(String[] args) throws Exception {
        DynamicLauncher.launch(args);
//        StaticLauncher.launch(args);
    }
}

