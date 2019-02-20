package convertors;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.convertors.GmlComplexDataConvertor;
import com.demevag.gmlserializer.elements.GmlComplexData;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlKey;
import com.demevag.gmlserializer.elements.GmlKeyTarget;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GmlComplexDataConvertorTest
{
    @Test
    @DisplayName("Should convert GmlComplexData to object")
    public void shouldConvertGmlComplexDataToObject()
    {
        GmlKey intValKey = new GmlKey("id",GmlKeyTarget.NODE, "_intValue_");
        GmlKey stringValKey = new GmlKey("id", GmlKeyTarget.NODE, "_stringValue_");

        GmlData intData = new GmlData(intValKey, 1337);
        GmlData stringData = new GmlData(stringValKey, "I'm a string");

        GmlComplexData complexData = new GmlComplexData();
        complexData.addDataAttribute(Arrays.asList(intData, stringData));

        GmlComplexDataConvertor convertor = new GmlComplexDataConvertor();

        TestObject testObject = null;
        try
        {
            testObject = (TestObject) convertor.convert(TestObject.class, complexData);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        }

        assertEquals(1337, testObject.getIntValue());
        assertEquals("I'm a string", testObject.getStringValue());
    }

    @Test
    @DisplayName("Should throw exception if no data for field")
    public void shouldThrowExceptionIfNoData()
    {
        GmlComplexDataConvertor convertor = new GmlComplexDataConvertor();

        assertThrows(IllegalStateException.class, ()-> convertor.convert(TestObject.class, new GmlComplexData()));
    }

    @Test
    @DisplayName("Should throw exception if complex data contains non data fields")
    public void shouldThrowExceptionIfNonDataFields()
    {
        GmlComplexDataConvertor convertor = new GmlComplexDataConvertor();

        assertThrows(IllegalArgumentException.class, ()->convertor.convert(TestExceptionObject.class, new GmlComplexData()));
    }
}
