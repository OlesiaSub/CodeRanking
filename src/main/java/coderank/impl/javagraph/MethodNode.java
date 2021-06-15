package coderank.impl.javagraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MethodNode {

    private final String name;
    private final String desc;
    private boolean used;
    private final List<Node<MethodNode>> children;

    public MethodNode(String name, String desc) {
        this.name = name;
        this.desc = desc;
        used = false;
        children = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    private boolean isUsed() {
        return used;
    }

    private void setUsed() {
        used = true;
    }

    public boolean nodeEquals(MethodNode other) {
        return name.equals(other.name) && desc.equals(other.desc);
    }

    public static Node<MethodNode> createNode() {
        return new Node<MethodNode>() {

            @Override
            public List<Node<MethodNode>> getChildren() {
                return payload.children;
            }

            @Override
            public boolean nodeEquals(Node<MethodNode> other) {
                return payload.nodeEquals(other.payload);
            }

            @Override
            public boolean isUsed() {
                return payload.isUsed();
            }

            @Override
            public void setUsed() {
                payload.setUsed();
            }

            @Override
            public String getName() {
                return payload.name;
            }

            @Override
            public boolean equals(Object o) {
//                System.out.println("in eq");
                if (!(o instanceof MethodNode)) {
                    return false;
                }
                MethodNode other = (MethodNode) o;
                return payload.nodeEquals(other);
            }

            @Override
            public int hashCode() {
//                System.out.println("in hashcode");
                return Objects.hash(payload.name, payload.desc);
            }
        };
    }
}
