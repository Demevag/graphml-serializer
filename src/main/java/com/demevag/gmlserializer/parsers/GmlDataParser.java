package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.Ignore;
import com.demevag.gmlserializer.elements.GmlData;
import com.demevag.gmlserializer.elements.GmlElement;
import com.demevag.gmlserializer.elements.GmlKey;
import com.demevag.gmlserializer.elements.GmlKeyTarget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlDataParser extends ElementParser
{
    private GmlKeyTarget target;
    private Object object;

    public GmlDataParser(GmlKeyTarget target, Object object)
    {
        this.target = target;
        this.object = object;
    }

    @Override
    public GmlElement parse(Field field) throws IllegalAccessException
    {
        GmlKey dataKey = new GmlKey(field.getName() + "_key", target, field.getName());

        Object data = getFieldData(field, object);

        dataKey.setAttrType(getDataType(data));

        return new GmlData(dataKey, data);
    }

    @Override
    public List<GmlData> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlData> dataAttributes = new ArrayList<GmlData>();

        for(Field field : fields)
        {
            if(field.isAnnotationPresent(Ignore.class))
                continue;
            if(isIdField(field))
                continue;
            if (isPrimitiveOrString(field))
                dataAttributes.add((GmlData)parse(field));
        }

        return dataAttributes;
    }
}
