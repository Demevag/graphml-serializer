package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.Edge;
import com.demevag.gmlserializer.annotations.Id;
import com.demevag.gmlserializer.annotations.Node;
import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ElementParser
{
    GmlElement parse(Field field) throws IllegalAccessException;
    GmlElement parse(Object object) throws IllegalAccessException;
    List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException;
}
