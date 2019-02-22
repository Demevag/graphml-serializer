package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlGraph;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlGraphConvertor extends ElementConvertor<GmlGraph, GmlElement>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlGraph gmlElement) throws IllegalAccessException
    {
        return elementObject;
    }

    @Override
    protected GmlElement extractGmlElementForFieldType(GmlGraph gmlElement, FieldType fieldType, Field field)
    {
        throw new IllegalArgumentException("Graph class can't contain "+fieldType.name()+" field");
    }

    @Override
    protected List<GmlElement> extractGmlElementsForContainerField(GmlGraph gmlGraph, FieldType fieldType, Field containerField, GmlElement parentElement)
    {
        switch (fieldType)
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

        throw new IllegalStateException("Graph can't contain "+fieldType.name());
    }
}
