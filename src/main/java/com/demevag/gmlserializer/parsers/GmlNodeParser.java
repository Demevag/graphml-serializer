package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.Ignore;
import com.demevag.gmlserializer.annotations.SubGraph;
import com.demevag.gmlserializer.elements.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlNodeParser extends ElementParser
{
    private GmlGraph graph;
    private Object object;

    public GmlNodeParser(Object object, GmlGraph graph)
    {
        this.graph = graph;
        this.object = object;
    }

    public GmlElement parse(Field field) throws IllegalAccessException
    {
        Class nodeClass = object.getClass();

        GmlNode node = new GmlNode(nodeClass.getName());

        Field[] nodeFields = nodeClass.getDeclaredFields();

        ElementParser dataParser = new GmlDataParser(GmlKeyTarget.NODE, object);
        ElementParser subGraphParser = new GmlSubGraphParser(object);
        ElementParser edgeCollectionParser = new GmlEdgeCollectionParser(object, graph.getDefaultEdgeType(), getId(object), nodeClass.getName());

        node.addDataAttribute((List<GmlData>) dataParser.parse(nodeFields));
        node.addSubGraph((List<GmlGraph>) subGraphParser.parse(nodeFields));
        graph.addEdges((List<GmlEdge>) edgeCollectionParser.parse(nodeFields));

        boolean hasId = false;

        for (Field nodeField : nodeFields)
        {
            if (isIdField(nodeField))
            {
                node.setId(node.getId() + "_" + getFieldData(nodeField, object));
                hasId = true;

                continue;
            }

        }

        if (!hasId)
            throw new IllegalStateException(nodeClass.getName() + " has no id field");

        return node;
    }

    public List<GmlNode> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlNode> nodes = new ArrayList<GmlNode>();

        for(Field field : fields)
        {
            if(isNode(field))
            {
                nodes.add((GmlNode) parse(field));
            }
        }

        return nodes;
    }
}
