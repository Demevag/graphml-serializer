/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.Ignore;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlKey;
import com.demevag.gmlserializer.elements.GmlKeyTarget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlDataParser implements ElementParser
{
    private GmlKeyTarget target;
    private Object parentObject;

    public GmlDataParser(GmlKeyTarget target, Object object)
    {
        this.target = target;
        this.parentObject = object;
    }

    @Override
    public GmlElement parse(Field field) throws IllegalAccessException
    {
        if(!Utils.isDataField(field))
            throw new IllegalArgumentException(field.getName()+" isn't primitive or string");

        GmlKey dataKey = new GmlKey(field.getName() + "_key", target, field.getName());

        Object data = Utils.getFieldData(field, parentObject);

        dataKey.setAttrType(Utils.getDataType(field.getType()));

        return new GmlData(dataKey, data);
    }

    @Override
    public GmlElement parse(Object object)
    {
        return null;
    }

    @Override
    public List<GmlData> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlData> dataAttributes = new ArrayList<GmlData>();

        for(Field field : fields)
        {
            if (Utils.isDataField(field))
                dataAttributes.add((GmlData)parse(field));
        }

        return dataAttributes;
    }

}
