package com.demevag.gmlserializer.elements;

public enum GmlEdgeType
{
    DIRECTED("directed"),
    UNDIRECTED("undirected");

    private String name;

    GmlEdgeType(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
