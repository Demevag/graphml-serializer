/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.annotations.*;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class Utils
{
    public static boolean isPrimitiveOrString(Field field)
    {
        return field.getType().isPrimitive() || field.getType() == String.class;
    }

    public static boolean isIdField(Field field)
    {
        return isPrimitiveOrString(field) && field.isAnnotationPresent(Id.class);
    }

    public static boolean isDataField(Field field)
    {

        return !field.isAnnotationPresent(Ignore.class)
                && !isIdField(field)
                && isPrimitiveOrString(field);
    }

    public static boolean isNode(Field field)
    {
        return field.getType().isAnnotationPresent(Node.class);
    }

    public static boolean isEdge(Field field)
    {
        return field.getType().isAnnotationPresent(Edge.class);
    }

    public static boolean isCollection(Field field)
    {
        return Collection.class.isAssignableFrom(field.getType());
    }

    public static boolean isComplexData(Field field)
    {
        return field.isAnnotationPresent(ComplexData.class);
    }

    public static boolean isSubGraph(Field field)
    {
        return field.isAnnotationPresent(SubGraph.class);
    }

    public static boolean isGraph(Field field)
    {
        return field.isAnnotationPresent(Graph.class);
    }

    public static boolean isMap(Field field)
    {
        return Map.class.isAssignableFrom(field.getType());
    }

    public static boolean isCollectionOfEdges(Field field)
    {
        if (!isCollection(field))
            return false;

        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Edge.class);
    }

    public static boolean isCollectionOfNodes(Field field)
    {
        if (!isCollection(field))
            return false;

        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Node.class);
    }

    public static boolean isMapOfNodes(Field field)
    {
        if (!isMap(field))
            return false;

        Class mapValClass = getMapValueClass(field);

        return mapValClass.isAnnotationPresent(Node.class);
    }


    public static String getDataType(Class dataClass)
    {
        String dataTypeName = "string";

        if (dataClass.isPrimitive())
        {
            dataTypeName = dataClass.getName();

            if (dataClass.getPackage() != null)
                dataTypeName = dataTypeName
                        .replace(dataClass.getPackage().getName() + ".", "")
                        .toLowerCase();
        }

        return dataTypeName;
    }

    public static Object getFieldData(Field field, Object object) throws IllegalAccessException
    {
        field.setAccessible(true);

        Object data = field.get(object);

        field.setAccessible(false);

        return data;
    }

    public static String getId(Field field, Object parentObject) throws IllegalAccessException
    {
        return getId(getFieldData(field, parentObject));
    }

    public static String getId(Object object) throws IllegalAccessException
    {
        Class objectClass = object.getClass();

        Field[] fields = objectClass.getDeclaredFields();

        Field idField = null;

        for (Field field : fields)
        {
            if (field.isAnnotationPresent(Id.class) && idField == null)
                idField = field;
            else if (field.isAnnotationPresent(Id.class) && idField != null)
                throw new IllegalStateException(objectClass.getName() + " contains more than one id field");
        }

        String id = (String) getFieldData(idField, object);

        id.replace(objectClass.getPackage().getName(), "");
        return id;
    }

    public static String getClassNameWithoutPackage(Class someClass)
    {
        if(someClass.getPackage() == null)
            return someClass.getName();

        return someClass.getName().replace(someClass.getPackage().getName()+".","");
    }

    public static Class getCollectionArgClass(Field field)
    {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType)
        {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();

            if (fieldArgTypes.length > 1)
                throw new IllegalArgumentException("Collection must have only one generic type");

            return (Class) fieldArgTypes[0];
        }

        return null;
    }

    public static Class getMapValueClass(Field field)
    {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType)
        {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();

            if (fieldArgTypes.length > 2 || fieldArgTypes.length < 2)
                throw new IllegalArgumentException("Map must have two generic types");

            if (fieldArgTypes[1] instanceof TypeVariableImpl)
            {
                TypeVariableImpl typeVariable = (TypeVariableImpl) fieldArgTypes[1];
                Type[] types = typeVariable.getBounds();
                return (Class) types[0];
            }

            return (Class) fieldArgTypes[1];
        }

        return null;
    }


}
