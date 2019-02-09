package com.demevag.gmlserializer;

import com.demevag.gmlserializer.annotations.*;
import com.demevag.gmlserializer.elements.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//TODO: Список ребёр, может находится в узле, из которого они выходят
//TODO: Обрабатывать enum как data
//TODO: Every node and edge should contain id
public class Parser<T>
{
    public GmlGraph parse(T object) throws IllegalAccessException
    {
        Class rootClass = object.getClass();

        if (!rootClass.isAnnotationPresent(Graph.class))
            throw new IllegalArgumentException("Root object should have Graph annotation");

        Graph graphAnnotation = (Graph) rootClass.getAnnotation(Graph.class);

        String graphId = graphAnnotation.id();
        GmlEdgeType defaultEdgeType = graphAnnotation.edgedefault();

        GmlGraph graph = parseGraph(object, graphId, defaultEdgeType);

        return graph;
    }

    private GmlGraph parseGraph(Object object, String graphId, GmlEdgeType defaultEdgeType) throws IllegalAccessException
    {
        GmlGraph graph = new GmlGraph(graphId, defaultEdgeType);

        Field[] fields = object.getClass().getDeclaredFields();

        for(Field field : fields)
        {
           if(isCollectionOfEdges(field))
           {
               graph.addEdges(parseCollectionOfEdges(field, object, defaultEdgeType));
               continue;
           }
           if(isCollectionOfNodes(field))
           {
               graph.addNodes(parseCollectionOfNodes(field, object));
               continue;
           }

           throw new IllegalArgumentException("Graph "+object.getClass()+" must have only nodes and edges");
        }

        return graph;
    }

    //TODO: реализовать
    private GmlGraph parseSubGraph(Field field, String graphId, GmlEdgeType defaultEdgeType)
    {
        return null;
    }

    private GmlNode parseNode(Object object) throws IllegalAccessException
    {
        Class nodeClass = object.getClass();

        GmlNode node = new GmlNode(nodeClass.getName());

        Field[] nodeFields = nodeClass.getDeclaredFields();

        for (Field field : nodeFields)
        {
            if (isPrimitiveOrString(field))
            {
                addDataAttribute(field, object, node, GmlKeyTarget.NODE);
            } else if (field.isAnnotationPresent(SubGraph.class))
            {
                SubGraph subGraphAnnotation = field.getAnnotation(SubGraph.class);

                node.addSubGraph(parseSubGraph(field, subGraphAnnotation.id(), subGraphAnnotation.edgedefault()));
            } else
                throw new IllegalArgumentException(field.getName() + " is non-primitive and without SubGraph annotation");

        }

        return node;
    }

    private GmlEdge parseEdge(Object object, GmlEdgeType edgeType) throws IllegalAccessException
    {
        Class edgeClass = object.getClass();

        GmlEdge edge = new GmlEdge(edgeClass.getName(), edgeType);

        Field[] nodeFields = edgeClass.getDeclaredFields();

        for (Field field : nodeFields)
        {
            if (isPrimitiveOrString(field))
            {
                addDataAttribute(field, object, edge, GmlKeyTarget.EDGE);
            }
            else if(field.isAnnotationPresent(EdgeSource.class))
                edge.setSourceId(getId(field, object));
            else if(field.isAnnotationPresent(EdgeTarget.class))
                edge.setTargetId(getId(field, object));
            else
                throw new IllegalArgumentException(field.getName() + " is non-primitive");

        }

        return edge;
    }

    private List<GmlEdge> parseCollectionOfEdges(Field field, Object object, GmlEdgeType defaultEdgeType) throws IllegalAccessException
    {
        field.setAccessible(true);
        Collection collection = (Collection)field.get(object);
        field.setAccessible(false);

        List<GmlEdge> edges = new ArrayList<GmlEdge>();

        for(Object o : collection)
        {
            edges.add(parseEdge(o, defaultEdgeType));
        }

        return edges;
    }

    private List<GmlNode> parseCollectionOfNodes(Field field, Object object) throws IllegalAccessException
    {
        field.setAccessible(true);
        Collection collection = (Collection)field.get(object);
        field.setAccessible(false);

        List<GmlNode> nodes = new ArrayList<GmlNode>();

        for(Object o : collection)
        {
            nodes.add(parseNode(o));
        }

        return nodes;
    }

    //TODO: реализовать
    private List<GmlEdge> parseMapOfEdges(Field field, Object object, GmlEdgeType defaultEdgeType)
    {
        return null;
    }

    //TODO: реализовать
    private List<GmlNode> parseMapOfNodes(Field field, Object object)
    {
        return null;
    }

    private void addDataAttribute(Field dataField, Object object, DataHandler dataHandler, GmlKeyTarget target) throws IllegalAccessException
    {

        if (dataField.isAnnotationPresent(Id.class))
        {
            dataField.setAccessible(true);

            dataHandler.setId(dataHandler.getId() + "_" + dataField.get(object));

            dataField.setAccessible(false);

            return;
        }

        dataHandler.addDataAttribute(parseData(dataField, target, object));

    }

    private GmlData parseData(Field field, GmlKeyTarget target, Object object) throws IllegalAccessException
    {
        Class dataType = field.getType();

        GmlKey dataKey = new GmlKey(field.getName()+"_key", target, field.getName());

        field.setAccessible(true);
        Object data = field.get(object);
        field.setAccessible(false);

        return new GmlData(dataKey, data);
    }

    private boolean isPrimitiveOrString(Field field)
    {
        return field.getType().isPrimitive() || field.getType() == String.class;
    }

    private boolean isNode(Field field)
    {
        return field.isAnnotationPresent(Node.class);
    }

    private boolean isEdge(Field field)
    {
        return field.isAnnotationPresent(Edge.class);
    }

    private boolean isCollection(Field field)
    {
        return Collection.class.isAssignableFrom(field.getType());
    }

    private boolean isMap(Field field)
    {
        return Map.class.isAssignableFrom(field.getType());
    }

    private boolean isCollectionOfEdges(Field field)
    {
        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Edge.class);
    }

    private boolean isCollectionOfNodes(Field field)
    {
        Class collectionArgClass = getCollectionArgClass(field);

        return collectionArgClass.isAnnotationPresent(Node.class);
    }

    private Class getCollectionArgClass(Field field)
    {
        Type genericFieldType = field.getGenericType();

        if (genericFieldType instanceof ParameterizedType)
        {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();

            if(fieldArgTypes.length > 1)
                throw new IllegalArgumentException("Collection must have only one generic type");

            return (Class)fieldArgTypes[0];
        }

        return null;
    }

    private String getId(Field field, Object object) throws IllegalAccessException
    {
        Class fieldClass = field.getType();

        Field idOfField = null;

        Field[] fieldsOfFieldClass = fieldClass.getDeclaredFields();

        for(Field f : fieldsOfFieldClass)
        {
            if(f.isAnnotationPresent(Id.class) && idOfField == null)
                idOfField = f;
            else if(f.isAnnotationPresent(Id.class) && idOfField != null)
                throw new IllegalStateException(fieldClass.getName() + " contains more than one id field");
        }

        if(idOfField == null)
            throw new IllegalStateException(fieldClass.getName() + " doesn't contain id field");

        field.setAccessible(true);
        idOfField.setAccessible(true);

        String id = (idOfField.get(field.get(object))).toString();

        field.setAccessible(false);
        idOfField.setAccessible(false);

        return id;
    }
}
