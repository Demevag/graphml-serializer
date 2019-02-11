import com.demevag.gmlserializer.Parser;
import com.demevag.gmlserializer.elements.GmlGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest
{
    private TestGraph testGraph = new TestGraph();
    private TestGraphWithEdgesInsideNodes testGraph2 = new TestGraphWithEdgesInsideNodes();

    @BeforeEach
    public void initGraph()
    {
        initTestGraph(testGraph);
        initGraphWithEdesInNodes(testGraph2);
    }

    @Test
    public void shouldParseGraph() throws IllegalAccessException
    {

        Parser parser = new Parser();

        GmlGraph graph = parser.parse(testGraph);

        assertEquals(2, graph.getNodes().size());
        assertEquals(1, graph.getEdges().size());
        assertEquals("testGraph", graph.getId());

    }

    @Test
    public void shoudParseGraphWithEdgesInsideNodes() throws IllegalAccessException
    {
        Parser parser = new Parser();

        GmlGraph graph = parser.parse(testGraph2);

        assertEquals(2, graph.getNodes().size());
        assertEquals(2, graph.getEdges().size());
        assertEquals("testGraph2", graph.getId());
    }

    private void initTestGraph(TestGraph testGraph)
    {
        List<TestNode> nodes = new ArrayList<TestNode>();
        List<TestEdge> edges = new ArrayList<TestEdge>();

        TestNode node1 = new TestNode();
        TestNode node2 = new TestNode();

        TestEdge edge = new TestEdge();

        node1.setNodeId("first-node");
        node1.setIntData(1337);
        node1.setStringData("I'm a string data of node1");

        node2.setNodeId("second-node");
        node2.setIntData(2281);
        node2.setStringData("I'm a string data of node2");

        edge.setEdgeId("First edge");
        edge.setSourceNode(node1);
        edge.setTargetNode(node2);

        nodes.add(node1);
        nodes.add(node2);

        edges.add(edge);

        testGraph.setEdges(edges);
        testGraph.setNodes(nodes);
    }

    private void initGraphWithEdesInNodes(TestGraphWithEdgesInsideNodes testGraph)
    {
        TestNodeWithEdgesInside node1 = new TestNodeWithEdgesInside();
        node1.setNodeId("node1");
        node1.setIntData(223);
        node1.setStringData("I'm node 1");


        TestNodeWithEdgesInside node2 = new TestNodeWithEdgesInside();
        node2.setNodeId("node2");
        node2.setIntData(224);
        node2.setStringData("I'm node 2");

        TestEdgeWithoutSource edge1 = new TestEdgeWithoutSource();
        edge1.setEdgeId("edge1");
        edge1.setTargetNode(node2);

        TestEdgeWithoutSource edge2 = new TestEdgeWithoutSource();
        edge2.setEdgeId("edge1");
        edge2.setTargetNode(node1);

        node1.setEdges(Arrays.asList(edge1));
        node2.setEdges(Arrays.asList(edge2));

        testGraph.setNodes(Arrays.asList(node1,node2));
    }
}
