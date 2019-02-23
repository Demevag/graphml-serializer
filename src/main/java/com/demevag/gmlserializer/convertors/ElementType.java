package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;

public enum ElementType
{
    NODE,
    EDGE,
    GRAPH,
    DATA,
    COMPLEX_DATA,
    ID;

    public static ElementType getTypeForField(Field field)
    {
        if(Utils.isNode(field))
            return ElementType.NODE;

        if(Utils.isEdge(field))
            return ElementType.EDGE;

        if(Utils.isDataField(field))
            return ElementType.DATA;

        if(Utils.isComplexData(field))
            return ElementType.COMPLEX_DATA;

        if(Utils.isIdField(field))
            return ElementType.ID;

        throw new IllegalArgumentException("Unknown type of field "+field.getName());
    }
}
