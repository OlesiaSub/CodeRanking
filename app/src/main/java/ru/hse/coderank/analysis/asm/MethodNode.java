package ru.hse.coderank.analysis.asm;

import java.util.ArrayList;

public class MethodNode {

    public String name;
    public String desc;
    public ArrayList<MethodNode> children;

    public MethodNode(String name, String desc) {
        this.name = name;
        this.desc = desc;
        this.children = null;
        this.children = new ArrayList<>();
    }

    boolean equals(MethodNode other) {
        return getClass().equals(other.getClass()) && name.equals(other.name) &&
                desc.equals(other.desc) && children.equals(other.children);
    }

}
