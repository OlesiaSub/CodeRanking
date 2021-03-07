package ru.hse.coderank.analysis.asm;

import java.util.ArrayList;

public interface Node {
    public ArrayList<? extends Node> getChildren();
}
