/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.Utils;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlGraph;
import com.demevag.gmlserializer.elements.GmlNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Implementation of parser for map of nodes as values and nodes' ids as a key set
 *
 * @see com.demevag.gmlserializer.parsers.ContainerParser ContainerParser
 * @see com.demevag.gmlserializer.elements.GmlNode GmlNode
 * @author demevag
 */
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

            Class nodeClass = nodesMap.get(key).getClass();

            String nodeClassName = nodeClass.getName().replace(nodeClass.getPackage().getName()+".", "");
            node.setId(nodeClassName+"_"+key.toString());

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
