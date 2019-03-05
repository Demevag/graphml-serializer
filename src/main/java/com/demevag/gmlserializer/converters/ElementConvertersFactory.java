package com.demevag.gmlserializer.converters;

public class ElementConvertersFactory
{
    public static ElementConvertor getConvertorForField(ElementType elementType)
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
