package convertors;

import com.demevag.gmlserializer.convertors.GmlEdgeConvertor;
import com.demevag.gmlserializer.elements.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GmlEdgeConvertorTest
{
    @Test
    @DisplayName("Should convert gmlEdge to Edge object")
    public void shouldConvertGmlEdgeeToObject()
    {
        GmlEdge gmlEdge = new GmlEdge("id", GmlEdgeType.UNDIRECTED);

        GmlKey intValKey = new GmlKey("id", GmlKeyTarget.NODE, "TestObject_intValue_");
        GmlKey stringValKey = new GmlKey("id", GmlKeyTarget.NODE, "TestObject_stringValue_");
        GmlKey stringDataKey = new GmlKey("id", GmlKeyTarget.EDGE, "_stringData_");

        GmlData intData = new GmlData(intValKey, 1337);
        GmlData stringDataInComplex = new GmlData(stringValKey, "I'm a string");
        GmlData stringData = new GmlData(stringDataKey, "I'm string data");

        GmlComplexData complexData = new GmlComplexData();
        complexData.addDataAttribute(Arrays.asList(intData, stringDataInComplex));

        gmlEdge.addDataAttribute(stringData);
        gmlEdge.addComplexDataAttribute(complexData);

        GmlEdgeConvertor convertor = new GmlEdgeConvertor();

        TestEdge testEdge = null;
        try
        {
            testEdge = (TestEdge) convertor.convert(TestEdge.class, gmlEdge, new GmlNode("id"));
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        }

        assertEquals("I'm string data", testEdge.getStringData());
        assertEquals(1337, testEdge.getComplexData().getIntValue());
        assertEquals("I'm a string", testEdge.getComplexData().getStringValue());
    }
}
