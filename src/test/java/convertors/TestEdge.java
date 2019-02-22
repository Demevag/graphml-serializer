package convertors;

import com.demevag.gmlserializer.annotations.*;
import lombok.Data;

@Edge
@Data
public class TestEdge
{
    @Id
    private String id;

    @EdgeSource
    private String sourceNodeId;
    @EdgeTarget
    private String targetNodeId;

    private String stringData;
    @ComplexData
    private TestObject complexData;
}
