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
    public static String getDataType(Object data)
    {
        return data.getClass().getName();
    }


    public static boolean isPrimitiveOrString(Field field)
    {
        return field.getType().isPrimitive() || field.getType() == String.class;
    }

    public static boolean isIdField(Field field)
    {
        return isPrimitiveOrString(field) && field.isAnnotationPresent(Id.class);
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

    public static boolean isMap(Field field)
    {
        return Map.class.isAssignableFrom(field.getType());
    }

    public static boolean isCollectionOfEdges(Field field)
    {
        if(!isCollection(field))
            return false;

        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Edge.class);
    }

    public static boolean isCollectionOfNodes(Field field)
    {
        if(!isCollection(field))
            return false;

        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Node.class);
    }

    public static boolean isMapOfNodes(Field field)
    {
        if(!isMap(field))
            return false;

        Class mapValClass = getMapValueClass(field);

        return mapValClass.isAnnotationPresent(Node.class);
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

            if(fieldArgTypes[1] instanceof TypeVariableImpl)
            {
                TypeVariableImpl typeVariable = (TypeVariableImpl) fieldArgTypes[1];
                Type[] types = typeVariable.getBounds();
                return (Class)types[0];
            }

            return (Class) fieldArgTypes[1];
        }

        return null;
    }

    public static Object getFieldData(Field field, Object object) throws IllegalAccessException
    {
        field.setAccessible(true);

        Object data = field.get(object);

        field.setAccessible(false);

        return data;
    }

    public static String getId(Field field, Object object) throws IllegalAccessException
    {
        Class fieldClass = field.getType();

        Field idOfField = null;

        Field[] fieldsOfFieldClass = fieldClass.getDeclaredFields();

        for (Field f : fieldsOfFieldClass)
        {
            if (f.isAnnotationPresent(Id.class) && idOfField == null)
                idOfField = f;
            else if (f.isAnnotationPresent(Id.class) && idOfField != null)
                throw new IllegalStateException(fieldClass.getName() + " contains more than one id field");
        }

        if (idOfField == null)
            throw new IllegalStateException(fieldClass.getName() + " doesn't contain id field");

        String id = ( getFieldData(idOfField, getFieldData(field, object) ) ).toString();


        return id;
    }

    public static String getId(Object object) throws IllegalAccessException
    {
        Class objectClass = object.getClass();

        Field[] fields = objectClass.getDeclaredFields();

        Field idField = null;

        for(Field field : fields)
        {
            if (field.isAnnotationPresent(Id.class) && idField == null)
                idField = field;
            else if (field.isAnnotationPresent(Id.class) && idField != null)
                throw new IllegalStateException(objectClass.getName() + " contains more than one id field");
        }

        return (String)getFieldData(idField, object);
    }

    public static boolean isSubGraph(Field field)
    {
        return field.isAnnotationPresent(SubGraph.class);
    }

    public static boolean isGraph(Field field)
    {
        return field.isAnnotationPresent(Graph.class);
    }
}
