package parsers;

import com.demevag.gmlserializer.annotations.*;
import convertors.TestObject;
import lombok.Data;

@Edge
@Data
public class TestingEdge
{
    @Id
    private String edgeId;

    @EdgeSource
    private TestingNode sourceNode;

    @EdgeTarget
    private TestingNode targetNode;


    @ComplexData
    private TestObject complexData;

    @ComplexData
    private TestEnum testEnum;
}
