/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.Utils;
import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.annotations.Ignore;
import com.demevag.gmlserializer.elements.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of parser for edge
 *
 * @see com.demevag.gmlserializer.parsers.ElementParser ElementParser
 * @see com.demevag.gmlserializer.elements.GmlEdge GmlEdge
 * @author demevag
 */
public class GmlEdgeParser implements ElementParser
{
    private Object parentObject;
    private GmlEdgeType edgeType;

    public GmlEdgeParser(Object object, GmlEdgeType edgeType)
    {
        this.parentObject = object;
        this.edgeType = edgeType;
    }

    public GmlElement parse(Field field) throws IllegalAccessException
    {

        return parse(Utils.getFieldData(field, parentObject));
    }

    @Override
    public GmlElement parse(Object edgeObject) throws IllegalAccessException
    {
        Class edgeClass = edgeObject.getClass();

        String edgeClassName = Utils.getClassNameWithoutPackage(edgeClass);

        GmlEdge edge = new GmlEdge(edgeClassName, edgeType);

        Field[] edgeFields = edgeClass.getDeclaredFields();

        ElementParser dataParser = new GmlDataParser(GmlKeyTarget.EDGE, edgeObject);
        ElementParser complexDataParser = new GmlComplexDataParser(GmlKeyTarget.EDGE, edgeObject);
        edge.addDataAttributes((List<GmlData>) dataParser.parse(edgeFields));
        edge.addComplexDataAttribute((List<GmlComplexData>) complexDataParser.parse(edgeFields));


        boolean hasId = false;

        for (Field edgeField : edgeFields)
        {
            if (edgeField.isAnnotationPresent(Ignore.class))
                continue;

            if (Utils.isIdField(edgeField))
            {
                edge.setId(edge.getId() + "_" + Utils.getFieldData(edgeField, edgeObject));
                hasId = true;

                continue;
            }
            if (edgeField.isAnnotationPresent(EdgeSource.class))
            {
                if (String.class.isAssignableFrom(edgeField.getType()))
                {
                    EdgeSource edgeSourceAnnotaition = edgeField.getAnnotation(EdgeSource.class);

                    String sourceNodeClassName = edgeSourceAnnotaition.nodeClassName();

                    if(sourceNodeClassName.equals(""))
                        throw new IllegalStateException("Can't resolve node class name for "+edgeField.getName());

                    edge.setSourceId(sourceNodeClassName+"_"+(String) Utils.getFieldData(edgeField, edgeObject));
                }
                else
                {
                    edge.setSourceId(Utils.getClassNameWithoutPackage(edgeField.getType()) + "_" + Utils.getId(edgeField, edgeObject));
                }
            } else if (edgeField.isAnnotationPresent(EdgeTarget.class))
            {
                if (String.class.isAssignableFrom(edgeField.getType()))
                {
                    EdgeTarget edgeTargetAnnotation = edgeField.getAnnotation(EdgeTarget.class);

                    String targetNodeClassName = edgeTargetAnnotation.nodeClassName();

                    if(targetNodeClassName.equals(""))
                        throw new IllegalStateException("Can't resolve node class name for "+edgeField.getName());

                    edge.setTargetId(targetNodeClassName+"_"+(String) Utils.getFieldData(edgeField, edgeObject));
                }
                else
                    edge.setTargetId(Utils.getClassNameWithoutPackage(edgeField.getType()) + "_" + Utils.getId(edgeField, edgeObject));
            }
        }
        ;


        if (!hasId)
            throw new IllegalStateException(edgeClass.getName() + " has no id field");

        return edge;
    }

    public List<GmlEdge> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlEdge> edges = new ArrayList<GmlEdge>();

        for (Field field : fields)
        {
            if (Utils.isEdge(field))
                edges.add((GmlEdge) parse(field));
        }

        return edges;
    }
}
