package CodeRank.app.src.main.impl.dynamic;

import CodeRank.app.src.main.impl.asm.Configuration;
import javassist.*;
import javassist.bytecode.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class InformationCollector {

    private InformationCollector() {
    }

    public static CtClass processMethod(String className) throws DynamicAnalysisException, ClassNotFoundException {
        try {
            ClassPool currentClassPool = ClassPool.getDefault();
            // костыль, занести в плагин??
            File file = new File("/home/olesya/HSE_2020-1/java/maze/out/production/maze/");
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader currentLoader = new URLClassLoader(urls);
            Class<?> loadedClass = currentLoader.loadClass(className);
            currentClassPool.insertClassPath(new ClassClassPath(loadedClass));
//            currentClassPool.appendClassPath(new ClassClassPath(loadedClass));
            CtClass currentCtClass = currentClassPool.getOrNull(className);
            if (currentCtClass == null) {
                return null;
            }
            try {
                ClassFile currentClassFile = currentCtClass.getClassFile();
                collectMethodsInformation(currentClassFile, currentCtClass);
                currentCtClass.writeFile();
                currentCtClass.detach();
            } catch (BadBytecode ex) {
                throw new DynamicAnalysisException(ex.getMessage());
            }
            return currentCtClass;
        } catch (IOException | CannotCompileException | NotFoundException ex) {
            throw new DynamicAnalysisException("Unable to load methods from ClassPool " + ex.getMessage(), ex);
        }
    }

    private static void modifyMethod(CtMethod method, int lineNumber, String from, String to, String invocationType) {
        try {
            String name = method.getLongName();
            System.out.println("IN METHOD MODIFICATION OF " + name);
            method.insertBefore("{ System.out.println(\"hello! \"); }");
            method.insertAfter("{ System.out.println(\" bye!\"); }");
//            method.insertAt(lineNumber, "{ System.out.println(\"hello middle! \"); }");

            // todo; for all
            if (name.contains("MazeSolver")) {
                String insertion = "{" +
                        "CodeRank.app.src.main.impl.dynamic.BytecodeInsertion.toInsert(" +
                        "\"" + from + "\"" + "," +
                        "\"" + to + "\"" + "," +
                        "\"" + invocationType + "\"" +
                        ");" +
                        "}";
                // debug
//                method.insertAt(lineNumber, "{ System.out.println(\"OUTPUT \" + " +
//                        " \" " + lineNumber + " \" " +
//                        " \" " + from + " \" " +
//                        " \" " + to + " \" " +
//                        " \" " + invocationType + " \" " + "); }");
                method.insertAt(lineNumber, insertion);
            }
        } catch (CannotCompileException e) {
            throw new RuntimeException("An error occurred while decorating method " + method.getName(), e);
        }

    }

    private static void collectMethodsInformation(ClassFile currentClassFile, CtClass currentCtClass) throws BadBytecode, NotFoundException {
        for (MethodInfo currentMethodInfo : currentClassFile.getMethods()) {
            System.out.println("DEBUG; " + "\nMETHOD INSTRUCTIONS OF " + currentClassFile.getName() + '.' + currentMethodInfo.getName() + ":\n");
            if ((currentMethodInfo.getAccessFlags() & AccessFlag.ABSTRACT) != 0) {
                continue;
            }
            CodeAttribute currentCodeAttribute = currentMethodInfo.getCodeAttribute();
            for (CodeIterator currentCodeIterator = currentCodeAttribute.iterator(); currentCodeIterator.hasNext(); ) {
                int address = currentCodeIterator.next();
                int opcode = currentCodeIterator.byteAt(address);

                String parameters = "";
                if (opcode == Opcode.INVOKEINTERFACE || opcode == Opcode.INVOKEVIRTUAL || opcode == Opcode.INVOKESPECIAL) {
                    int index = currentCodeIterator.s16bitAt(address + 1);
                    parameters += currentClassFile.getConstPool().getInterfaceMethodrefName(index);
                    String currentInterfaceName = currentClassFile.getConstPool().getInterfaceMethodrefClassName(index);
                    if (!Configuration.processPackage(currentInterfaceName)) {
                        continue;
                    }
                    System.out.println("DEBUG; Line: " + currentMethodInfo.getLineNumber(address));
                    System.out.println("DEBUG; " + currentClassFile.getName());
                    System.out.println("DEBUG; " + Mnemonic.OPCODE[opcode] + ' ' + currentInterfaceName + '.' + parameters);
                    CtMethod method = currentCtClass.getDeclaredMethod(currentMethodInfo.getName());
                    modifyMethod(method, currentMethodInfo.getLineNumber(address),
                            currentClassFile.getName(), currentInterfaceName, Mnemonic.OPCODE[opcode]);
                }
            }
        }
    }

    private static void collectFieldsInformation(ClassFile currentClassFile) {
        System.out.println("FIELDS: \n");
        for (Object fieldInfoObj : currentClassFile.getFields()) {
            FieldInfo fieldInfo = (FieldInfo) fieldInfoObj;
            System.out.printf("Field %s; %s%n", fieldInfo.getName(), fieldInfo.getDescriptor());
        }
    }

}
