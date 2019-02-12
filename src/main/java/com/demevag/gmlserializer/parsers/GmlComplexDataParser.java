package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.ComplexData;
import com.demevag.gmlserializer.elements.GmlComplexData;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlKey;
import com.demevag.gmlserializer.elements.GmlKeyTarget;

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

        GmlComplexData complexData = new GmlComplexData();

        Field[] fields = complexDataClass.getDeclaredFields();

        for(Field field : fields)
        {
            String attrName = complexDataClass.getName() + "_"+field.getName();

            GmlKey key = new GmlKey(attrName+"_key", target, attrName);
            Object data = Utils.getFieldData(field, complexDataObject);

            complexData.addDataAttribute(key, data);
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

            for(GmlKey key : complexData.getData().keySet())
                key.setId(key.getId()+"#"+i);

            complexDataOfCollection.addDataAttribute(complexData.getData());
            i++;
        }

        return complexDataOfCollection;
    }
}
