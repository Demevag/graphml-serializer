/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Interface for parser of container (map, collection, etc) with gml elements
 *
 * @author demevag
 */
public interface ContainerParser
{
    /**
     * Accept field and convert it to list of GmlElements
     * @param field
     * @return
     * @throws IllegalAccessException
     */
    List<? extends GmlElement> parse(Field field) throws IllegalAccessException;

    /**
     * Accept array of fields and convert them to list of GmlElement
     * @param fields
     * @return
     * @throws IllegalAccessException
     */
    List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException;
}
