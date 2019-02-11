import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.annotations.Node;
import lombok.Data;

@Node
@Data
public class TestNode
{
    @Id
    private String nodeId;

    private String stringData;
    private int intData;
}
