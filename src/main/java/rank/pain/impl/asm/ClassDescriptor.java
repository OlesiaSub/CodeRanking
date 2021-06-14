package rank.pain.impl.asm;

import rank.pain.impl.graph.MethodNode;
import rank.pain.impl.graph.Node;
import rank.pain.impl.graphbuilder.GraphBuilderException;
import rank.pain.impl.staticanalysys.StaticLauncher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.InputStream;

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
        actualClassName = name;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        String actualName = (actualClassName + '.' + name).replace('/', '.');
        Node<MethodNode> parent = MethodNode.createNode();
        parent.payload = new MethodNode(actualName, desc);
        ReferencedMethodsVisitor ref = new ReferencedMethodsVisitor(parent);
        try {
            StaticLauncher.loader.applyGetStorage().add(parent);
        } catch (GraphBuilderException e) {
            e.printStackTrace();
        }
        return ref;
    }

}
