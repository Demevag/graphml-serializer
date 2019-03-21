/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.List;

public class GmlDataConverter extends ElementConverter<GmlData, GmlElement>
{
    @Override
    public Object convert(Class fieldsClass, GmlData gmlData, GmlElement parentElement)
    {
        Object data = gmlData.getData();

        if(gmlData.getKey().getId().equals("id_field"))
        {
            String idString = (String)data;

            String[] idStringParts = idString.split("_");

            return idStringParts[idStringParts.length - 1];
        }

        return data;
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
