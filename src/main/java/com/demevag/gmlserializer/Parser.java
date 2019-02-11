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

//TODO: normal work with exceptions
//TODO: add logging
//TODO: Список ребёр, может находится в узле, из которого они выходят
//TODO: Обрабатывать enum как data
public class Parser
{
    public GmlGraph parse(Object object) throws IllegalAccessException
    {
        Class rootClass = object.getClass();

        if (!rootClass.isAnnotationPresent(Graph.class))
            throw new IllegalArgumentException("Root object should have Graph annotation");

        Graph graphAnnotation = (Graph) rootClass.getAnnotation(Graph.class);

        String graphId = graphAnnotation.id();
        GmlEdgeType defaultEdgeType = graphAnnotation.edgedefault();

        return parseGraph(object,graphId, defaultEdgeType);
    }

    private GmlGraph parseGraph(Object object, String graphId, GmlEdgeType defaultEdgeType) throws IllegalAccessException
    {
        GmlGraph graph = new GmlGraph(graphId, defaultEdgeType);

        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields)
        {
            if(field.isAnnotationPresent(Ignore.class))
                continue;

            if (isCollectionOfEdges(field))
            {
                graph.addEdges(parseCollectionOfEdges(field, object, defaultEdgeType));
                continue;
            }
            if (isCollectionOfNodes(field))
            {
                graph.addNodes(parseCollectionOfNodes(field, object, graph));
                continue;
            }

            throw new IllegalArgumentException("Graph " + object.getClass() + " must have only nodes and edges");
        }

        return graph;
    }

    //TODO: реализовать
    private GmlGraph parseSubGraph(Field field,Object object, String graphId, GmlEdgeType defaultEdgeType) throws IllegalAccessException
    {
        Class fieldClass = field.getType();

        if( field.isAnnotationPresent(Graph.class) )
            return parseGraph(getFieldData(field, object), graphId, defaultEdgeType);

        if(!isCollectionOfNodes(field))
            throw new IllegalArgumentException(field.getName() + " is neither Graph nor Collection of nodes");

        GmlGraph graph = new GmlGraph(graphId, defaultEdgeType);
        graph.addNodes(parseCollectionOfNodes(field, object, graph));

        return graph;
    }

    private GmlNode parseNode(Object object, GmlGraph graph) throws IllegalAccessException
    {
        Class nodeClass = object.getClass();

        GmlNode node = new GmlNode(nodeClass.getName());

        Field[] nodeFields = nodeClass.getDeclaredFields();

        boolean hasId = false;

        for (Field field : nodeFields)
        {
            if(field.isAnnotationPresent(Ignore.class))
                continue;

            if (isIdField(field))
            {
                node.setId(node.getId() + "_" + getFieldData(field, object));
                hasId = true;

                continue;
            }

            if (isPrimitiveOrString(field))
            {
                node.addDataAttribute(parseData(field, GmlKeyTarget.NODE, object));
            } else if (field.isAnnotationPresent(SubGraph.class))
            {
                SubGraph subGraphAnnotation = field.getAnnotation(SubGraph.class);

                node.addSubGraph(parseSubGraph(field,object, subGraphAnnotation.id(), subGraphAnnotation.edgedefault()));
            } else if (isCollectionOfEdges(field))
            {
                graph.addEdges(parseCollectionOfEdges(field, object, graph.getDefaultEdgeType(), getId(object), nodeClass.getName()));
            } else
                throw new IllegalArgumentException(field.getName() + " is non-primitive and without SubGraph annotation");
        }

        if(!hasId)
            throw  new IllegalStateException(nodeClass.getName()+" has no id field");

        return node;
    }

    private GmlEdge parseEdge(Object object, GmlEdgeType edgeType) throws IllegalAccessException
    {
        Class edgeClass = object.getClass();

        GmlEdge edge = new GmlEdge(edgeClass.getName(), edgeType);

        Field[] nodeFields = edgeClass.getDeclaredFields();

        boolean hasId = false;

        for (Field field : nodeFields)
        {
            if(field.isAnnotationPresent(Ignore.class))
                continue;

            if (isIdField(field))
            {
                edge.setId(edge.getId() + "_" + getFieldData(field, object));
                hasId = true;

                continue;
            }

            if (isPrimitiveOrString(field))
            {
                edge.addDataAttribute(parseData(field, GmlKeyTarget.EDGE, object));
            } else if (field.isAnnotationPresent(EdgeSource.class))
                edge.setSourceId(field.getType().getName()+"_"+getId(field, object));
            else if (field.isAnnotationPresent(EdgeTarget.class))
                edge.setTargetId(field.getType().getName()+"_"+getId(field, object));
            else
                throw new IllegalArgumentException(field.getName() + " is non-primitive");

        }

        if(!hasId)
            throw  new IllegalStateException(edgeClass.getName()+" has no id field");

        return edge;
    }

    private GmlEdge parseEdge(Object object, GmlEdgeType edgeType, String sourceId, String sourceClassName) throws IllegalAccessException
    {
        GmlEdge edge = parseEdge(object, edgeType);
        edge.setSourceId(sourceClassName + "_" + sourceId);

        return edge;
    }

    private List<GmlEdge> parseCollectionOfEdges(Field field, Object object, GmlEdgeType defaultEdgeType) throws IllegalAccessException
    {

        Collection collection = (Collection) getFieldData(field, object);

        List<GmlEdge> edges = new ArrayList<GmlEdge>();

        for (Object o : collection)
        {
            edges.add(parseEdge(o, defaultEdgeType));
        }

        return edges;
    }

    private List<GmlEdge> parseCollectionOfEdges(Field field, Object object, GmlEdgeType defaultEdgeType, String sourceId, String sourceClassName) throws IllegalAccessException
    {
        List<GmlEdge> edges = parseCollectionOfEdges(field, object, defaultEdgeType);

        for(GmlEdge edge : edges)
            edge.setSourceId(sourceClassName + "_" + sourceId);

        return edges;
    }

    private List<GmlNode> parseCollectionOfNodes(Field field, Object object, GmlGraph graph) throws IllegalAccessException
    {
        Collection collection = (Collection) getFieldData(field, object);

        List<GmlNode> nodes = new ArrayList<GmlNode>();

        for (Object o : collection)
        {
            nodes.add(parseNode(o, graph));
        }

        return nodes;
    }

    private List<GmlEdge> parseMapOfEdges(Field field, Object object, GmlEdgeType defaultEdgeType) throws IllegalAccessException
    {
        Map map = (Map) getFieldData(field, object);

        List<GmlEdge> edges = new ArrayList<GmlEdge>();

        for(Object o : map.values())
        {
            GmlEdge edge = parseEdge(o, defaultEdgeType);
            //TODO: map keys = ?

            edges.add(edge);
        }

        return edges;
    }

    private List<GmlNode> parseMapOfNodes(Field field, Object object, GmlGraph graph) throws IllegalAccessException
    {
        Map map = (Map) getFieldData(field, object);

        List<GmlNode> nodes = new ArrayList<GmlNode>();

        for(Object o : map.values())
        {
            GmlNode node = parseNode(o, graph);
            //TODO: map keys = ?

            nodes.add(node);
        }
        return nodes;
    }

    private GmlData parseData(Field field, GmlKeyTarget target, Object object) throws IllegalAccessException
    {
        GmlKey dataKey = new GmlKey(field.getName() + "_key", target, field.getName());

        Object data = getFieldData(field, object);

        dataKey.setAttrType(getDataType(data));

        return new GmlData(dataKey, data);
    }

    private String getDataType(Object data)
    {
        if(data instanceof Integer)
            return "int";

        return data.getClass().getName().toLowerCase();
    }


    private boolean isPrimitiveOrString(Field field)
    {
        return field.getType().isPrimitive() || field.getType() == String.class;
    }

    private boolean isIdField(Field field)
    {
        return isPrimitiveOrString(field) && field.isAnnotationPresent(Id.class);
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

            if (fieldArgTypes.length > 1)
                throw new IllegalArgumentException("Collection must have only one generic type");

            return (Class) fieldArgTypes[0];
        }

        return null;
    }

    public Object getFieldData(Field field, Object object) throws IllegalAccessException
    {
        field.setAccessible(true);

        Object data = field.get(object);

        field.setAccessible(false);

        return data;
    }

    private String getId(Field field, Object object) throws IllegalAccessException
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

    private String getId(Object object) throws IllegalAccessException
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
