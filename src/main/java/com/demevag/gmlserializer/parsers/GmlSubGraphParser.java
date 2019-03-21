/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.Graph;
import com.demevag.gmlserializer.annotations.SubGraph;
import com.demevag.gmlserializer.elements.GmlEdgeType;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlGraph;
import com.demevag.gmlserializer.elements.GmlNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlSubGraphParser implements ElementParser
{
    private Object parentObject;

    public GmlSubGraphParser(Object object)
    {
        this.parentObject = object;
    }

    public GmlElement parse(Field field) throws IllegalAccessException
    {
        Class fieldClass = field.getType();
        SubGraph subGraphAnnotation = field.getAnnotation(SubGraph.class);

        String graphId = subGraphAnnotation.id();
        GmlEdgeType defaultEdgeType = subGraphAnnotation.edgedefault();

        if (field.isAnnotationPresent(Graph.class))
        {
            ElementParser graphParser = new GmlGraphParser(parentObject, graphId , defaultEdgeType);
            return graphParser.parse(field);
        }

        if (!Utils.isCollectionOfNodes(field))
            throw new IllegalArgumentException(field.getName() + " is neither Graph nor Collection of nodes");

        GmlGraph graph = new GmlGraph(graphId, defaultEdgeType);
        ContainerParser nodeCollectionParser = new GmlNodeCollectionParser(parentObject, graph);
        graph.addNodes((List<GmlNode>) nodeCollectionParser.parse(field));

        return graph;
    }

    @Override
    public GmlElement parse(Object object) throws IllegalAccessException
    {
        return null;
    }

    public List<GmlGraph> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlGraph> graphs = new ArrayList<GmlGraph>();

        for(Field field : fields)
        {
            if(Utils.isSubGraph(field))
                graphs.add((GmlGraph) parse(field));
        }

        return graphs;
    }
}
