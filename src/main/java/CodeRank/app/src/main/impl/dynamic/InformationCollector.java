package CodeRank.app.src.main.impl.dynamic;

import javassist.*;
import javassist.bytecode.*;

import java.io.IOException;
import java.util.function.Consumer;

public class InformationCollector {

    private InformationCollector() {
    }

    public static CtClass processMethod(String className, String methodName) throws DynamicAnalysisException {
        try {
            ClassPool currentClassPool = ClassPool.getDefault();
//        currentClassPool.appendClassPath(new ClassClassPath(classDescriptor));
            ClassFile currentClassFile = currentClassPool.get(className).getClassFile();
            CtClass currentCtClass = currentClassPool.get(className);
            CtMethod currentMethod = currentCtClass.getDeclaredMethod(methodName);
            Consumer<CtMethod> consumer = new MethodConsumer();
            consumer.accept(currentMethod);
            currentCtClass.writeFile();
            currentCtClass.detach();

            try {
                collectInformation(currentClassFile, methodName);
            } catch (BadBytecode ex) {
                throw new DynamicAnalysisException(ex.getMessage());
            }
            return currentCtClass;
        } catch (IOException | CannotCompileException | NotFoundException ex) {
            throw new DynamicAnalysisException("Unable to load method " + methodName + " from ClassPool.", ex);
        }
    }

    private static final class MethodConsumer implements Consumer<CtMethod> {
        @Override
        public void accept(CtMethod method) {
            try {
                method.insertBefore("{ System.out.println($1 + \" hello!\"); }");
                method.insertAfter("{ System.out.println($1 + \" bye!\"); }");
            } catch (CannotCompileException e) {
                throw new RuntimeException("An error occurred while decorating method " + method.getName(), e);
            }
        }
    }

    private static void collectInformation(ClassFile currentClassFile, String methodName) throws BadBytecode {
        System.out.println("FIELDS: \n");
        for (Object fieldInfoObj : currentClassFile.getFields()) {
            FieldInfo fieldInfo = (FieldInfo) fieldInfoObj;
            System.out.printf("Field %s; %s%n", fieldInfo.getName(), fieldInfo.getDescriptor());
        }

        System.out.println("\n\nMETHOD INSTRUCTIONS: \n");
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
