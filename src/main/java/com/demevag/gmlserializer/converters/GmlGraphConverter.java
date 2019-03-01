package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlGraph;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlGraphConverter extends ElementConvertor<GmlGraph, GmlElement>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlGraph gmlElement) throws IllegalAccessException
    {
        return elementObject;
    }

    @Override
    protected GmlElement extractGmlElementForFieldType(GmlGraph gmlElement, ElementType elementType, Field field)
    {
        throw new IllegalArgumentException("Graph class can't contain "+ elementType.name()+" field");
    }

    @Override
    protected List<GmlElement> extractGmlElementsForContainerField(GmlGraph gmlGraph, ContainerType elementType, Field containerField, GmlElement parentElement)
    {
        switch (elementType)
        {
            case NODE_COLLECTION:{
                return new ArrayList<>(gmlGraph.getNodes());
            }
            case NODE_MAP:{
                return new ArrayList<>(gmlGraph.getNodes());
            }
            case EDGE_COLLECTION:{
                return new ArrayList<>(gmlGraph.getEdges());
            }
        }

        throw new IllegalStateException("Graph can't contain "+ elementType.name());
    }
}
