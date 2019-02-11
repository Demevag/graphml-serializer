package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GmlNode implements GmlElement
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
    private List<GmlGraph> subGraphs = new ArrayList<GmlGraph>();

    public GmlNode(String id)
    {
        this.id = id;
    }

    public void addDataAttribute(GmlData data)
    {
        this.dataAttributes.add(data);
    }

    public void addSubGraph(GmlGraph subGraph)
    {
        this.subGraphs.add(subGraph);
    }

    public void addSubGraph(List<GmlGraph> subGraphs)
    {
        this.subGraphs.addAll(subGraphs);
    }

    public void addDataAttribute(List<GmlData> dataAttributes)
    {
        this.dataAttributes.addAll(dataAttributes);
    }
}
