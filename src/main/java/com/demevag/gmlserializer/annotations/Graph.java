/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.annotations;

import com.demevag.gmlserializer.elements.GmlEdgeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Graph
{
    String id() default "g";
    GmlEdgeType edgedefault() default GmlEdgeType.UNDIRECTED;
}
