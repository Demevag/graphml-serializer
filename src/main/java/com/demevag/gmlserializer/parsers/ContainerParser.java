/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.List;

public interface ContainerParser
{
    List<? extends GmlElement> parse(Field field) throws IllegalAccessException;
    List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException;
}
