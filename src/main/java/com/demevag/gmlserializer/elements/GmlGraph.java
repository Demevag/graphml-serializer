package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO:method which return all GmlKey'
@Data
public class GmlGraph
{
    private String id;

    private GmlEdgeType defaultEdgeType = GmlEdgeType.UNDIRECTED;
    private List<GmlNode> nodes = new ArrayList<GmlNode>();
    private List<GmlEdge> edges = new ArrayList<GmlEdge>();

    public GmlGraph(String id)
    {
        this.id = id;
    }

    public GmlGraph(String id, GmlEdgeType defaultEdgeType)
    {
        this.id = id;
        this.defaultEdgeType = defaultEdgeType;
    }
}
