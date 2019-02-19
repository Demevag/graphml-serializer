package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;

public class GmlDataConvertor extends ElementConvertor
{
    @Override
    public Object convertSpecificFields(Object elementObject, Field[] fields, GmlElement gmlElement)
    {
        return null;
    }

    @Override
    public GmlElement extractGmlElementForFieldType(GmlElement gmlElement, FieldType fieldType)
    {
        return null;
    }
}
