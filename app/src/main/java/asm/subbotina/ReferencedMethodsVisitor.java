package asm.subbotina;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ReferencedMethodsVisitor extends MethodVisitor {

    public ReferencedMethodsVisitor() {
        super(Opcodes.ASM7);
    }

    public ReferencedMethodsVisitor(ClassDescriptor descriptor) {
        super(Opcodes.ASM7);
    }

    static void getReferenceInfo(String owner, String name, String desc) {
        System.out.println("REF " + Type.getObjectType(owner).getClassName() + "." + name + " " + desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        getReferenceInfo(owner, name, desc);
    }

}
