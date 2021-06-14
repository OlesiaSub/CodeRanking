package rank.pain.impl.dynamic;

import rank.pain.impl.asm.Configuration;
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
            ClassFile currentClassFile = currentCtClass.getClassFile();
//                modifyClass(currentCtClass);
            collectMethodsInformation(currentClassFile, currentCtClass);
            currentCtClass.writeFile();
//                currentCtClass.detach();
            return currentCtClass;
        } catch (IOException | CannotCompileException | NotFoundException | ClassNotFoundException ex) {
            throw new DynamicAnalysisException("Unable to load methods from ClassPool " + ex.getMessage(), ex);
        }
    }

    // does not work(((
    private static void modifyClass(CtClass currentCtClass) throws CannotCompileException {
//        CtClass clazz = ClassPool.getDefault().get("java.lang.Class");
//        CtField f = new CtField(clazz, "bytecodeInsertionClass", currentCtClass);
        CtField f1 = CtField.make("Class bb = BytecodeInsertion.class;", currentCtClass);
        f1.setModifiers(Modifier.PRIVATE);
        f1.setModifiers(Modifier.STATIC);
        f1.setModifiers(Modifier.FINAL);
        currentCtClass.addField(f1);
    }

    private static void modifyMethod(CtMethod method, int lineNumber, String from, String to, String invocationType) throws DynamicAnalysisException {
        try {
            String insertion = "{" +
                    "rank.pain.impl.dynamic.BytecodeInsertion.toInsert(" +
                    "\"" + from + "\"" + "," +
                    "\"" + to + "\"" + "," +
                    "\"" + invocationType + "\"" +
                    ");" +
                    "}";
            method.insertAt(lineNumber, insertion);
        } catch (CannotCompileException ex) {
            throw new DynamicAnalysisException("An error occurred while decorating method " + method.getName(), ex);
        }

    }

    private static void collectMethodsInformation(ClassFile currentClassFile, CtClass currentCtClass) throws DynamicAnalysisException {
        for (MethodInfo currentMethodInfo : currentClassFile.getMethods()) {
            if ((currentMethodInfo.getAccessFlags() & AccessFlag.ABSTRACT) != 0) {
                continue;
            }
            CodeAttribute currentCodeAttribute = currentMethodInfo.getCodeAttribute();
            try {
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
                        modifyMethod(method, currentMethodInfo.getLineNumber(address), currentClassFile.getName(),
                                currentInterfaceName, Mnemonic.OPCODE[opcode]);
                    }
                }
            } catch (BadBytecode | NotFoundException e) {
                throw new DynamicAnalysisException("Error while iteration through code attribute.");
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
