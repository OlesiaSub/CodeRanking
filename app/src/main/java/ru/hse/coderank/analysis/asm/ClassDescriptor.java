package ru.hse.coderank.analysis.asm;

import java.io.InputStream;

import org.objectweb.asm.*;
import ru.hse.coderank.analysis.graph.MethodNode;
import ru.hse.coderank.analysis.graph.Node;

public class ClassDescriptor extends ClassVisitor {

    public InputStream className;
    public String actualClassName;

    public ClassDescriptor(InputStream stream) {
        super(Opcodes.ASM7);
        className = stream;
    }

    @Override
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
//        System.out.println(name + " extends " + superName);
//        for (String curInterface : interfaces) {
//            System.out.println(name + " implements " + curInterface);
//        }
        actualClassName = name;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
//        System.out.println(" " + desc + " " + name);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        String actualName = (actualClassName + '.' + name).replace('/', '.');
//        System.out.println("ACTUAL: " + actualName + desc);
        Node<MethodNode> parent = MethodNode.createNode();
        parent.payload = new MethodNode(actualName, desc);
        ReferencedMethodsVisitor ref = new ReferencedMethodsVisitor(parent);
        Main.graph.storage.add(parent);
        return ref;
    }

    @Override
    public void visitEnd() {
//        System.out.println("end");
    }

}
