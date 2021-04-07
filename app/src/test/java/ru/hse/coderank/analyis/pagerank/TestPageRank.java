package ru.hse.coderank.analyis.pagerank;

import static org.junit.Assert.*;
import org.junit.Test;
import ru.hse.coderank.analysis.pagerank.PageGraph;
import ru.hse.coderank.analysis.pagerank.PageNode;

import java.lang.reflect.Array;
import java.util.*;

public class TestPageRank {

    private PageGraph<TestNode> testGraph;

    private void initializeLinearOrder() {
        HashSet<PageNode> initNodes = new HashSet<>();
        int curIndex = 0;
        PageNode node1 = new PageNode(curIndex++);
        PageNode node2 = new PageNode(curIndex++);
        PageNode node3 = new PageNode(curIndex++);
        PageNode node4 = new PageNode(curIndex++);
        PageNode node5 = new PageNode(curIndex++);
        PageNode node6 = new PageNode(curIndex);
        node1.neighbours.add(node2);
        node2.neighbours.add(node3);
        node3.neighbours.add(node4);
        node4.neighbours.add(node5);
        node5.neighbours.add(node6);
        node2.parents.add(node1);
        node3.parents.add(node2);
        node4.parents.add(node3);
        node5.parents.add(node4);
        node6.parents.add(node5);
        initNodes.add(node1);
        initNodes.add(node2);
        initNodes.add(node3);
        initNodes.add(node4);
        initNodes.add(node5);
        initNodes.add(node6);
        testGraph = new PageGraph<>(initNodes);
    }

    @Test
    public void testPageRankLinearOrder() {
        initializeLinearOrder();
        System.out.println("\nPAGERANK:");
        testGraph.launchPageRank(50);
        List<Integer> expected = Arrays.asList(5, 4, 3, 2, 1, 0);
        List<Integer> actual = new ArrayList<>();
        testGraph.nodes.stream()
                .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                .forEach(x -> actual.add(x.index));
        assertEquals(expected, actual);
    }

    private void initializeCycle() {
        HashSet<PageNode> initNodes = new HashSet<>();
        int curIndex = 0;
        PageNode node1 = new PageNode(curIndex++);
        PageNode node2 = new PageNode(curIndex++);
        PageNode node3 = new PageNode(curIndex++);
        PageNode node4 = new PageNode(curIndex++);
        PageNode node5 = new PageNode(curIndex);
        node1.neighbours.add(node2);
        node2.neighbours.add(node3);
        node3.neighbours.add(node4);
        node4.neighbours.add(node5);
        node5.neighbours.add(node1);
        node1.parents.add(node5);
        node2.parents.add(node1);
        node3.parents.add(node2);
        node4.parents.add(node3);
        node5.parents.add(node4);
        initNodes.add(node1);
        initNodes.add(node2);
        initNodes.add(node3);
        initNodes.add(node4);
        initNodes.add(node5);
        testGraph = new PageGraph<>(initNodes);
    }

    @Test
    public void testPageRankCycle() {
        initializeCycle();
        System.out.println("\nPAGERANK:");
        testGraph.launchPageRank(50);
        testGraph.getPageRank();
    }



}
