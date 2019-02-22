package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  abstract class ElementConvertor <T extends GmlElement>
{

    public Object convert(Class elementClass, T gmlElement) throws IllegalAccessException, InstantiationException
    {
        Object elementObject = elementClass.newInstance();

        List<Field> elementFields = new ArrayList<>(Arrays.asList(elementClass.getDeclaredFields()));

        elementObject = convertSpecificFields(elementObject, elementFields, gmlElement);

        for(Field field : elementFields)
        {
            FieldType fieldType = FieldType.getFieldType(field);

            ElementConvertor convertor = ElementConvertorsFactory.getConvertorForField(fieldType);

            GmlElement elementForFieldConvertor = extractGmlElementForFieldType(gmlElement, fieldType, field);

            set(elementObject, field, convertor.convert(field.getType(), elementForFieldConvertor));
        }

        return elementObject;
    }

    protected void set(Object parentObject, Field field, Object data) throws IllegalAccessException
    {
        field.setAccessible(true);

        field.set(parentObject, data);

        field.setAccessible(false);
    }

    protected abstract Object convertSpecificFields(Object elementObject, List<Field> fields, T gmlElement) throws IllegalAccessException;
    protected abstract GmlElement extractGmlElementForFieldType(T gmlElement, FieldType fieldType, Field field);
}
