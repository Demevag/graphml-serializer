package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.GmlEdgeType;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlGraph;

import java.lang.reflect.Field;
import java.util.List;

//TODO: implement
public class GmlSubGraphParser extends ElementParser
{
    public GmlSubGraphParser(Object object)
    {

    }

    public GmlElement parse(Field field) throws IllegalAccessException
    {
        return null;
    }

    public List<GmlGraph> parse(Field[] fields) throws IllegalAccessException
    {
        return null;
    }
}
