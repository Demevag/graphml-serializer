package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO:method which return all GmlKey'
@Data
public class GmlGraph implements GmlElement
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

    public void addEdges(List<GmlEdge> newEdges)
    {
        this.edges.addAll(newEdges);
    }

    public void addEdge(GmlEdge edge)
    {
        this.edges.add(edge);
    }

    public void addNodes(List<GmlNode> newNodes)
    {
        this.nodes.addAll(newNodes);
    }

    public void addNode(GmlNode node)
    {
        this.nodes.add(node);
    }
}
