package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GmlEdge implements GmlElement
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
    private List<GmlComplexData> complexDataAttributes = new ArrayList<>();
    private GmlEdgeType type;
    private String targetId;
    private String sourceId;

    public GmlEdge(String id, GmlEdgeType type)
    {
        this.id = id;
        this.type = type;
    }

    public void addDataAttribute(GmlData data)
    {
        this.dataAttributes.add(data);
    }

    public void addDataAttributes(List<GmlData> dataAttributes)
    {
        this.dataAttributes.addAll(dataAttributes);
    }

    public void addComplexDataAttribute(GmlComplexData complexData)
    {
        this.complexDataAttributes.add(complexData);
    }

    public void addComplexDataAttribute(List<GmlComplexData> complexDataAttributes)
    {
        this.complexDataAttributes.addAll(complexDataAttributes);
    }
}
