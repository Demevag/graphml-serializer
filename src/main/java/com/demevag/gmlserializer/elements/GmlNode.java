/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing graphML node tag
 *
 * @see GmlGraph GmlGraph
 * @see GmlEdge GmlEdge
 * @see GmlData GmlData
 * @see GmlComplexData GmlComplexData
 */
@Data
public class GmlNode implements GmlElement
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
    private List<GmlGraph> subGraphs = new ArrayList<GmlGraph>();
    private List<GmlComplexData> complexDataAttributes = new ArrayList<>();

    public GmlNode(String id)
    {
        this.id = id;
    }

    public void addDataAttribute(GmlData data)
    {
        this.dataAttributes.add(data);
    }

    public void addSubGraph(GmlGraph subGraph)
    {
        this.subGraphs.add(subGraph);
    }

    public void addSubGraph(List<GmlGraph> subGraphs)
    {
        this.subGraphs.addAll(subGraphs);
    }

    public void addDataAttribute(List<GmlData> dataAttributes)
    {
        this.dataAttributes.addAll(dataAttributes);
    }

    public void addComplexDataAttribute(GmlComplexData complexData)
    {
        this.complexDataAttributes.add(complexData);
    }

    public void addComplexDataAttribute(List<GmlComplexData> complexDataAttributes)
    {
        this.complexDataAttributes.addAll(complexDataAttributes);
    }

}
