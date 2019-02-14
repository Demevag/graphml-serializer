package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.elements.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GmlComplexDataParser implements ElementParser
{
    private GmlKeyTarget target;
    private Object parentObject;

    public GmlComplexDataParser(GmlKeyTarget target, Object parentObject)
    {
        this.parentObject = parentObject;
        this.target = target;
    }

    @Override
    public GmlElement parse(Field field) throws IllegalAccessException
    {
        return parse(Utils.getFieldData(field, parentObject));
    }

    @Override
    public GmlElement parse(Object complexDataObject) throws IllegalAccessException
    {
        Class complexDataClass = complexDataObject.getClass();

        if(Collection.class.isAssignableFrom(complexDataClass))
            return parseCollection((Collection)complexDataObject);

        if(Enum.class.isAssignableFrom(complexDataClass))
            return parseEnum((Enum)complexDataObject);

        GmlComplexData complexData = new GmlComplexData();

        Field[] fields = complexDataClass.getDeclaredFields();

        for(Field field : fields)
        {
            String complexDataClassName = Utils.getClassNameWithoutPackage(complexDataClass);

            String attrName = complexDataClassName + "_"+field.getName();

            GmlKey key = new GmlKey(attrName+"_key", target, attrName);
            key.setAttrType(Utils.getDataType(field.getType()));
            Object data = Utils.getFieldData(field, complexDataObject);

            GmlData dataAttribute = new GmlData(key, data);

            complexData.addDataAttribute(dataAttribute);
        }

        return complexData;
    }

    @Override
    public List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlComplexData> complexData = new ArrayList<>();

        for(Field field : fields)
        {
            if(Utils.isComplexData(field))
                complexData.add((GmlComplexData) parse(field));
        }

        return complexData;
    }

    private GmlComplexData parseCollection(Collection collection) throws IllegalAccessException
    {
        GmlComplexData complexDataOfCollection = new GmlComplexData();

        int i = 0;
        for(Object object : collection)
        {
            GmlComplexData complexData = (GmlComplexData) parse(object);

            for(GmlData data : complexData.getData())
                data.getKey().setId(data.getKey().getId()+"#"+i);

            complexDataOfCollection.addDataAttribute(complexData.getData());
            i++;
        }

        return complexDataOfCollection;
    }

    private GmlComplexData parseEnum(Enum enumData)
    {
        Class enumClass = enumData.getDeclaringClass();

        String attrName = enumClass.getName();

        GmlKey key = new GmlKey(attrName+"_key", target, attrName);
        key.setAttrType(String.class.getName());
        Object data = enumData.name();

        GmlData dataAttribute = new GmlData(key, data);

        GmlComplexData complexData = new GmlComplexData();
        complexData.addDataAttribute(dataAttribute);

        return complexData;
    }
}
