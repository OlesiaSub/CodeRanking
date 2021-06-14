package rank.pain.impl.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import rank.pain.impl.graph.MethodNode;
import rank.pain.impl.graph.Node;

public class ReferencedMethodsVisitor extends MethodVisitor {

    public Node<MethodNode> parent;

    public ReferencedMethodsVisitor(Node<MethodNode> parent) {
        super(Opcodes.ASM7);
        this.parent = parent;
    }

    void getReferenceInfo(String owner, String name, String desc) {
        if (Configuration.processPackage(owner)) {
            String actualName = (Type.getObjectType(owner).getClassName() + "." + name).replace('/', '.');
            Node<MethodNode> child = MethodNode.createNode();
            child.payload = new MethodNode(actualName, desc);
            parent.getChildren().add(child);
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        getReferenceInfo(owner, name, desc);
    }

}