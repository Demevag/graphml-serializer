package com.demevag.gmlserializer.convertors;

public class ElementConvertorsFactory
{
    public static ElementConvertor getConvertorForField(FieldType fieldType)
    {
        switch (fieldType)
        {
            case DATA: return new GmlDataConvertor();
            case COMPLEX_DATA: return new GmlComplexDataConvertor();
        }
        return null;
    }
}
