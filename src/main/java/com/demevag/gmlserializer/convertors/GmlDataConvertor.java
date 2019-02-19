package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;

public class GmlDataConvertor extends ElementConvertor<GmlData>
{
    @Override
    public Object convert(Class fieldsClass, GmlData gmlData)
    {
        return gmlData.getData();
    }

    @Override
    protected Object convertSpecificFields(Object elementObject, Field[] fields, GmlData gmlData)
    {
        return gmlData.getData();
    }

    @Override
    protected GmlElement extractGmlElementForFieldType(GmlData gmlElement, FieldType fieldType, Field field)
    {
        return null;
    }
}
