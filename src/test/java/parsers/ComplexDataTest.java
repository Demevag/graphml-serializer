package parsers;

import com.demevag.gmlserializer.annotations.ComplexData;
import lombok.Data;

@Data
public class ComplexDataTest
{
    private String name;
    private double price;

    @ComplexData
    private TestEnum testEnum = TestEnum.HAHA;

    public ComplexDataTest(String name, double price)
    {
        this.name = name;
        this.price = price;
    }
}
