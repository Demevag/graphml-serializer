package com.demevag.gmlserializer.elements;

import java.util.ArrayList;
import java.util.List;

public class GmlEdge implements DataHandler
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

    public String getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }

    public String getSourceId()
    {
        return sourceId;
    }

    public void setSourceId(String sourceId)
    {
        this.sourceId = sourceId;
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        GmlEdge gmlEdge = (GmlEdge) object;

        if (id != null ? !id.equals(gmlEdge.id) : gmlEdge.id != null) return false;
        if (dataAttributes != null ? !dataAttributes.equals(gmlEdge.dataAttributes) : gmlEdge.dataAttributes != null)
            return false;
        if (type != gmlEdge.type) return false;
        if (targetId != null ? !targetId.equals(gmlEdge.targetId) : gmlEdge.targetId != null) return false;
        return sourceId != null ? sourceId.equals(gmlEdge.sourceId) : gmlEdge.sourceId == null;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dataAttributes != null ? dataAttributes.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (targetId != null ? targetId.hashCode() : 0);
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        return result;
    }
}
