package CodeRank.app.src.main.impl.dynamic;

import javassist.*;
import javassist.bytecode.*;
import javassist.tools.Dump;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class InformationCollector {

    // useless
    public static void doSmth(String className, String methodName, Class<?> classDesc) throws NotFoundException, BadBytecode {
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new ClassClassPath(classDesc));
        ClassFile cf = pool.get(className).getClassFile();
        MethodInfo currentMethodInfo = cf.getMethod(methodName);
        ConstPool ctpool = currentMethodInfo.getConstPool();
        CodeAttribute ca = currentMethodInfo.getCodeAttribute();
        CodeIterator currentCodeIterator = ca.iterator();

        List<String> operations = new LinkedList<>();
        while (currentCodeIterator.hasNext()) {
            int index = currentCodeIterator.next();
            int op = currentCodeIterator.byteAt(index);
            operations.add(Mnemonic.OPCODE[op]);
        }
        System.out.println(operations);
    }

    public void collectInformation(String className, String methodName, Class<?> classDesc) throws BadBytecode, NotFoundException, CannotCompileException, IOException {
        ClassPool currentClassPool = ClassPool.getDefault();
        currentClassPool.appendClassPath(new ClassClassPath(classDesc));
        ClassFile currentClassFile = currentClassPool.get(className).getClassFile();
        CtClass cc = currentClassPool.get(className);
        CtMethod m = cc.getDeclaredMethod(methodName);
        m.insertBefore("{ System.out.println($1); }");
        cc.writeFile();

        System.out.println("FIELDS: \n");
        for (Object fieldInfoObj : currentClassFile.getFields()) {
            FieldInfo fieldInfo = (FieldInfo) fieldInfoObj;
            System.out.printf("Field %s; %s%n", fieldInfo.getName(), fieldInfo.getDescriptor());
        }

        System.out.println("\n\nMETHODS: \n");
        MethodInfo currentMethodInfo = currentClassFile.getMethod(methodName);
        CodeAttribute currentCodeAttribute = currentMethodInfo.getCodeAttribute();
        for (CodeIterator currentCodeIterator = currentCodeAttribute.iterator(); currentCodeIterator.hasNext(); ) {
            int address = currentCodeIterator.next();
            int opcode = currentCodeIterator.byteAt(address);

            String parameters = "";
            if (opcode == Opcode.INVOKEINTERFACE || opcode == Opcode.INVOKEVIRTUAL || opcode == Opcode.INVOKESPECIAL) {
                int index = currentCodeIterator.s16bitAt(address + 1);
                parameters += currentClassFile.getConstPool().getInterfaceMethodrefName(index);
                String currentInterfaceName = currentClassFile.getConstPool().getInterfaceMethodrefClassName(index);
                System.out.println("Line: " + currentMethodInfo.getLineNumber(address) + "; Address: " + address);
                System.out.println(Mnemonic.OPCODE[opcode] + ' ' + currentInterfaceName + '.' + parameters + '\n');
            }
        }
    }

}
