import com.demevag.gmlserializer.annotations.Graph;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Graph(id="testGraph2")
@Data
public class TestGraphWithEdgesInsideNodes
{
    private List<TestNodeWithEdgesInside> nodes = new ArrayList<TestNodeWithEdgesInside>();

}
