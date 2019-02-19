package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;

public  abstract class ElementConvertor <T extends GmlElement>
{

    public Object convert(Class elementClass, T gmlElement) throws IllegalAccessException, InstantiationException
    {
        Object elementObject = elementClass.newInstance();

        Field[] elementFields = elementClass.getDeclaredFields();

        for(Field field : elementFields)
        {
            FieldType fieldType = FieldType.getFieldType(field);

            ElementConvertor convertor = ElementConvertorsFactory.getConvertorForField(fieldType);
            ElementSetter setter = ElementSettersFactory.getSetterForField(fieldType);

            GmlElement elementForFieldConvertor = extractGmlElementForFieldType(gmlElement, fieldType, field);

            setter.set(elementObject, field, convertor.convert(field.getType(), elementForFieldConvertor));
        }

        return convertSpecificFields(elementObject, elementFields, gmlElement);
    }

    protected abstract Object convertSpecificFields(Object elementObject, Field[] fields, T gmlElement);
    protected abstract GmlElement extractGmlElementForFieldType(T gmlElement, FieldType fieldType, Field field);
}
