package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlNodeParser implements ElementParser
{
    private GmlGraph graph;
    private Object parentObject;

    public GmlNodeParser(Object object, GmlGraph graph)
    {
        this.graph = graph;
        this.parentObject = object;
    }

    public GmlElement parse(Field field) throws IllegalAccessException
    {
        return parse(Utils.getFieldData(field, parentObject));
    }

    @Override
    public GmlElement parse(Object nodeObject) throws IllegalAccessException
    {
        Class nodeClass = nodeObject.getClass();

        String nodeClassName = nodeClass.getName().replace(nodeClass.getPackage().getName()+".", "");

        GmlNode node = new GmlNode(nodeClassName);

        Field[] nodeFields = nodeClass.getDeclaredFields();

        ElementParser dataParser = new GmlDataParser(GmlKeyTarget.NODE, nodeObject);
        ElementParser complexDataParser = new GmlComplexDataParser(GmlKeyTarget.NODE, nodeObject);
        ElementParser subGraphParser = new GmlSubGraphParser(nodeObject);
        ContainerParser edgeCollectionParser = new GmlEdgeCollectionParser(nodeObject, graph.getDefaultEdgeType(), Utils.getId(nodeObject), nodeClassName);

        node.addDataAttribute((List<GmlData>) dataParser.parse(nodeFields));
        node.addComplexDataAttribute((List<GmlComplexData>) complexDataParser.parse(nodeFields));
        node.addSubGraph((List<GmlGraph>) subGraphParser.parse(nodeFields));
        graph.addEdges((List<GmlEdge>) edgeCollectionParser.parse(nodeFields));

        boolean hasId = false;

        for (Field nodeField : nodeFields)
        {
            if (Utils.isIdField(nodeField))
            {
                node.setId(node.getId() + "_" + Utils.getFieldData(nodeField, nodeObject));
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
            if(Utils.isNode(field))
            {
                nodes.add((GmlNode) parse(field));
            }
        }

        return nodes;
    }
}
