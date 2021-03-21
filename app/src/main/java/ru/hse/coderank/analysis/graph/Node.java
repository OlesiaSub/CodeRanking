package ru.hse.coderank.analysis.graph;

import java.util.List;

public abstract class Node<T> {

    public T payload;

    public abstract List<Node<T>> getChildren();

    public abstract boolean nodeEquals(Node<T> other);

    public abstract boolean isUsed();

    public abstract void setUsed();

    public abstract String getName();
}
