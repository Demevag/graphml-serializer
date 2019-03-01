package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlEdgeConverter extends ElementConvertor<GmlEdge, GmlElement>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlEdge gmlEdge) throws IllegalAccessException
    {
        List<Field> handledFields = new ArrayList<>();

        for(Field field : fields)
        {
            if(field.isAnnotationPresent(EdgeSource.class))
            {
                if(field.getType().equals(String.class))
                    set(elementObject, field, gmlEdge.getSourceId());
                //todo: else ?

                handledFields.add(field);
            }
            else if(field.isAnnotationPresent(EdgeTarget.class))
            {
                if(field.getType().equals(String.class))
                    set(elementObject, field, gmlEdge.getTargetId());
                //todo: else ?

                handledFields.add(field);
            }

        }

        fields.removeAll(handledFields);

        return elementObject;
    }

    @Override
    protected GmlElement extractGmlElementForFieldType(GmlEdge gmlEdge, ElementType elementType, Field field)
    {
        String fieldName = field.getName();

        switch (elementType)
        {
            case DATA:{
                for(GmlData gmlData : gmlEdge.getDataAttributes())
                {
                    GmlKey key = gmlData.getKey();

                    if(key.getAtrrName().contains(fieldName))
                        return gmlData;
                }

                throw new IllegalStateException("No data for "+ fieldName);
            }
            case COMPLEX_DATA:{
                for(GmlComplexData complexData : gmlEdge.getComplexDataAttributes())
                {
                    GmlKey key = complexData.getData().get(0).getKey();

                    if(key.getAtrrName().contains(Utils.getClassNameWithoutPackage(field.getType())))
                        return complexData;
                }

                throw new IllegalStateException("No complex data for " +fieldName);
            }
            case ID:{
                GmlKey keyForIdData = new GmlKey("id_field", GmlKeyTarget.EDGE, fieldName);
                return new GmlData(keyForIdData, gmlEdge.getId());
            }
        }

        throw new IllegalArgumentException("Edge class can't contain "+ elementType.name()+" field");
    }
}
