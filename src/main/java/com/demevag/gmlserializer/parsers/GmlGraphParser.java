/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.Ignore;
import com.demevag.gmlserializer.elements.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class GmlGraphParser implements ElementParser
{
    private Object parentObject;
    private String graphId;
    private GmlEdgeType defaultEdgeType;

    public GmlGraphParser(Object object, String graphId, GmlEdgeType defaultEdgeType)
    {
        this.parentObject = object;
        this.graphId = graphId;
        this.defaultEdgeType = defaultEdgeType;
    }

    @Override
    public GmlElement parse(Field field) throws IllegalAccessException
    {
        Object graphObject = Utils.getFieldData(field, parentObject);

        return parse(graphObject);
    }

    @Override
    public GmlElement parse(Object graphObject) throws IllegalAccessException
    {
        GmlGraph graph = new GmlGraph(graphId, defaultEdgeType);

        Field[] fields = graphObject.getClass().getDeclaredFields();

        ContainerParser nodeCollectionParser = new GmlNodeCollectionParser(graphObject, graph);
        ContainerParser nodeMapParser = new GmlNodeMapParser(graphObject, graph);
        ContainerParser edgeCollectionParser = new GmlEdgeCollectionParser(graphObject, defaultEdgeType);

        graph.addNodes((List<GmlNode>) nodeCollectionParser.parse(fields));
        graph.addNodes((List<GmlNode>) nodeMapParser.parse(fields));
        graph.addEdges((List<GmlEdge>) edgeCollectionParser.parse(fields));

        return graph;
    }

    @Override
    public List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlGraph> graphs = new ArrayList<>();

        for(Field field : fields)
        {
            if(Utils.isGraph(field))
                graphs.add((GmlGraph) parse(field));
        }

        return graphs;
    }
}
