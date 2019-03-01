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
        }
        return null;
    }
}
