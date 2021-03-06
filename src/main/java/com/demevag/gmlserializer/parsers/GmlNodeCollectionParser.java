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

/**
 * Implementation of parser for collection of nodes
 *
 * @see com.demevag.gmlserializer.parsers.ContainerParser ContainerParser
 * @see com.demevag.gmlserializer.elements.GmlNode GmlNode
 * @author demevag
 */
public class GmlNodeCollectionParser implements ContainerParser
{
    private Object parentObject;
    private GmlGraph graph;

    public GmlNodeCollectionParser(Object object, GmlGraph graph)
    {
        this.parentObject = object;
        this.graph = graph;
    }

    public List<? extends GmlElement> parse(Field field) throws IllegalAccessException
    {
        Collection collection = (Collection) Utils.getFieldData(field, parentObject);
        ElementParser nodeParser = new GmlNodeParser(parentObject, graph);

        List<GmlNode> nodes = new ArrayList<GmlNode>();

        for (Object o : collection)
        {
            nodes.add((GmlNode) nodeParser.parse(o));
        }

        return nodes;
    }

    public List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlNode> nodes = new ArrayList<>();

        for(Field field : fields)
        {
            if(Utils.isCollectionOfNodes(field))
                nodes.addAll((Collection<? extends GmlNode>) parse(field));
        }

        return nodes;
    }
}
