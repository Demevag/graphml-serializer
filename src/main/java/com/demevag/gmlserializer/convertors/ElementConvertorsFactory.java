package com.demevag.gmlserializer.convertors;

public class ElementConvertorsFactory
{
    public static ElementConvertor getConvertorForField(ElementType elementType)
    {
        switch (elementType)
        {
            case DATA: return new GmlDataConvertor();
            case COMPLEX_DATA: return new GmlComplexDataConvertor();
            case ID: return new GmlDataConvertor();
        }
        return null;
    }
}
