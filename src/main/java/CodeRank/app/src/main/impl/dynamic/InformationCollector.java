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
            ClassLoader cl = new URLClassLoader(urls);
            Class<?> cls = cl.loadClass(className);
            currentClassPool.insertClassPath(new ClassClassPath(cls));
//            currentClassPool.appendClassPath(new ClassClassPath(cls));
            CtClass ctc = currentClassPool.getOrNull(className);
            if (ctc == null) {
                return null;
            }
            ClassFile currentClassFile = ctc.getClassFile();
            CtClass currentCtClass = currentClassPool.get(className);
            Stream.of(currentCtClass.getDeclaredMethods())
                    .filter(method -> !(Modifier.isAbstract(method.getModifiers())))
                    .forEach(method -> {
                        Consumer<CtMethod> consumer = new MethodConsumer();
                        consumer.accept(method);
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

    private static final class MethodConsumer implements Consumer<CtMethod> {
        @Override
        public void accept(CtMethod method) {
            try {
                method.insertBefore("{ System.out.println(\" hello!\"); }");
                method.insertAfter("{ System.out.println(\" bye!\"); }");
            } catch (CannotCompileException e) {
                throw new RuntimeException("An error occurred while decorating method " + method.getName(), e);
            }
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
                    System.out.println("Line: " + currentMethodInfo.getLineNumber(address) + "; Address: " + address);
                    System.out.println(Mnemonic.OPCODE[opcode] + ' ' + currentInterfaceName + '.' + parameters + '\n');
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
