package ru.hse.coderank.analysis.asm;

import java.io.InputStream;

import org.objectweb.asm.*;

public class ClassDescriptor extends ClassVisitor {

    public InputStream className;
    public String actualClassName;

    public ClassDescriptor(InputStream stream) {
        super(Opcodes.ASM7);
        className = stream;
    }

    /**
     * Выводит информацию об extended и implemented классах и интерфейсах
     */
    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName);
        for (String curInterface : interfaces) {
            System.out.println(name + " implements " + curInterface);
        }
        actualClassName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    /**
     * Выводит информацию о полях.
     * Можно закомментировать строку с выводом в консоль, чтобы получить только методы.
     */
    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
//        System.out.println(" " + desc + " " + name);
        return null;
    }

    /**
     * Выводит информацию о самом методе, ReferencedMethodsVisitor выводит ссылки на вызываемые внутри методы.
     */
    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        String actualName = (actualClassName + '.' + name).replace('/', '.');
        System.out.println("ACTUAL: " + actualName + desc);
        MethodNode parent = new MethodNode(actualName, desc);
        ReferencedMethodsVisitor ref = new ReferencedMethodsVisitor(parent);
        Main.graph.storage.put(parent, false);
        return ref;
    }

    /**
     * Обозначение конца обработки текущего класса.
     */
    @Override
    public void visitEnd() {
        System.out.println("end");
    }

}
