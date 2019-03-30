/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for object data fields. Each field of this object will be serialized as gml data
 *
 * @see GmlData GmlData
 * @author demevag
 */

@Data
public class GmlComplexData implements GmlElement
{
    private List<GmlData> data = new ArrayList<>();

    public void addDataAttribute(GmlData data)
    {
        this.data.add(data);
    }

    public void addDataAttribute(List<GmlData> dataAttributes)
    {
        this.data.addAll(dataAttributes);
    }
}
