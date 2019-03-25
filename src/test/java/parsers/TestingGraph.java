package parsers;

import com.demevag.gmlserializer.annotations.Graph;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Graph(id ="testGraph")
@Data
public class TestingGraph
{
    private Map<String, TestingNode> nodes;
    private List<TestingEdge> edges;
}
