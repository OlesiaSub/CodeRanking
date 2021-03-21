package ru.hse.coderank.analysis.asm;

import java.util.ArrayList;
import java.util.List;

public class MethodNode {

    public String name;
    public String desc;
    public boolean used;
    public List<Node<MethodNode>> children;

    public MethodNode(String name, String desc) {
        this.name = name;
        this.desc = desc;
        used = false;
        children = new ArrayList<>();
    }

    public boolean nodeEquals(MethodNode other) {
        return name.equals(other.name) && desc.equals(other.desc);
    }

    public static Node<MethodNode> createNode() {
        return new Node<>() {

            @Override
            public List<Node<MethodNode>> getChildren() {
                return payload.children;
            }

            @Override
            public boolean nodeEquals(Node<MethodNode> other) {
                return payload.nodeEquals(other.payload);
            }
        };
    }
}
