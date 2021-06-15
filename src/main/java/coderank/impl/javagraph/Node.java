package coderank.impl.javagraph;

import java.util.List;

public abstract class Node<T> {

    public T payload;

    public abstract List<Node<T>> getChildren();

    public abstract boolean nodeEquals(Node<T> other);

    public abstract boolean isUsed();

    public abstract void setUsed();

    public abstract String getName();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
