package ru.hse.coderank.analysis.asm;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public abstract class Node {
    public String name;
    public String desc;
    public ArrayList<MethodNode> children;
    public boolean used;
    public abstract <T extends Node> ArrayList<T> getChildren();

    public <T extends Node> boolean nodeEquals(@NonNull T current, @NonNull T other) {
        return current.name.equals(other.name) && current.desc.equals(other.desc);
    }
}
