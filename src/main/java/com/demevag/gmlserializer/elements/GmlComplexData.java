package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GmlComplexData implements GmlElement
{
    private Map<GmlKey, Object> data;

    public void addDataAttribute(GmlKey key, Object dataObject)
    {
        data.put(key, dataObject);
    }

    public void addDataAttribute(Map<GmlKey, Object> dataMap)
    {
        data.putAll(dataMap);
    }
}
