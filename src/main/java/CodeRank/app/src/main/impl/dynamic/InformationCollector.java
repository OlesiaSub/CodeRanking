package CodeRank.app.src.main.impl.dynamic;

import javassist.*;
import javassist.bytecode.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class InformationCollector {

    public static void doSmth(String className, String methodName, Class<?> classDesc) throws NotFoundException, BadBytecode, CannotCompileException, IOException {
        
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new ClassClassPath(classDesc));
        ClassFile cf = pool.get(className).getClassFile();
        MethodInfo minfo = cf.getMethod(methodName);
        CodeAttribute ca = minfo.getCodeAttribute();
        CodeIterator ci = ca.iterator();

        List<String> operations = new LinkedList<>();
        while (ci.hasNext()) {
            int index = ci.next();
            int op = ci.byteAt(index);
            operations.add(Mnemonic.OPCODE[op]);
        }
        System.out.println(operations);
    }

}
