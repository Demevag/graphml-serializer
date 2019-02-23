package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlComplexData;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlKey;

import java.lang.reflect.Field;
import java.util.List;

public class GmlComplexDataConvertor extends ElementConvertor<GmlComplexData, GmlElement>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlComplexData gmlComplexData)
    {
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
}
