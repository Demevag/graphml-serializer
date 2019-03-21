/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

public enum GmlKeyTarget
{
    NODE("node"),
    EDGE("edge");

    private String name;

    GmlKeyTarget(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
