package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class GmlComplexData implements GmlElement
{
    private List<GmlData> data = new ArrayList<>();

    public void addDataAttribute(GmlData data)
    {
        this.data.add(data);
    }

    public void addDataAttribute(List<GmlData> dataAttributes)
    {
        this.data.addAll(dataAttributes);
    }
}
