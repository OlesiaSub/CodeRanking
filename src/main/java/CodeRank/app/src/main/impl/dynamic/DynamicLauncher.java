package CodeRank.app.src.main.impl.dynamic;

import CodeRank.app.src.main.impl.pagerank.PageGraph;
import CodeRank.app.src.main.impl.pagerank.PageNode;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicLauncher {

    public static void launch(String[] args) throws CannotCompileException, DynamicAnalysisException {
        String className = "CodeRank.app.src.main.impl.pagerank.PageGraph";
        String methodName = "updatePageRank";
        InformationCollector.processMethod(className, methodName).toClass();

        try {
            Class<?> classDesc = PageGraph.class;
            Method meth = classDesc.getDeclaredConstructor().newInstance().getClass().getDeclaredMethod(methodName, PageNode.class);
            meth.setAccessible(true);
            PageNode node = new PageNode(1);
            meth.invoke(classDesc.getDeclaredConstructor().newInstance(), node);
        } catch (Exception ex) {
            throw new DynamicAnalysisException("беда");
        }

    }
}
