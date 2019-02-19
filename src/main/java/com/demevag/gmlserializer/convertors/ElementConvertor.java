package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;

public abstract class ElementConvertor
{

    public Object convert(Class elementClass, GmlElement gmlElement) throws IllegalAccessException, InstantiationException
    {
        Object elementObject = elementClass.newInstance();

        Field[] elementFields = elementClass.getDeclaredFields();

        for(Field field : elementFields)
        {
            FieldType fieldType = FieldType.getFieldType(field);

            ElementConvertor convertor = ElementConvertorsFactory.getConvertorForField(fieldType);
            ElementSetter setter = ElementSettersFactory.getSetterForField(fieldType);

            GmlElement elementForFieldConvertor = extractGmlElementForFieldType(gmlElement, fieldType);

            setter.set(elementObject, field, convertor.convert(field.getType(), elementForFieldConvertor));
        }

        return convertSpecificFields(elementObject, elementFields, gmlElement);
    }

    public abstract Object convertSpecificFields(Object elementObject, Field[] fields, GmlElement gmlElement);
    public abstract GmlElement extractGmlElementForFieldType(GmlElement gmlElement, FieldType fieldType);
}
