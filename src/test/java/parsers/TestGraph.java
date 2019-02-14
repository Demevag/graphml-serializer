package parsers;

import com.demevag.gmlserializer.annotations.Graph;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Graph(id ="testGraph")
@Data
public class TestGraph
{
    private Map<String,TestNode> nodes;
    private List<TestEdge> edges;
}
