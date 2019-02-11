package com.demevag.gmlserializer.elements;

import lombok.Data;

@Data
public class GmlKey
{
    private String id;
    private GmlKeyTarget target;
    private String atrrName;
    private String attrType;

    private Object defaultVal;

    public GmlKey(String id, GmlKeyTarget target, String atrrName)
    {
        this.id = id;
        this.target = target;
        this.atrrName = atrrName;
    }

    public GmlKey(String id, GmlKeyTarget target, String atrrName, Object defaultVal)
    {
        this.id = id;
        this.target = target;
        this.atrrName = atrrName;
        this.defaultVal = defaultVal;
    }
}
