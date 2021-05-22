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
            ClassFile currentClassFile = currentCtClass.getClassFile();
            Stream.of(currentCtClass.getDeclaredMethods())
                    .filter(method -> !(Modifier.isAbstract(method.getModifiers())))
                    .forEach(method -> {
                        int[] nums = {0, 3, 4};
                        modifyMethod(method, nums);
                    });
            currentCtClass.writeFile();
            currentCtClass.detach();

            try {
                collectMethodsInformation(currentClassFile);
            } catch (BadBytecode ex) {
                throw new DynamicAnalysisException(ex.getMessage());
            }
            return currentCtClass;
        } catch (IOException | CannotCompileException | NotFoundException ex) {
            throw new DynamicAnalysisException("Unable to load methods from ClassPool " + ex.getMessage(), ex);
        }
    }

    private static void modifyMethod(CtMethod method, int[] lineNumbers) {
        try {
            for (int currentLineNumber : lineNumbers) {
                method.insertAt(currentLineNumber, "{ " +
//                        "CodeRank.app.src.main.impl.dynamic.ByteCodeInsertion.printt(); " +
                        "System.out.println(\" LINE!\"); }");
                method.insertBefore("{ System.out.println(\" hello!\"); }");
                method.insertAfter("{ System.out.println(\" bye!\"); }");
            }
        } catch (CannotCompileException e) {
            throw new RuntimeException("An error occurred while decorating method " + method.getName(), e);
        }

    }

    private static void collectMethodsInformation(ClassFile currentClassFile) throws BadBytecode {
        for (MethodInfo currentMethodInfo : currentClassFile.getMethods()) {
            System.out.println("\nMETHOD INSTRUCTIONS OF " + currentClassFile.getName() + '.' + currentMethodInfo.getName() + ":\n");
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
                    System.out.println("Line: " + currentMethodInfo.getLineNumber(address));
                    System.out.println(currentClassFile.getName());
                    System.out.println(Mnemonic.OPCODE[opcode] + ' ' + currentInterfaceName + '.' + parameters);
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
