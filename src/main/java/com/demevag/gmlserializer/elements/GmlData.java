package com.demevag.gmlserializer.elements;

public class GmlData<Type>
{
    private GmlKey<Type> key;
    private Type data;

    public GmlData(GmlKey<Type> key)
    {
        this.key = key;
    }

    public GmlData(GmlKey<Type> key, Type data)
    {
        this.key = key;
        this.data = data;
    }

    public GmlKey<Type> getKey()
    {
        return key;
    }

    public void setKey(GmlKey<Type> key)
    {
        this.key = key;
    }

    public Type getData()
    {
        return data;
    }

    public void setData(Type data)
    {
        this.data = data;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GmlData<?> gmlData = (GmlData<?>) o;

        if (key != null ? !key.equals(gmlData.key) : gmlData.key != null) return false;
        return data != null ? data.equals(gmlData.data) : gmlData.data == null;
    }

    @Override
    public int hashCode()
    {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
