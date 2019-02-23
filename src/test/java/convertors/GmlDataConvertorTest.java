package convertors;

import com.demevag.gmlserializer.convertors.GmlDataConvertor;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlKey;
import com.demevag.gmlserializer.elements.GmlKeyTarget;
import com.demevag.gmlserializer.elements.GmlNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GmlDataConvertorTest
{
    @Test
    @DisplayName("Should just return data from GmlData")
    public void shouldReturnDataFromGmlData()
    {
        GmlKey gmlKey = new GmlKey("id", GmlKeyTarget.NODE, "attrName");
        GmlData gmlData = new GmlData(gmlKey, "String data");

        GmlDataConvertor dataConvertor = new GmlDataConvertor();

        assertEquals("String data", dataConvertor.convert(String.class, gmlData, new GmlNode("id")), "For String field");

        gmlData.setData(3.14);

        assertEquals(3.14, dataConvertor.convert(double.class, gmlData, new GmlNode("id")), "For double field");

    }
}
