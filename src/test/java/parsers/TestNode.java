package parsers;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.annotations.Node;
import lombok.Data;

import java.util.List;

@Node
@Data
public class TestNode
{
    @Id
    private String nodeId;

    private String stringData;
    private int intData;

    @ComplexData
    private List<ComplexDataTest> complexData;
}
