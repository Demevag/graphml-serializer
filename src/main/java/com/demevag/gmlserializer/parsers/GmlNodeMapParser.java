package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlGraph;
import com.demevag.gmlserializer.elements.GmlNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GmlNodeMapParser implements ContainerParser
{
    private Object parentObject;
    private GmlGraph graph;

    public GmlNodeMapParser(Object object, GmlGraph graph)
    {
        this.parentObject = object;
        this.graph = graph;
    }

    @Override
    public List<? extends GmlElement> parse(Field field) throws IllegalAccessException
    {
        ElementParser nodeParser = new GmlNodeParser(parentObject, graph);

        List<GmlNode> nodes = new ArrayList<>();

        Map nodesMap = (Map) Utils.getFieldData(field, parentObject);

        for (Object key : nodesMap.keySet())
        {
            GmlNode node = (GmlNode) nodeParser.parse(nodesMap.get(key));
            node.setId(key.toString());

            nodes.add(node);
        }


        return nodes;
    }

    @Override
    public List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlNode> nodes = new ArrayList<>();

        for (Field field : fields)
        {
            if (Utils.isMapOfNodes(field))
                nodes.addAll((Collection<? extends GmlNode>) parse(field));
        }

        return nodes;
    }
}
