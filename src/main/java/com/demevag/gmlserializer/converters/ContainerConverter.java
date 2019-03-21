/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.List;

public abstract class ContainerConverter<T extends GmlElement, P extends GmlElement>
{
    public Object convert(Field containerField, List<T> elements, P parentElement) throws IllegalAccessException, InstantiationException
    {
        Class containerClass = containerField.getType();
        Object container = createContainerObject(containerClass);

        ElementType dataType = getTypeOfData(ContainerType.getTypeForField(containerField));
        Class dataClass = getClassOFData(containerField);

        for(T element : elements)
        {
            Object data = extractDataFromGmlElement(dataClass, dataType, element, parentElement);
            addToContainer(container, data);
        }

        return container;
    }

    private ElementType getTypeOfData(ContainerType fieldType)
    {
        switch (fieldType)
        {
            case EDGE_COLLECTION:{
                return ElementType.EDGE;
            }

            case NODE_COLLECTION:
            case NODE_MAP:
                return ElementType.NODE;
            case COMPLEX_DATA_COLLECTION:{
                return ElementType.COMPLEX_DATA;
            }
        }

        throw new IllegalStateException("Container field can't be "+fieldType.name());
    }

    protected abstract Class getClassOFData(Field containerField);

    protected Object extractDataFromGmlElement(Class dataClass, ElementType dataType, T element, P parentElement) throws InstantiationException, IllegalAccessException
    {
        ElementConverter convertor = ElementConvertersFactory.getConvertorForField(dataType);

        return convertor.convert(dataClass, element, parentElement);
    }
    protected abstract Object createContainerObject(Class containerClass);
    protected abstract void addToContainer(Object container, Object data);
}
