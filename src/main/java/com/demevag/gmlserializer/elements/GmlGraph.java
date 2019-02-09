package com.demevag.gmlserializer.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public GmlEdgeType getDefaultEdgeType()
    {
        return defaultEdgeType;
    }

    public void setDefaultEdgeType(GmlEdgeType defaultEdgeType)
    {
        this.defaultEdgeType = defaultEdgeType;
    }

    public List<GmlNode> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<GmlNode> nodes)
    {
        this.nodes = nodes;
    }

    public List<GmlEdge> getEdges()
    {
        return edges;
    }

    public void setEdges(List<GmlEdge> edges)
    {
        this.edges = edges;
    }

    public void addNode(GmlNode node)
    {
        this.nodes.add(node);
    }

    public void addNodes(Collection<GmlNode> newNodes)
    {
        this.nodes.addAll(newNodes);
    }

    public void addEdge(GmlEdge edge)
    {
        this.edges.add(edge);
    }

    public void addEdges(Collection<GmlEdge> newEdges)
    {
        this.edges.addAll(newEdges);
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GmlGraph gmlGraph = (GmlGraph) o;

        if (id != null ? !id.equals(gmlGraph.id) : gmlGraph.id != null) return false;
        if (defaultEdgeType != gmlGraph.defaultEdgeType) return false;
        if (nodes != null ? !nodes.equals(gmlGraph.nodes) : gmlGraph.nodes != null) return false;
        return edges != null ? edges.equals(gmlGraph.edges) : gmlGraph.edges == null;
    }

    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (defaultEdgeType != null ? defaultEdgeType.hashCode() : 0);
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        return result;
    }
}
