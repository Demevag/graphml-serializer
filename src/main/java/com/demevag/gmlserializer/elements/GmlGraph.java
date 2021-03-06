/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class for representing graphML graph tag
 *
 * @see GmlEdge GmlEdge
 * @see GmlNode GmlNode
 * @see GmlEdgeType GmlEdgeType
 * @author demevag
 */
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
