package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.List;

public class GmlDataConverter extends ElementConvertor<GmlData, GmlElement>
{
    @Override
    public Object convert(Class fieldsClass, GmlData gmlData, GmlElement parentElement)
    {
        return gmlData.getData();
    }

    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlData gmlData)
    {
        return gmlData.getData();
    }

    @Override
    protected GmlElement extractGmlElementForFieldType(GmlData gmlElement, ElementType elementType, Field field)
    {
        return null;
    }
}
