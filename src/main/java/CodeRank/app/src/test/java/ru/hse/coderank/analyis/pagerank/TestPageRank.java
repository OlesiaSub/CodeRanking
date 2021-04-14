package CodeRank.app.src.test.java.ru.hse.coderank.analyis.pagerank;

import static org.gradle.internal.impldep.org.junit.Assert.assertEquals;
import static org.gradle.internal.impldep.org.junit.Assert.assertTrue;
//import static org.junit.Assert.*;

import org.testng.annotations.Test;
import CodeRank.app.src.main.java.ru.hse.coderank.analysis.pagerank.PageGraph;
import CodeRank.app.src.main.java.ru.hse.coderank.analysis.pagerank.PageNode;

import java.util.*;

public class TestPageRank {

    private PageGraph<PageNode> testGraph;

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
        testGraph.launchPageRank(50);
        double eps = 1e-5;
        double expected = 0.2;
        for (PageNode node : testGraph.nodes) {
            assertTrue(Math.abs(node.pagerank - expected) < eps);
        }
    }

    private void initializeBasicFirst() {
        HashSet<PageNode> initNodes = new HashSet<>();
        int curIndex = 0;
        PageNode node1 = new PageNode(curIndex++);
        PageNode node2 = new PageNode(curIndex++);
        PageNode node3 = new PageNode(curIndex++);
        PageNode node4 = new PageNode(curIndex++);
        PageNode node5 = new PageNode(curIndex++);
        PageNode node6 = new PageNode(curIndex++);
        PageNode node7 = new PageNode(curIndex);

        node1.neighbours.add(node2);
        node1.neighbours.add(node3);
        node1.neighbours.add(node4);
        node1.neighbours.add(node5);
        node1.neighbours.add(node7);
        node2.neighbours.add(node1);
        node3.neighbours.add(node1);
        node3.neighbours.add(node2);
        node4.neighbours.add(node2);
        node4.neighbours.add(node3);
        node4.neighbours.add(node5);
        node5.neighbours.add(node1);
        node5.neighbours.add(node3);
        node5.neighbours.add(node6);
        node5.neighbours.add(node4);
        node6.neighbours.add(node1);
        node6.neighbours.add(node5);
        node7.neighbours.add(node5);

        node1.parents.add(node2);
        node1.parents.add(node3);
        node1.parents.add(node5);
        node1.parents.add(node6);
        node2.parents.add(node1);
        node2.parents.add(node3);
        node2.parents.add(node4);
        node3.parents.add(node1);
        node3.parents.add(node4);
        node3.parents.add(node5);
        node4.parents.add(node1);
        node4.parents.add(node5);
        node5.parents.add(node1);
        node5.parents.add(node4);
        node5.parents.add(node6);
        node5.parents.add(node7);
        node6.parents.add(node5);
        node7.parents.add(node1);

        initNodes.add(node1);
        initNodes.add(node2);
        initNodes.add(node3);
        initNodes.add(node4);
        initNodes.add(node5);
        initNodes.add(node6);
        initNodes.add(node7);

        testGraph = new PageGraph<>(initNodes);
    }

    @Test
    public void testPageRankBasicFirst() {
        initializeBasicFirst();
        testGraph.launchPageRank(50);
        List<Integer> expected = Arrays.asList(0, 4, 1, 2, 3, 6, 5);
        List<Integer> actual = new ArrayList<>();
        testGraph.nodes.stream()
                .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                .forEach(x -> actual.add(x.index));
        assertEquals(expected, actual);
    }

    private void initializeBasicSecond() {
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
        node4.neighbours.add(node1);
        node4.neighbours.add(node5);
        node5.neighbours.add(node1);

        node1.parents.add(node4);
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
    public void testPageRankBasicSecond() {
        initializeBasicSecond();
        testGraph.launchPageRank(50);
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4);
        List<Integer> actual = new ArrayList<>();
        testGraph.nodes.stream()
                .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                .forEach(x -> actual.add(x.index));
        assertEquals(expected, actual);
    }

    private void initializeBasicThird() {
        HashSet<PageNode> initNodes = new HashSet<>();
        int curIndex = 0;
        PageNode node1 = new PageNode(curIndex++);
        PageNode node2 = new PageNode(curIndex++);
        PageNode node3 = new PageNode(curIndex++);
        PageNode node4 = new PageNode(curIndex);

        node1.neighbours.add(node2);
        node2.neighbours.add(node1);
        node2.neighbours.add(node3);
        node3.neighbours.add(node2);
        node3.neighbours.add(node4);
        node4.neighbours.add(node1);
        node4.neighbours.add(node3);

        node1.parents.add(node2);
        node1.parents.add(node4);
        node2.parents.add(node1);
        node2.parents.add(node3);
        node3.parents.add(node2);
        node3.parents.add(node4);
        node4.parents.add(node1);

        initNodes.add(node1);
        initNodes.add(node2);
        initNodes.add(node3);
        initNodes.add(node4);
        testGraph = new PageGraph<>(initNodes);
    }

    @Test
    public void testPageRankBasicThird() {
        initializeBasicThird();
        testGraph.launchPageRank(50);
        List<Integer> expectedFirst = Arrays.asList(1, 2, 0, 3);
        List<Integer> expectedSecond = Arrays.asList(1, 0, 2, 3);
        List<Integer> actual = new ArrayList<>();
        testGraph.nodes.stream()
                .sorted(Comparator.comparingDouble((PageNode x) -> x.pagerank).reversed())
                .forEach(x -> actual.add(x.index));
        assertTrue(expectedFirst.equals(actual) || expectedSecond.equals(actual));
    }

}
