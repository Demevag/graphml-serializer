package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;

public enum FieldType
{
    NODE,
    EDGE,
    GRAPH,
    DATA,
    COMPLEX_DATA,
    NODE_COLLECTION,
    EDGE_COLLECTION,
    NODE_MAP,
    ID;

    public static FieldType getFieldType(Field field)
    {
        if(Utils.isNode(field))
            return FieldType.NODE;

        if(Utils.isEdge(field))
            return FieldType.EDGE;

        if(Utils.isDataField(field))
            return FieldType.DATA;

        if(Utils.isComplexData(field))
            return FieldType.COMPLEX_DATA;

        if(Utils.isCollectionOfNodes(field))
            return FieldType.NODE_COLLECTION;

        if(Utils.isCollectionOfEdges(field))
            return FieldType.EDGE_COLLECTION;

        if(Utils.isMapOfNodes(field))
            return FieldType.NODE_MAP;

        if(Utils.isIdField(field))
            return FieldType.ID;

        throw new IllegalArgumentException("Unknown type of field "+field.getName());
    }
}
