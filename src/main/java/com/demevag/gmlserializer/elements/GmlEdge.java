package com.demevag.gmlserializer.elements;

import java.util.ArrayList;
import java.util.List;

public class GmlEdge
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
    private GmlEdgeType type;

    public GmlEdge(String id, GmlEdgeType type)
    {
        this.id = id;
        this.type = type;
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

    public GmlEdgeType getType()
    {
        return type;
    }

    public void setType(GmlEdgeType type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GmlEdge gmlEdge = (GmlEdge) o;

        if (id != null ? !id.equals(gmlEdge.id) : gmlEdge.id != null) return false;
        if (dataAttributes != null ? !dataAttributes.equals(gmlEdge.dataAttributes) : gmlEdge.dataAttributes != null)
            return false;
        return type == gmlEdge.type;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataAttributes != null ? dataAttributes.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
