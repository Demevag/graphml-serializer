import com.demevag.gmlserializer.annotations.Graph;
import lombok.Data;

import java.util.List;

@Graph(id ="testGraph")
@Data
public class TestGraph
{
    private List<TestNode> nodes;
    private List<TestEdge> edges;
}
