package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GmlNode implements DataHandler
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
    private List<GmlGraph> subGraphs = new ArrayList<GmlGraph>();

    public GmlNode(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<GmlData> getDataAttributes()
    {
        return dataAttributes;
    }

    public void setDataAttributes(List<GmlData> dataAttributes)
    {
        this.dataAttributes = dataAttributes;
    }

    public void addDataAttribute(GmlData data)
    {
        this.dataAttributes.add(data);
    }
}
