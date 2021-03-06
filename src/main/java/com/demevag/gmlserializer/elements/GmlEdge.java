/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for representing graphML edge tag
 *
 * @see GmlData GmlData
 * @see GmlComplexData GmlComplexData
 * @see GmlEdgeType GmlEdgeType
 * @author demevag
 */
@Data
public class GmlEdge implements GmlElement
{
    private String id;
    private List<GmlData> dataAttributes = new ArrayList<GmlData>();
    private List<GmlComplexData> complexDataAttributes = new ArrayList<>();
    private GmlEdgeType type;
    private String targetId;
    private String sourceId;

    public GmlEdge(String id, GmlEdgeType type)
    {
        this.id = id;
        this.type = type;
    }

    public void addDataAttribute(GmlData data)
    {
        this.dataAttributes.add(data);
    }

    public void addDataAttributes(List<GmlData> dataAttributes)
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
