/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

import lombok.Data;

/**
 * Class for representing graphML key tag
 *
 * @see GmlKeyTarget GmlKeyTarget
 * @author demevag
 */
@Data
public class GmlKey implements GmlElement
{
    private String id;
    private GmlKeyTarget target;
    private String atrrName;
    private String attrType;

    private Object defaultVal;

    public GmlKey(String id, GmlKeyTarget target, String atrrName)
    {
        this.id = id;
        this.target = target;
        this.atrrName = atrrName;
    }

    public GmlKey(String id, GmlKeyTarget target, String atrrName, Object defaultVal)
    {
        this.id = id;
        this.target = target;
        this.atrrName = atrrName;
        this.defaultVal = defaultVal;
    }
}
