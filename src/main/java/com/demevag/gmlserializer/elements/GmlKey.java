package com.demevag.gmlserializer.elements;

public class GmlKey<AttrType>
{
    private String id;
    private GmlKeyTarget target;
    private String atrrName;

    private AttrType defaultVal;

    public GmlKey(String id, GmlKeyTarget target, String atrrName)
    {
        this.id = id;
        this.target = target;
        this.atrrName = atrrName;
    }

    public GmlKey(String id, GmlKeyTarget target, String atrrName, AttrType defaultVal)
    {
        this.id = id;
        this.target = target;
        this.atrrName = atrrName;
        this.defaultVal = defaultVal;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public GmlKeyTarget getTarget()
    {
        return target;
    }

    public void setTarget(GmlKeyTarget target)
    {
        this.target = target;
    }

    public String getAtrrName()
    {
        return atrrName;
    }

    public void setAtrrName(String atrrName)
    {
        this.atrrName = atrrName;
    }

    public AttrType getDefaultVal()
    {
        return defaultVal;
    }

    public void setDefaultVal(AttrType defaultVal)
    {
        this.defaultVal = defaultVal;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GmlKey<?> gmlKey = (GmlKey<?>) o;

        if (id != null ? !id.equals(gmlKey.id) : gmlKey.id != null) return false;
        if (target != gmlKey.target) return false;
        if (atrrName != null ? !atrrName.equals(gmlKey.atrrName) : gmlKey.atrrName != null) return false;
        return defaultVal != null ? defaultVal.equals(gmlKey.defaultVal) : gmlKey.defaultVal == null;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (atrrName != null ? atrrName.hashCode() : 0);
        result = 31 * result + (defaultVal != null ? defaultVal.hashCode() : 0);
        return result;
    }
}
