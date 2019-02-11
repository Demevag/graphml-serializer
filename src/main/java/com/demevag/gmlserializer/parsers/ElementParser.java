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

public abstract class ElementParser
{
    public abstract GmlElement parse(Field field) throws IllegalAccessException;
    public  abstract List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException;

    protected String getDataType(Object data)
    {
        if(data instanceof Integer)
            return "int";

        return data.getClass().getName().toLowerCase();
    }


    protected boolean isPrimitiveOrString(Field field)
    {
        return field.getType().isPrimitive() || field.getType() == String.class;
    }

    protected boolean isIdField(Field field)
    {
        return isPrimitiveOrString(field) && field.isAnnotationPresent(Id.class);
    }

    protected boolean isNode(Field field)
    {
        return field.isAnnotationPresent(Node.class);
    }

    protected boolean isEdge(Field field)
    {
        return field.isAnnotationPresent(Edge.class);
    }

    protected boolean isCollection(Field field)
    {
        return Collection.class.isAssignableFrom(field.getType());
    }

    protected boolean isMap(Field field)
    {
        return Map.class.isAssignableFrom(field.getType());
    }

    protected boolean isCollectionOfEdges(Field field)
    {
        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Edge.class);
    }

    protected boolean isCollectionOfNodes(Field field)
    {
        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Node.class);
    }

    protected Class getCollectionArgClass(Field field)
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

    protected Object getFieldData(Field field, Object object) throws IllegalAccessException
    {
        field.setAccessible(true);

        Object data = field.get(object);

        field.setAccessible(false);

        return data;
    }

    protected String getId(Field field, Object object) throws IllegalAccessException
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

    protected String getId(Object object) throws IllegalAccessException
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
}
