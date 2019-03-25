/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.Utils;

import java.lang.reflect.Field;

public enum ContainerType
{
    NODE_COLLECTION,
    EDGE_COLLECTION,
    COMPLEX_DATA_COLLECTION,
    NODE_MAP;

    public static ContainerType getTypeForField(Field field)
    {

        if(Utils.isCollectionOfNodes(field))
            return ContainerType.NODE_COLLECTION;

        if(Utils.isCollectionOfEdges(field))
            return ContainerType.EDGE_COLLECTION;

        if(Utils.isMapOfNodes(field))
            return ContainerType.NODE_MAP;

        if(Utils.isCollection(field) && field.isAnnotationPresent(ComplexData.class))
            return ContainerType.COMPLEX_DATA_COLLECTION;


        throw new IllegalArgumentException("Unknown type of field "+field.getName());
    }
}
