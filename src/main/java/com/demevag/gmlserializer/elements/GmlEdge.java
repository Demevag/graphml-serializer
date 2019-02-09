package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GmlEdge
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
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


}
