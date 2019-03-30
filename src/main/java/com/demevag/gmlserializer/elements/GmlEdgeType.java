/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

/**
 * Enum of the edge type
 *
 * @see GmlEdge GmlEdge
 * @author demevag
 */
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
