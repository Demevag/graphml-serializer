package com.demevag.gmlserializer;

import com.demevag.gmlserializer.annotations.EdgeSource;
import com.demevag.gmlserializer.annotations.EdgeTarget;
import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.*;

public class Resolver
{
    private Map<String, Object> resolvedNodes = new HashMap<>();
    private Set<Object> nodesWithEdgesInside = new HashSet<>();
    private Map<GmlEdge, Object> resolvedEdges = new HashMap<>();

    public Object resolve(Class graphClass, GmlGraph gmlGraph) throws IllegalAccessException, InstantiationException
    {
        Object graphObject = graphClass.newInstance();

        Field[] graphFields = graphClass.getDeclaredFields();

        for(Field field : graphFields)
        {
            if(Utils.isCollectionOfNodes(field))
            {
                Collection nodesCollection = createCollectionOfNodes(Utils.getCollectionArgClass(field),
                                                                    gmlGraph.getNodes(),
                                                                    ArrayList.class,
                                                                    gmlGraph.getEdges());
                setDataToField(graphObject, field, nodesCollection);
            }
            else if(Utils.isMapOfNodes(field))
            {
                Map nodesMap = createMapOfNodes(Utils.getMapValueClass(field),
                                                        gmlGraph.getNodes(),
                                                        HashMap.class,
                                                        gmlGraph.getEdges());
                setDataToField(graphObject, field, nodesMap);
            }
            else if(Utils.isCollectionOfEdges(field))
            {
                Collection edgesCollection = createCollectionOfEdges(Utils.getCollectionArgClass(field),
                                                                    gmlGraph.getEdges(),
                                                                    ArrayList.class);

                setDataToField(graphObject, field, edgesCollection);
            }
        }

        for(GmlEdge gmlEdge : resolvedEdges.keySet())
        {
            setEdgeLinks(resolvedEdges.get(gmlEdge), gmlEdge);
        }

        return graphObject;
    }

    private Object createNode(Class nodeClass, GmlNode gmlNode, List<GmlEdge> gmlEdges) throws IllegalAccessException, InstantiationException
    {
        Field[] nodeFields = nodeClass.getDeclaredFields();

        Object nodeObject = nodeClass.newInstance();

        setDataFields(nodeObject, nodeFields, gmlNode.getDataAttributes());
        setComplexDataFields(nodeObject, nodeFields, gmlNode.getComplexDataAttributes());
        setIdField(nodeObject, gmlNode.getId(), nodeFields);

        for(Field field : nodeFields)
            if(Utils.isCollectionOfEdges(field))
            {
                Collection edgeCollection = createCollectionOfEdges(Utils.getCollectionArgClass(field),
                                                                    gmlEdges,
                                                                    ArrayList.class,
                                                                    gmlNode.getId());
                setDataToField(nodeObject,field, edgeCollection);

                nodesWithEdgesInside.add(nodeObject);
            }

        resolvedNodes.put(gmlNode.getId(), nodeObject);

        return nodeObject;
    }

    private Object createEdge(Class edgeClass, GmlEdge gmlEdge) throws IllegalAccessException, InstantiationException
    {
        Field[] edgeFields = edgeClass.getDeclaredFields();

        Object edgeObject = edgeClass.newInstance();

        setDataFields(edgeObject, edgeFields, gmlEdge.getDataAttributes());
        setComplexDataFields(edgeObject, edgeFields, gmlEdge.getComplexDataAttributes());
        setIdField(edgeObject, gmlEdge.getId(), edgeFields);

        resolvedEdges.put(gmlEdge, edgeObject);

        return edgeObject;
    }

    private void setEdgeLinks(Object edgeObject, GmlEdge gmlEdge) throws IllegalAccessException
    {
        Field[] edgeFields = edgeObject.getClass().getDeclaredFields();

        for(Field field : edgeFields)
        {
            if(field.isAnnotationPresent(EdgeSource.class))
                setEdgeLink(edgeObject, field, gmlEdge.getSourceId());
            else if(field.isAnnotationPresent(EdgeTarget.class))
                setEdgeLink(edgeObject,field, gmlEdge.getTargetId());
        }
    }

    private void setEdgeLink(Object  edgeObject, Field linkField, String nodeId) throws IllegalAccessException
    {
        if(linkField.getType().equals(String.class))
            setDataToField(edgeObject, linkField, nodeId);
        else
            setDataToField(edgeObject, linkField, resolvedNodes.get(nodeId));

    }

    private Collection createCollectionOfNodes(Class nodeClass, List<GmlNode> gmlNodes, Class collectionType, List<GmlEdge> gmlEdges) throws IllegalAccessException, InstantiationException
    {
        Collection nodeCollection = (Collection) collectionType.newInstance();

        for(GmlNode gmlNode : gmlNodes)
        {
            nodeCollection.add(createNode(nodeClass, gmlNode, gmlEdges));
        }

        return nodeCollection;
    }

    private Collection createCollectionOfEdges(Class edgeClass, List<GmlEdge> gmlEdges, Class collectionType) throws IllegalAccessException, InstantiationException
    {
        Collection edgeCollection = (Collection) collectionType.newInstance();

        for(GmlEdge gmlEdge : gmlEdges)
        {
            edgeCollection.add(createEdge(edgeClass, gmlEdge));
        }

        return edgeCollection;
    }

    private Collection createCollectionOfEdges(Class edgeClass, List<GmlEdge> gmlEdges, Class collectionType, String sourceNodeId) throws InstantiationException, IllegalAccessException
    {
        List<GmlEdge> gmlEdgesInsideNode = new ArrayList<>();

        for(GmlEdge gmlEdge : gmlEdges)
            if(gmlEdge.getSourceId().equals(sourceNodeId))
                gmlEdgesInsideNode.add(gmlEdge);

        return createCollectionOfEdges(edgeClass, gmlEdgesInsideNode, collectionType);
    }

    private Map createMapOfNodes(Class nodeClass, List<GmlNode> gmlNodes, Class mapType, List<GmlEdge> gmlEdges) throws IllegalAccessException, InstantiationException
    {
        Map nodeMap = (Map) mapType.newInstance();

        for(GmlNode gmlNode : gmlNodes)
        {
            nodeMap.put(gmlNode.getId(), createNode(nodeClass, gmlNode, gmlEdges));
        }

        return nodeMap;
    }

    private Map resolveMapOfEdges(Class edgeClass, List<GmlEdge> gmlEdges)
    {
        //not implemented yet
        return null;
    }

    private void setIdField(Object parentObject, String id, Field[] fields) throws IllegalAccessException
    {
        for(Field field : fields)
        {
            if(Utils.isIdField(field))
            {
                setDataToField(parentObject, field, id);
                return;
            }

        }

        throw new IllegalStateException("There are no id fields in "+parentObject.getClass());
    }

    private void setDataFields(Object parentObject, Field[] fields, List<GmlData> dataAttributes) throws IllegalAccessException
    {
        for(GmlData data : dataAttributes)
        {
            Field fieldForData = findFieldForData(data, fields);

            setDataToField(parentObject, fieldForData, data.getData());
        }
    }

    private void setComplexDataFields(Object parentObject, Field[] fields, List<GmlComplexData> complexDataAttributes) throws IllegalAccessException, InstantiationException
    {
        for(GmlComplexData complexData : complexDataAttributes)
        {
            Field fieldForData = findFieldForComplexData(complexData, fields);

            Class dataClass = fieldForData.getType();

            Object fieldObject = dataClass.newInstance();

            setDataFields(fieldObject, dataClass.getDeclaredFields(), complexData.getData());
            setDataToField(parentObject, fieldForData, fieldObject);
        }
    }

    private Field findFieldForData(GmlData data, Field[] fields)
    {
        GmlKey key = data.getKey();

        for(Field field : fields)
        {
            if(key.getAtrrName().contains(field.getName()))
                return field;
        }

        throw new IllegalStateException("No fields for "+key.getAtrrName()+" data");
    }

    private Field findFieldForComplexData(GmlComplexData complexData, Field[] fields)
    {
        GmlKey key = complexData.getData().get(0).getKey();

        for(Field field : fields)
        {
            String fieldClassWithoutPackage = Utils.getClassNameWithoutPackage(field.getType());
            if(key.getAtrrName().contains(fieldClassWithoutPackage))
                return field;
        }

        throw new IllegalStateException("No fields for "+key.getAtrrName()+" data");

    }

    private void setDataToField(Object parentObject, Field field, Object data) throws IllegalAccessException
    {
        field.setAccessible(true);

        field.set(parentObject, data);

        field.setAccessible(false);
    }
}
