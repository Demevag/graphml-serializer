/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.GmlComplexData;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlComplexDataConverter extends ElementConverter<GmlComplexData, GmlElement>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlComplexData gmlComplexData) throws IllegalAccessException
    {
        List<Field> convertedFields = new ArrayList<>();

        if(Enum.class.isAssignableFrom(elementObject.getClass()))
        {
            fields.clear();
            return elementObject;
        }

        for(Field field : fields)
        {
            if(Enum.class.isAssignableFrom(field.getType()))
            {
                GmlData elementWithEnum = (GmlData) extractGmlElementForFieldType(gmlComplexData, ElementType.DATA, field);

                Object enumObject = Enum.valueOf((Class<? extends Enum>)field.getType(), (String)elementWithEnum.getData());

                set(elementObject, field, enumObject);

                convertedFields.add(field);
            }
        }

        fields.removeAll(convertedFields);

        return elementObject;
    }

    @Override
    protected GmlElement extractGmlElementForFieldType(GmlComplexData gmlComplexData, ElementType elementType, Field field)
    {
        if(elementType == ElementType.DATA)
        {
            String fieldName = field.getName();

            for(GmlData gmlData : gmlComplexData.getData())
            {
                GmlKey gmlKey = gmlData.getKey();

                if(gmlKey.getAtrrName().contains(fieldName))
                    return gmlData;
            }

            throw new IllegalStateException("No data for "+fieldName);
        }

        throw new IllegalArgumentException("Complex data class can't contain "+ elementType.name()+" in field "+field.getName());
    }

    @Override
    protected Object specificInstanciating(Class elementClass, GmlComplexData gmlComplexData) throws InstantiationException
    {
        if(Enum.class.isAssignableFrom(elementClass))
        {
            return Enum.valueOf((Class<? extends Enum>)elementClass, (String)gmlComplexData.getData().get(0).getData());
        }

        throw new InstantiationException("Can't create instance of "+elementClass.getName());
    }
}
