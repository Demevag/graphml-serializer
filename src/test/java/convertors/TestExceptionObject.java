package convertors;

import com.demevag.gmlserializer.annotations.ComplexData;
import lombok.Data;

@Data
public class TestExceptionObject
{
    @ComplexData
    private Integer integerValue;
}
