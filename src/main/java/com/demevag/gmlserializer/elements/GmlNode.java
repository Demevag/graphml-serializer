package com.demevag.gmlserializer.elements;

import java.util.ArrayList;
import java.util.List;

public class GmlNode
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

    public List<GmlGraph> getSubGraphs()
    {
        return subGraphs;
    }

    public void setSubGraphs(List<GmlGraph> subGraphs)
    {
        this.subGraphs = subGraphs;
    }

    public void addSubGraph(GmlGraph subGraph)
    {
        this.subGraphs.add(subGraph);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GmlNode gmlNode = (GmlNode) o;

        if (id != null ? !id.equals(gmlNode.id) : gmlNode.id != null) return false;
        if (dataAttributes != null ? !dataAttributes.equals(gmlNode.dataAttributes) : gmlNode.dataAttributes != null)
            return false;
        return subGraphs != null ? subGraphs.equals(gmlNode.subGraphs) : gmlNode.subGraphs == null;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataAttributes != null ? dataAttributes.hashCode() : 0);
        result = 31 * result + (subGraphs != null ? subGraphs.hashCode() : 0);
        return result;
    }
}
