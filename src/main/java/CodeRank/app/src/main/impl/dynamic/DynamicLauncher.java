package CodeRank.app.src.main.impl.dynamic;

import CodeRank.app.src.main.impl.graph.Graph;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

import java.io.IOException;

public class DynamicLauncher {

    public static void launch(String[] args) throws NotFoundException, BadBytecode, CannotCompileException, IOException {

//        InformationCollector.doSmth("CodeRank.app.src.main.impl.pagerank.PageGraph",
//                "updatePageRank", PageGraph.class);

        InformationCollector infoCol = new InformationCollector();
        infoCol.collectInformation("CodeRank.app.src.main.impl.graph.Graph",
                "addEdge", Graph.class);
    }
}
