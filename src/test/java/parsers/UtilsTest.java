package parsers;

import com.demevag.gmlserializer.parsers.Utils;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public  class  UtilsTest
{

    @Test
    @DisplayName("Should return data type 'string' for all except primitives")
    public void shouldRetStringTypeForAllExceptPrimitives()
    {
        assertEquals("int", Utils.getDataType(int.class));
        assertEquals("string", Utils.getDataType(String.class));
        assertEquals("string", Utils.getDataType(Integer.class));
    }

    @Test
    @DisplayName("Should return collection type")
    public void shouldReturnCollectionType()
    {
        Field integerListField = null;
        Field stringDequeField = null;
        Field doubleSetField = null;
        Field longQueueField = null;

        try
        {
            integerListField = TestClass.class.getDeclaredField("integerList");
            stringDequeField = TestClass.class.getDeclaredField("stringDeque");
            doubleSetField = TestClass.class.getDeclaredField("doubleSet");
            longQueueField = TestClass.class.getDeclaredField("longQueue");
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        assertEquals(Integer.class, Utils.getCollectionArgClass(integerListField));
        assertEquals(String.class, Utils.getCollectionArgClass(stringDequeField));
        assertEquals(Double.class, Utils.getCollectionArgClass(doubleSetField));
        assertEquals(Long.class, Utils.getCollectionArgClass(longQueueField));
    }

    @Test
    @DisplayName("Should return value class of map or parent class of generic value")
    public void shouldReturnValueClass()
    {
        Class testClass = TestClass.class;

        Field nodesFromGenericField = null;
        Field nodesField = null;
        try
        {
            nodesField = testClass.getDeclaredField("nodes");
            nodesFromGenericField = testClass.getDeclaredField("nodesFromGeneric");
        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        assertEquals(TestNode.class, Utils.getMapValueClass(nodesField));
        assertEquals(TestNode.class, Utils.getMapValueClass(nodesFromGenericField));
    }

    @Test
    @DisplayName("Should recognise map of nodes")
    public void shouldRecogniseMapOfNodes() throws NoSuchFieldException
    {
        Class testClass = TestClass.class;

        Field nodesFromGenericField = testClass.getDeclaredField("nodesFromGeneric");
        Field nodesField = testClass.getDeclaredField("nodes");

        assertTrue(Utils.isMapOfNodes(nodesFromGenericField));
        assertTrue(Utils.isMapOfNodes(nodesField));
    }

    @Data
    public class TestClass <N extends TestNode>
    {
        private Map<String, N> nodesFromGeneric;
        private Map<String, TestNode> nodes;


        private List<Integer> integerList = new ArrayList<>();
        private Deque<String> stringDeque = new ArrayDeque<>();
        private Set<Double> doubleSet = new HashSet<>();
        private Queue<Long> longQueue = new PriorityQueue<>();
    }
}
