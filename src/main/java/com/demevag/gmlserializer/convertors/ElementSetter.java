package com.demevag.gmlserializer.convertors;

import java.lang.reflect.Field;

public interface ElementSetter
{
    void set(Object parentObject, Field field, Object data);
}
