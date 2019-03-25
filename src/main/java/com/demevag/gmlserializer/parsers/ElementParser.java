/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

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

/**
 * Interface for parser of single gml element (such as data, complex data, node, edge, graph, etc)
 *
 * @author demevag
 */
public interface ElementParser
{
    /**
     * Accept field and convert it to GmlElement
     * @param field
     * @return
     * @throws IllegalAccessException
     */
    GmlElement parse(Field field) throws IllegalAccessException;

    /**
     * Accept object and convert it to GmlElement
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    GmlElement parse(Object object) throws IllegalAccessException;

    /**
     * Accept array of fields and convert them to list of GmlElement
     * @param fields
     * @return
     * @throws IllegalAccessException
     */
    List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException;
}
