/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer;

import com.demevag.gmlserializer.converters.ElementConvertersFactory;
import com.demevag.gmlserializer.converters.ElementConverter;
import com.demevag.gmlserializer.converters.ElementType;
import com.demevag.gmlserializer.elements.*;

import java.util.*;

public class Converter
{
    private Map<String, Object> resolvedNodes = new HashMap<>();
    private Set<Object> nodesWithEdgesInside = new HashSet<>();
    private Map<GmlEdge, Object> resolvedEdges = new HashMap<>();

    public Object resolve(Class graphClass, GmlGraph gmlGraph) throws IllegalAccessException, InstantiationException
    {
        ElementConverter converter = ElementConvertersFactory.getConvertorForField(ElementType.GRAPH);

        Object graphObject = converter.convert(graphClass, gmlGraph, null);


        return graphObject;
    }
}