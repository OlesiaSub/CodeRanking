package ru.hse.coderank.analysis.asm;

import java.util.ArrayList;

public class MethodNode extends Node {

    public MethodNode(String name, String desc) {
        this.name = name;
        this.desc = desc;
        this.children = null;
        this.used = false;
        this.children = new ArrayList<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<MethodNode> getChildren() {
        return children;
    }
}
