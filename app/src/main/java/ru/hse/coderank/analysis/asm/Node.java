package ru.hse.coderank.analysis.asm;

import java.util.List;

public abstract class Node<T> {

//    public <T extends Node> boolean nodeEquals(@NonNull T current, @NonNull T other) {
//        return current.name.equals(other.name) && current.desc.equals(other.desc);
//    }

    public T payload;

    public abstract List<Node<T>> getChildren();

    public abstract boolean nodeEquals(Node<T> other);
}
