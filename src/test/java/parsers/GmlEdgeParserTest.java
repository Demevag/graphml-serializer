package parsers;

import com.demevag.gmlserializer.annotations.*;
import com.demevag.gmlserializer.elements.GmlEdge;
import com.demevag.gmlserializer.elements.GmlEdgeType;
import com.demevag.gmlserializer.parsers.GmlEdgeParser;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GmlEdgeParserTest
{


    @Test
    @DisplayName("Should set as it is if source/target field is string")
    public void shouldSetAsItIsIfSourceTargetFieldIsString() throws IllegalAccessException
    {
        EdgeWithSourceTargetIdAsString edgeObject = new EdgeWithSourceTargetIdAsString();
        edgeObject.setSourceId("sourceId");
        edgeObject.setTargetId("targetId");
        edgeObject.setId("edge_id");

        GmlEdgeParser edgeParser = new GmlEdgeParser(edgeObject, GmlEdgeType.UNDIRECTED);

        GmlEdge edge = (GmlEdge) edgeParser.parse(edgeObject);

        assertEquals("Node_sourceId", edge.getSourceId());
        assertEquals("Node_targetId", edge.getTargetId());

    }

    @Test
    @DisplayName("Should extract id from object source/target")
    public void shouldExtractId()
    {
        EdgeWithSourceAndTargetAsNodes edgeObject = new EdgeWithSourceAndTargetAsNodes();
        SimpleNode sourceNode = new SimpleNode();
        SimpleNode targetNode = new SimpleNode();

        sourceNode.setNodeId("node1");
        targetNode.setNodeId("node2");

        edgeObject.setId("1");
        edgeObject.setSourceNode(sourceNode);
        edgeObject.setTargetNode(targetNode);

        GmlEdgeParser edgeParser = new GmlEdgeParser(edgeObject, GmlEdgeType.UNDIRECTED);

        GmlEdge edge = null;
        try
        {
            edge = (GmlEdge) edgeParser.parse(edgeObject);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        assertEquals("GmlEdgeParserTest$SimpleNode_node1", edge.getSourceId());
        assertEquals("GmlEdgeParserTest$SimpleNode_node2", edge.getTargetId());

    }

    @Test
    @DisplayName("Should throw an exception if node class doesn't specified for string source/target")
    public void shouldThrowExceptionIfNodeClassNameDoesntSpecified()
    {
        EdgeThrowingException edgeObject = new EdgeThrowingException();
        edgeObject.setSourceId("sourceId");
        edgeObject.setTargetId("targetId");
        edgeObject.setId("edge_id");

        GmlEdgeParser edgeParser = new GmlEdgeParser(edgeObject, GmlEdgeType.UNDIRECTED);

        assertThrows(IllegalStateException.class, ()-> edgeParser.parse(edgeObject));
    }

    @Edge
    @Data
    public class EdgeWithSourceTargetIdAsString
    {
        @EdgeSource(nodeClassName = "Node")
        private String sourceId;

        @EdgeTarget(nodeClassName = "Node")
        private String targetId;

        @Id
        private String id;
    }

    @Node
    @Data
    public class SimpleNode
    {
        @Id
        private String nodeId;
    }

    @Edge
    @Data
    public class EdgeWithSourceAndTargetAsNodes
    {
        @Id
        private String id;

        @EdgeTarget
        private SimpleNode targetNode;
        @EdgeSource
        private SimpleNode sourceNode;

    }

    @Edge
    @Data
    public class EdgeThrowingException
    {
        @Id
        private String id;

        @EdgeSource()
        private String sourceId;

        @EdgeTarget()
        private String targetId;
    }
}
