package ru.hse.coderank.analysis.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ReferencedMethodsVisitor extends MethodVisitor {

    public MethodNode parent;

    public ReferencedMethodsVisitor() {
        super(Opcodes.ASM7);
    }

    public ReferencedMethodsVisitor(MethodNode parent) {
        super(Opcodes.ASM7);
        this.parent = parent;
    }

    void getReferenceInfo(String owner, String name, String desc) {
        if (Configuration.processPackage(owner)) {
            String actualName = Type.getObjectType(owner).getClassName() + "." + name;
            System.out.println("REF: " + actualName + " " + desc);
            parent.children.add(new MethodNode(actualName, desc));
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        getReferenceInfo(owner, name, desc);
    }

    @Override
    public void visitEnd() {

    }

}
