package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MapConverter extends ContainerConverter
{
    @Override
    protected Class getClassOFData(Field containerField)
    {
        return Utils.getMapValueClass(containerField);
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
            if(Map.class.isAssignableFrom(containerClass))
                containerObject = new HashMap<>();
            else throw new IllegalStateException("Unknown map class "+containerClass.getName());
        }

        return containerObject;
    }

    @Override
    protected void addToContainer(Object container, Object data)
    {
        Map map = (Map) container;
        String id = null;
        try
        {
            id = Utils.getId(data);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        map.put(id, data);
    }
}
