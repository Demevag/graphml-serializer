package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.GmlElement;

import java.util.List;

public class ContainerConvertor<T extends GmlElement, P extends GmlElement>
{
    public Object convert(Class elementClass, List<T> elements, P parentElement)
    {
        return null;
    }
}
