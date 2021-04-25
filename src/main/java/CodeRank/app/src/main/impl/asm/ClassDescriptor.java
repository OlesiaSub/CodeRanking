package CodeRank.app.src.main.impl.asm;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import CodeRank.app.src.main.impl.graphbuilder.GraphBuilderException;
import CodeRank.app.src.main.impl.main.Main;
import org.objectweb.asm.*;
import CodeRank.app.src.main.impl.graph.MethodNode;
import CodeRank.app.src.main.impl.graph.Node;

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
            Main.loader.applyGetStorage().add(parent);
        } catch (GraphBuilderException e) {
            e.printStackTrace();
        }
        return ref;
    }

}
