package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.GmlEdgeType;
import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.List;

//TODO: implement
public class GmlEdgeCollectionParser extends ElementParser
{

    public GmlEdgeCollectionParser(Object object, GmlEdgeType defaultEdgeType)
    {

    }

    public GmlEdgeCollectionParser(Object object, GmlEdgeType defaultEdgeType, String sourceId, String sourceClassName)
    {

    }

    public GmlElement parse(Field field) throws IllegalAccessException
    {
        return null;
    }

    public List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException
    {
        return null;
    }
}
