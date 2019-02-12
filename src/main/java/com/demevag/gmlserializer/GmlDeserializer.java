package com.demevag.gmlserializer;

import com.demevag.gmlserializer.elements.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        for(Element keyElement : root.getChildren("key", Namespace.getNamespace("http://graphml.graphdrawing.org/xmlns")))
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

        for(Element graphElement : element.getChildren())
        {
            if(graphElement.getName().equals("node"))
                graph.addNode(parseNode(graphElement));
            else if(graphElement.getName().equals("edge") )
                graph.addEdge(parseEdge(graphElement));
            else
                throw new IllegalStateException("Unknown element inside graph: "+graphElement.getName());
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

        if(edgeIsDirected != null && edgeIsDirected.equals("true") )
            edgeType = GmlEdgeType.DIRECTED;

        GmlEdge edge = new GmlEdge(edgeId, edgeType);
        edge.setSourceId(sourceId);
        edge.setTargetId(targetId);

        for(Element edgeElement : element.getChildren())
        {
            if(edgeElement.getName().equals("data") )
                edge.addDataAttribute(parseData(edgeElement));
            else
                throw new IllegalStateException("Unknown element inside edge: "+edgeElement.getName());
        }

        return edge;
    }

    private GmlNode parseNode(Element element) throws ClassNotFoundException
    {
        String nodeId = element.getAttributeValue("id");

        GmlNode node = new GmlNode(nodeId);

        for(Element nodeElement : element.getChildren())
        {
            if(nodeElement.getName().equals("data") )
                node.addDataAttribute(parseData(nodeElement));
            else if(nodeElement.getName().equals("graph") )
                node.addSubGraph(parseGraph(nodeElement));
            else
                throw new IllegalStateException("Unknown element inside node: "+nodeElement.getName());
        }

        return node;
    }

    private GmlData parseData(Element element) throws ClassNotFoundException
    {
        String keyId = element.getAttributeValue("key");

        GmlData dataAttribute = new GmlData(keys.get(keyId));

        Class dataClass = Class.forName(keys.get(keyId).getAttrType());

        Object data =castFromString(element.getValue(), dataClass);

        dataAttribute.setData(data);

        return dataAttribute;
    }

    private Object castFromString(String value, Class dataClass)
    {
        if(String.class.isAssignableFrom(dataClass))
            return value;

        if(Integer.class.isAssignableFrom(dataClass))
            return Integer.parseInt(value);

        if(Long.class.isAssignableFrom(dataClass))
            return Long.parseLong(value);

        if(Short.class.isAssignableFrom(dataClass))
            return Short.parseShort(value);

        if(Float.class.isAssignableFrom(dataClass))
            return Float.parseFloat(value);

        if(Double.class.isAssignableFrom(dataClass))
            return Double.parseDouble(value);

        return null;
    }
}
