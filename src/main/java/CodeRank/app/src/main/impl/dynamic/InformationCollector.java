package CodeRank.app.src.main.impl.dynamic;

import CodeRank.app.src.main.impl.asm.Configuration;
import javassist.*;
import javassist.bytecode.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class InformationCollector {

    private InformationCollector() {
    }

    public static CtClass processClass(String className, String classFilesLocation) throws DynamicAnalysisException {
        try {
            ClassPool currentClassPool = ClassPool.getDefault();
            File file = new File(classFilesLocation);
            URL url = file.toURI().toURL();
            URL[] urls = new URL[]{url};
            ClassLoader currentLoader = new URLClassLoader(urls);
            Class<?> loadedClass = currentLoader.loadClass(className);
            currentClassPool.insertClassPath(new ClassClassPath(loadedClass));

            CtClass currentCtClass = currentClassPool.getOrNull(className);
            if (currentCtClass == null) {
                return null;
            }
            try {
                ClassFile currentClassFile = currentCtClass.getClassFile();
//                modifyClass(currentCtClass);
                collectMethodsInformation(currentClassFile, currentCtClass);
                currentCtClass.writeFile();
                currentCtClass.detach();
            } catch (BadBytecode ex) {
                throw new DynamicAnalysisException(ex.getMessage());
            }
            return currentCtClass;
        } catch (IOException | CannotCompileException | NotFoundException | ClassNotFoundException ex) {
            throw new DynamicAnalysisException("Unable to load methods from ClassPool " + ex.getMessage(), ex);
        }
    }

    // does not work(((
    private static void modifyClass(CtClass currentCtClass) throws NotFoundException, CannotCompileException {
//        CtClass clazz = ClassPool.getDefault().get("java.lang.Class");
//        CtField f = new CtField(clazz, "bytecodeInsertionClass", currentCtClass);
        CtField f1 = CtField.make("Class bb = BytecodeInsertion.class;", currentCtClass);
        f1.setModifiers(Modifier.PRIVATE);
        f1.setModifiers(Modifier.STATIC);
        f1.setModifiers(Modifier.FINAL);
        System.out.println("here " + currentCtClass.getName());
        currentCtClass.addField(f1);
    }

    private static void modifyMethod(CtMethod method, int lineNumber, String from, String to, String invocationType) {
        try {
            String insertion = "{" +
                    "CodeRank.app.src.main.impl.dynamic.BytecodeInsertion.toInsert(" +
                    "\"" + from + "\"" + "," +
                    "\"" + to + "\"" + "," +
                    "\"" + invocationType + "\"" +
                    ");" +
                    "}";
            method.insertAt(lineNumber, insertion);
        } catch (CannotCompileException ex) {
            throw new RuntimeException("An error occurred while decorating method " + method.getName(), ex);
        }

    }

    private static void collectMethodsInformation(ClassFile currentClassFile, CtClass currentCtClass) throws BadBytecode, NotFoundException {
        for (MethodInfo currentMethodInfo : currentClassFile.getMethods()) {
//            System.out.println("\nDEBUG; " + "METHOD INSTRUCTIONS OF " + currentClassFile.getName() + '.' + currentMethodInfo.getName() + ":\n");
            if ((currentMethodInfo.getAccessFlags() & AccessFlag.ABSTRACT) != 0) {
                continue;
            }
            CodeAttribute currentCodeAttribute = currentMethodInfo.getCodeAttribute();
            for (CodeIterator currentCodeIterator = currentCodeAttribute.iterator(); currentCodeIterator.hasNext(); ) {
                int address = currentCodeIterator.next();
                int opcode = currentCodeIterator.byteAt(address);
                if (opcode == Opcode.INVOKEINTERFACE || opcode == Opcode.INVOKEVIRTUAL || opcode == Opcode.INVOKESPECIAL) {
                    int index = currentCodeIterator.s16bitAt(address + 1);
                    String currentInterfaceName = currentClassFile.getConstPool().getInterfaceMethodrefClassName(index);
                    if (!Configuration.processPackage(currentInterfaceName)) {
                        continue;
                    }
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
