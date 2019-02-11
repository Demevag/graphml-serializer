import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.annotations.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
public class TestNodeWithEdgesInside
{
    @Id
    private String nodeId;

    private String stringData;
    private int intData;

    private List<TestEdgeWithoutSource> edges = new ArrayList<TestEdgeWithoutSource>();
}
