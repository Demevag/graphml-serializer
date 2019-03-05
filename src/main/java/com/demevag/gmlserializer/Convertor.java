package com.demevag.gmlserializer;

import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.converters.ElementConvertersFactory;
import com.demevag.gmlserializer.converters.ElementConvertor;
import com.demevag.gmlserializer.converters.ElementType;
import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.*;

public class Convertor
{
    private Map<String, Object> resolvedNodes = new HashMap<>();
    private Set<Object> nodesWithEdgesInside = new HashSet<>();
    private Map<GmlEdge, Object> resolvedEdges = new HashMap<>();

    public Object resolve(Class graphClass, GmlGraph gmlGraph) throws IllegalAccessException, InstantiationException
    {
        ElementConvertor converter = ElementConvertersFactory.getConvertorForField(ElementType.GRAPH);

        Object graphObject = converter.convert(graphClass, gmlGraph, null);


        return graphObject;
    }
}