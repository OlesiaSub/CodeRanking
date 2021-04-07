package ru.hse.coderank.analysis.pagerank;

import java.util.LinkedList;
import java.util.List;

public class PageNode {

    public int index;
    public double pagerank = 1;
    public List<PageNode> parents = new LinkedList<>();
    public List<PageNode> neighbours = new LinkedList<>();

    public PageNode(int index) {
        this.index = index;
    }

    public PageNode(int index, List<PageNode> parents, List<PageNode> neighbours) {
        this.index = index;
        this.parents = parents;
        this.neighbours = neighbours;
    }

}
