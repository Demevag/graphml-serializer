import com.demevag.gmlserializer.annotations.Edge;
import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.annotations.Id;
import lombok.Data;

@Edge
@Data
public class TestEdgeWithoutSource
{
    @Id
    private String edgeId;

    @EdgeTarget
    private TestNodeWithEdgesInside targetNode;
}
