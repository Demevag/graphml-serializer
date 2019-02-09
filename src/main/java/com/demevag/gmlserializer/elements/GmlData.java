package com.demevag.gmlserializer.elements;

import lombok.Data;

@Data
public class GmlData
{
    private GmlKey key;
    private Object data;

    public GmlData(GmlKey key)
    {
        this.key = key;
    }

    public GmlData(GmlKey key, Object data)
    {
        this.key = key;
        this.data = data;
    }


}
