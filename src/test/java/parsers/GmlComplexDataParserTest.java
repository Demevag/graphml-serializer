package parsers;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.elements.GmlComplexData;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlKeyTarget;
import com.demevag.gmlserializer.parsers.GmlComplexDataParser;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class GmlComplexDataParserTest
{
    private ClassWithComplexData testObject;
    private GmlComplexDataParser complexDataParser;

    @BeforeEach
    public void init()
    {
        initClassWithComplexData();
        initComplexDataParser();
    }

    @Test
    @DisplayName("Should parse all with ComplexData annotation")
    public void shouldParseAllWithComplexDataAnnotation()
    {
        Class testClass = ClassWithComplexData.class;
        List<GmlComplexData> complexData = null;
        try
        {
            complexData = (List<GmlComplexData>) complexDataParser.parse(testClass.getDeclaredFields());
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        assertEquals(4, complexData.size());
    }

    @Test
    @DisplayName("Should parse every element of the collection")
    public void shouldParseEveryElementOfCollection()
    {
        int numberOfFieldsInFloat = Float.class.getDeclaredFields().length;
        int sizeOfFloatList = testObject.getListOfFloatData().size();

        Field floatListField = null;
        GmlComplexData complexData = null;
        try
        {
            floatListField = ClassWithComplexData.class.getDeclaredField("listOfFloatData");
            complexData = (GmlComplexData) complexDataParser.parse(floatListField);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        assertEquals(numberOfFieldsInFloat * sizeOfFloatList, complexData.getData().size());
    }

    @Test
    @DisplayName("Should parse enum as a string")
    public void shouldParseEnumAsString()
    {
        Field enumField = null;
        GmlComplexData complexData = null;
        try
        {
            enumField = ClassWithComplexData.class.getDeclaredField("simpleEnumData");
            complexData = (GmlComplexData) complexDataParser.parse(enumField);
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        assertEquals(1, complexData.getData().size());

        GmlData data = complexData.getData().get(0);

        assertEquals("ONE", data.getData());
    }

    @Data
    public class ClassWithComplexData
    {
        @Id
        private String id;

        @ComplexData
        private Double doubleData;
        @ComplexData
        private List<Float> listOfFloatData;
        @ComplexData
        private String stringData;
        private int intData;
        @ComplexData
        private SimpleEnum simpleEnumData;

    }

    public enum SimpleEnum
    {
        ONE,
        TWO
    }

    private void initComplexDataParser()
    {
        complexDataParser = new GmlComplexDataParser(GmlKeyTarget.NODE, testObject);
    }

    private void initClassWithComplexData()
    {
        testObject = new ClassWithComplexData();

        testObject.setId("1");
        testObject.setIntData(123);
        testObject.setDoubleData(3.14);
        testObject.setSimpleEnumData(SimpleEnum.ONE);
        testObject.setStringData("String data");
        testObject.setListOfFloatData(Arrays.asList(3.14f, 2.71f));
    }
}
