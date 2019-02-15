package com.demevag.gmlserializer;

import com.demevag.gmlserializer.elements.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GmlDeserializer
{
    private Map<String, GmlKey> keys;

    public GmlGraph deserialize(InputStream inputStream) throws JDOMException, IOException, ClassNotFoundException
    {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(inputStream);
        Element root = doc.getRootElement();

        keys = new HashMap<String, GmlKey>();

        for (Element keyElement : root.getChildren("key", Namespace.getNamespace("http://graphml.graphdrawing.org/xmlns")))
        {
            GmlKey key = parseKey(keyElement);

            keys.put(key.getId(), key);
        }

        Element graphElement = root.getChild("graph", Namespace.getNamespace("http://graphml.graphdrawing.org/xmlns"));
        return parseGraph(graphElement);
    }

    private GmlKey parseKey(Element element)
    {
        String id = element.getAttributeValue("id");
        String targetName = element.getAttributeValue("for");
        String attrName = element.getAttributeValue("attr.name");
        String attrType = element.getAttributeValue("attr.type");

        GmlKey key = new GmlKey(id, GmlKeyTarget.valueOf(targetName.toUpperCase()), attrName);

        key.setAttrType(attrType);

        return key;
    }

    private GmlGraph parseGraph(Element element) throws ClassNotFoundException
    {
        String graphId = element.getAttributeValue("id");
        String defaultEdgeTypeName = element.getAttributeValue("edgedefault");

        GmlGraph graph = new GmlGraph(graphId, GmlEdgeType.valueOf(defaultEdgeTypeName.toUpperCase()));

        for (Element graphElement : element.getChildren())
        {
            if (graphElement.getName().equals("node"))
                graph.addNode(parseNode(graphElement));
            else if (graphElement.getName().equals("edge"))
                graph.addEdge(parseEdge(graphElement));
            else
                throw new IllegalStateException("Unknown element inside graph: " + graphElement.getName());
        }

        return graph;
    }

    private GmlEdge parseEdge(Element element) throws ClassNotFoundException
    {
        String edgeId = element.getAttributeValue("id");
        String sourceId = element.getAttributeValue("source");
        String targetId = element.getAttributeValue("target");

        String edgeIsDirected = element.getAttributeValue("directed");

        GmlEdgeType edgeType = GmlEdgeType.UNDIRECTED;

        if (edgeIsDirected != null && edgeIsDirected.equals("true"))
            edgeType = GmlEdgeType.DIRECTED;

        GmlEdge edge = new GmlEdge(edgeId, edgeType);
        edge.setSourceId(sourceId);
        edge.setTargetId(targetId);

        List<Element> elementsWithComplexData = new ArrayList<>();

        for (Element edgeElement : element.getChildren())
        {
            if (edgeElement.getName().equals("data"))
            {
                if (edgeElement.getAttributeValue("key").contains("c:"))
                {
                    elementsWithComplexData.add(edgeElement);
                } else
                    edge.addDataAttribute(parseData(edgeElement));
            } else
                throw new IllegalStateException("Unknown element inside edge: " + edgeElement.getName());
        }

        edge.addComplexDataAttribute(parseComplexDataElements(elementsWithComplexData));

        return edge;
    }

    private GmlNode parseNode(Element element) throws ClassNotFoundException
    {
        String nodeId = element.getAttributeValue("id");

        GmlNode node = new GmlNode(nodeId);

        List<Element> elementsWithComplexData = new ArrayList<>();

        for (Element nodeElement : element.getChildren())
        {
            if (nodeElement.getName().equals("data"))
            {
                if (nodeElement.getAttributeValue("key").contains("c:"))
                {
                    elementsWithComplexData.add(nodeElement);
                } else
                    node.addDataAttribute(parseData(nodeElement));
            } else if (nodeElement.getName().equals("graph"))
                node.addSubGraph(parseGraph(nodeElement));
            else
                throw new IllegalStateException("Unknown element inside node: " + nodeElement.getName());
        }

        node.addComplexDataAttribute(parseComplexDataElements(elementsWithComplexData));

        return node;
    }

    private List<GmlComplexData> parseComplexDataElements(List<Element> elements) throws ClassNotFoundException
    {
        if(elements.size() == 0)
            return new ArrayList<>();

        String firstElementKey = elements.get(0).getAttributeValue("key");

        String className =firstElementKey .split("_")[0];
        String suffix = "";

        if(firstElementKey.matches(".*#[0-9]+"))
            suffix ="#" + firstElementKey.split("#")[1];

        List<GmlComplexData> result = new ArrayList<>();

        List<Element> currentComplexData = new ArrayList<>();

        for(Element element : elements)
        {
            String elementKey = element.getAttributeValue("key");

            if(elementKey.matches(className+"_.+_key"+suffix))
                currentComplexData.add(element);
            else
            {
                result.add(parseComplexData(currentComplexData));

                currentComplexData.clear();
                currentComplexData.add(element);

                className =elementKey .split("_")[0];
                suffix = "";

                if(elementKey.matches(".*#[0-9]+"))
                    suffix ="#" + elementKey.split("#")[1];
            }
        }

        result.add(parseComplexData(currentComplexData));

        return result;
    }

    private GmlComplexData parseComplexData(List<Element> elements) throws ClassNotFoundException
    {
        GmlComplexData complexData = new GmlComplexData();

        for(Element element : elements)
        {
            complexData.addDataAttribute(parseData(element));
        }

        return complexData;
    }

    private GmlData parseData(Element element) throws ClassNotFoundException
    {
        String keyId = element.getAttributeValue("key");

        GmlData dataAttribute = new GmlData(keys.get(keyId));

        Class dataClass = getClassFromGmlType(keys.get(keyId).getAttrType());

        Object data = castFromString(element.getValue(), dataClass);

        dataAttribute.setData(data);

        return dataAttribute;
    }

    private Class getClassFromGmlType(String gmlTypeName) throws ClassNotFoundException
    {
        if (gmlTypeName.equals("string"))
            return String.class;

        if (gmlTypeName.equals("byte"))
            return byte.class;
        if (gmlTypeName.equals("short"))
            return short.class;
        if (gmlTypeName.equals("int"))
            return int.class;
        if (gmlTypeName.equals("long"))
            return long.class;
        if (gmlTypeName.equals("char"))
            return char.class;
        if (gmlTypeName.equals("float"))
            return float.class;
        if (gmlTypeName.equals("double"))
            return double.class;
        if (gmlTypeName.equals("boolean"))
            return boolean.class;
        if (gmlTypeName.equals("void"))
            return void.class;

        throw new IllegalArgumentException("Not primitive type : " + gmlTypeName);

    }

    private Object castFromString(String value, Class dataClass)
    {
        if (String.class.isAssignableFrom(dataClass))
            return value;

        if (int.class.isAssignableFrom(dataClass))
            return Integer.parseInt(value);

        if (long.class.isAssignableFrom(dataClass))
            return Long.parseLong(value);

        if (short.class.isAssignableFrom(dataClass))
            return Short.parseShort(value);

        if (byte.class.isAssignableFrom(dataClass))
            return Byte.parseByte(value);

        if (char.class.isAssignableFrom(dataClass))
            return value.toCharArray()[0];

        if (boolean.class.isAssignableFrom(dataClass))
            return Boolean.parseBoolean(value);

        if (float.class.isAssignableFrom(dataClass))
            return Float.parseFloat(value);

        if (double.class.isAssignableFrom(dataClass))
            return Double.parseDouble(value);

        return null;
    }
}
