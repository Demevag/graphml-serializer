/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

public class ElementConvertersFactory
{
    public static ElementConverter getConvertorForField(ElementType elementType)
    {
        switch (elementType)
        {
            case DATA: return new GmlDataConverter();
            case COMPLEX_DATA: return new GmlComplexDataConverter();
            case ID: return new GmlDataConverter();
            case NODE: return new GmlNodeConverter();
            case EDGE: return new GmlEdgeConverter();
            case GRAPH: return new GmlGraphConverter();
        }
        return null;
    }
}
