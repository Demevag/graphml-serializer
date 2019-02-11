import com.demevag.gmlserializer.annotations.Edge;
import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.annotations.Id;
import lombok.Data;

@Edge
@Data
public class TestEdge
{
    @Id
    private String edgeId;

    @EdgeSource
    private TestNode sourceNode;

    @EdgeTarget
    private TestNode targetNode;

}
