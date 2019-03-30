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

/**
 * Annotation for marking subgraph fields
 *
 * @author demevag
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SubGraph
{
    /**
     * Set graph id
     * @return
     */
    String id() default "g";
     /**
     * Set default edge type of the graph.
     * Default - undirected
     * @return
     */
    GmlEdgeType edgedefault() default GmlEdgeType.UNDIRECTED;
}
