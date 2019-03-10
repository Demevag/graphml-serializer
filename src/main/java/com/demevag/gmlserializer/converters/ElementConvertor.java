package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.annotations.Ignore;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  abstract class ElementConvertor <T extends GmlElement, P extends GmlElement>
{

    public Object convert(Class elementClass, T gmlElement, P parentElement) throws IllegalAccessException, InstantiationException
    {
        Object elementObject = elementClass.newInstance();

        List<Field> elementFields = new ArrayList<>(Arrays.asList(elementClass.getDeclaredFields()));

        elementObject = convertSpecificFields(elementObject, elementFields, gmlElement);

        for(Field field : elementFields)
        {
            if(field.isAnnotationPresent(Ignore.class))
                continue;

            if( (Utils.isCollection(field) || Utils.isMap(field)))
            {
                ContainerType containerType = ContainerType.getTypeForField(field);

                ContainerConverter convertor = ContainerConvertersFactory.getConverterForFieldType(containerType);

                List<GmlElement> elementsForContainerConvertor = extractGmlElementsForContainerField(gmlElement, containerType, field, parentElement);

                set(elementObject, field, convertor.convert(field, elementsForContainerConvertor, gmlElement));
            }
            else
            {

                ElementType elementType = ElementType.getTypeForField(field);

                ElementConvertor convertor = ElementConvertersFactory.getConvertorForField(elementType);

                GmlElement elementForFieldConvertor = extractGmlElementForFieldType(gmlElement, elementType, field);

                set(elementObject, field, convertor.convert(field.getType(), elementForFieldConvertor, gmlElement));
            }
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
    protected abstract GmlElement extractGmlElementForFieldType(T gmlElement, ElementType elementType, Field field);

    protected List<GmlElement> extractGmlElementsForContainerField(T gmlElement, ContainerType containerType, Field containerField, P parentElement)
    {
        throw new IllegalStateException(this.getClass().getName() + " should't contain any containers");
    }
}
