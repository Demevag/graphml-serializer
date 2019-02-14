package parsers;

import com.demevag.gmlserializer.parsers.Utils;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public  class  UtilsTest
{

    @Data
    public class TestClass <N extends TestNode>
    {
        private Map<String, N> nodesFromGeneric;
        private Map<String, TestNode> nodes;
    }

    @Test
    public void shouldRecogniseMapOfNodes() throws NoSuchFieldException
    {
        Class testClass = TestClass.class;

        Field nodesFromGenericField = testClass.getDeclaredField("nodesFromGeneric");
        Field nodesField = testClass.getDeclaredField("nodes");

        assertTrue(Utils.isMapOfNodes(nodesFromGenericField));
        assertTrue(Utils.isMapOfNodes(nodesField));
    }
}
