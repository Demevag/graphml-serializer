package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;

public enum ContainerType
{
    NODE_COLLECTION,
    EDGE_COLLECTION,
    NODE_MAP;

    public static ContainerType getTypeForField(Field field)
    {

        if(Utils.isCollectionOfNodes(field))
            return ContainerType.NODE_COLLECTION;

        if(Utils.isCollectionOfEdges(field))
            return ContainerType.EDGE_COLLECTION;

        if(Utils.isMapOfNodes(field))
            return ContainerType.NODE_MAP;


        throw new IllegalArgumentException("Unknown type of field "+field.getName());
    }
}
