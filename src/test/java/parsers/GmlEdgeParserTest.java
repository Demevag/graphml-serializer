package parsers;

import com.demevag.gmlserializer.annotations.Edge;
import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.elements.GmlEdge;
import com.demevag.gmlserializer.elements.GmlEdgeType;
import com.demevag.gmlserializer.parsers.GmlEdgeParser;
import lombok.Data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GmlEdgeParserTest
{
    @Edge
    @Data
    public class EdgeWithSourceIdAsString
    {
        @EdgeSource
        private String sourceId;

        @EdgeTarget
        private String targetId;

        @Id
        private String id;
    }

    @Test
    public void shouldSetAsItIsIfStringSourceField() throws IllegalAccessException
    {
        EdgeWithSourceIdAsString edgeObject = new EdgeWithSourceIdAsString();
        edgeObject.setSourceId("sourceId");
        edgeObject.setTargetId("targetId");
        edgeObject.setId("edge_id");

        GmlEdgeParser edgeParser = new GmlEdgeParser(edgeObject, GmlEdgeType.UNDIRECTED);

        GmlEdge edge = (GmlEdge) edgeParser.parse(edgeObject);

        assertEquals("sourceId", edge.getSourceId());

    }
}
