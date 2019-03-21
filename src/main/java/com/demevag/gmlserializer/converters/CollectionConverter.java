/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.*;

public class CollectionConverter extends ContainerConverter<GmlElement, GmlElement>
{
    @Override
    protected Class getClassOFData(Field containerField)
    {
        return Utils.getCollectionArgClass(containerField);
    }

    @Override
    protected Object createContainerObject(Class containerClass)
    {
        Object containerObject = null;

        try
        {
            containerObject = containerClass.newInstance();

        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            if(List.class.isAssignableFrom(containerClass))
                containerObject = new ArrayList<>();
            else if(Queue.class.isAssignableFrom(containerClass))
                containerObject = new ArrayDeque<>();
            else if(Set.class.isAssignableFrom(containerClass))
                containerObject = new HashSet<>();
            else throw new IllegalStateException("Unknown collection class "+containerClass.getName());
        }

        return containerObject;
    }

    @Override
    protected void addToContainer(Object container, Object data)
    {
        Collection collection = (Collection) container;

        collection.add(data);
    }
}
