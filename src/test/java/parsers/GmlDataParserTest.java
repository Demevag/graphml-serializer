package parsers;

import com.demevag.gmlserializer.annotations.Edge;
import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.annotations.Node;
import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.GmlDataParser;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GmlDataParserTest
{
    private SimpleNode simpleNode;
    private GmlDataParser nodeDataParser;

    @Node
    @Data
    public class SimpleNode
    {
        @Id
        private String nodeId;

        private String stringData;
        private int intData;

        private Integer integerData; //should be ignored
    }

    @BeforeEach
    public void initNodeAndEdge()
    {
        initNode();
        initParserForSimpleElements();
    }

    @Test
    @DisplayName(value = "Should parse only primitives and strings, ignoring id")
    public void shouldParseOnlyPrimitiveAndStringsIgnoringId()
    {
        List<GmlData> nodeDataAttributes = null;

        try
        {
            nodeDataAttributes =  nodeDataParser.parse(SimpleNode.class.getDeclaredFields());
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        assertEquals(2, nodeDataAttributes.size());
    }

    @Test
    @DisplayName("Should throw an exception if field isn't primitive or string")
    public void shouldThrowExceptionIfNotAPrimitiveOrString()
    {
        Field nonPrimitiveField = null;
        try
        {
            nonPrimitiveField = SimpleNode.class.getDeclaredField("integerData");
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        Field finalNonPrimitiveField = nonPrimitiveField;
        assertThrows(IllegalArgumentException.class, ()-> nodeDataParser.parse(finalNonPrimitiveField));
    }

    @Test
    @DisplayName("Key id should be 'field name' + '_key'")
    public void shouldParseKeyIdCorrectly()
    {
        Field stringField = null;

        try
        {
            stringField = SimpleNode.class.getDeclaredField("stringData");
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        GmlKey stringDataKey = parseField(stringField).getKey();

        assertEquals("stringData_key", stringDataKey.getId());
    }

    @Test
    @DisplayName("Attr.types should be in lower case and without packages")
    public void shouldParseRightKeyType()
    {
        Field stringField = null;
        Field intField = null;

        try
        {
            stringField = SimpleNode.class.getDeclaredField("stringData");
            intField = SimpleNode.class.getDeclaredField("intData");
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        GmlKey stringDataKey = parseField(stringField).getKey();
        GmlKey intDataKey = parseField(intField).getKey();

        assertEquals("string", stringDataKey.getAttrType());
        assertEquals("int", intDataKey.getAttrType());


    }


    private GmlData parseField(Field field)
    {
        GmlData data = null;

        try
        {
            data = (GmlData) nodeDataParser.parse(field);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return data;
    }

    private void initNode()
    {
        simpleNode = new SimpleNode();

        simpleNode.setNodeId("1");
        simpleNode.setStringData("String data");
        simpleNode.setIntData(1337);
    }

    private void initParserForSimpleElements()
    {
        nodeDataParser = new GmlDataParser(GmlKeyTarget.NODE, simpleNode);
    }
}
