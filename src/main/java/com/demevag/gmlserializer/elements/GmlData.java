/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.elements;

import lombok.Data;

@Data
public class GmlData implements GmlElement
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
