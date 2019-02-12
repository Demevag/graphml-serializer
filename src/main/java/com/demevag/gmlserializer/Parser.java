package com.demevag.gmlserializer;

import com.demevag.gmlserializer.annotations.*;
import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.ElementParser;
import com.demevag.gmlserializer.parsers.GmlDataParser;
import com.demevag.gmlserializer.parsers.GmlGraphParser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//TODO: normal work with exceptions
//TODO: add loggin
//TODO: Обрабатывать enum как data
public class Parser
{
    public GmlGraph parse(Object object) throws IllegalAccessException
    {
        Class rootClass = object.getClass();

        if (!rootClass.isAnnotationPresent(Graph.class))
            throw new IllegalArgumentException("Root object should have Graph annotation");

        Graph graphAnnotation = (Graph) rootClass.getAnnotation(Graph.class);

        String graphId = graphAnnotation.id();
        GmlEdgeType defaultEdgeType = graphAnnotation.edgedefault();

        ElementParser graphParser = new GmlGraphParser(object, graphId, defaultEdgeType);

        return (GmlGraph) graphParser.parse(object);
    }

}
