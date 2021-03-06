/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer;

import com.demevag.gmlserializer.elements.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xembly.Directive;
import org.xembly.Directives;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GmlSerializer
{
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;

    public GmlSerializer()
    {
        try
        {
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }


    public void serialize(Object object, OutputStream outputStream) throws IllegalAccessException, ImpossibleModificationException
    {
        Parser parser = new Parser();

        GmlGraph graph = parser.parse(object);

        Document document = docBuilder.newDocument();

        Directives directives = new Directives();
        directives
                .add("graphml")
                .attr("xmlns", "http://graphml.graphdrawing.org/xmlns")
                .attr("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance")
                .attr("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns\n http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");

        addKeyElements(graph, directives);
        addGraph(graph, directives);


        String xml = new Xembler(directives).xml();

        PrintWriter writer = new PrintWriter(outputStream);

        writer.print(xml);
        writer.flush();

    }

    private void addGraph(GmlGraph graph, Directives directives)
    {
        directives
                .add("graph")
                .attr("id", graph.getId())
                .attr("edgedefault", graph.getDefaultEdgeType().toString());

        addNodeElements(graph, directives);
        addEdgeElements(graph, directives);

        directives.up();
    }

    private void addKeyElements(GmlGraph graph, Directives directives)
    {

        List<GmlData> dataAttributes = new ArrayList<GmlData>();

        if (graph.getEdges().size() > 0)
        {
            GmlEdge edge = graph.getEdges().get(0);
            dataAttributes.addAll(edge.getDataAttributes());

            for (GmlComplexData complexData : edge.getComplexDataAttributes())
                dataAttributes.addAll(complexData.getData());
        }

        if (graph.getNodes().size() > 0)
        {
            GmlNode node = graph.getNodes().get(0);
            dataAttributes.addAll(node.getDataAttributes());


            for (GmlComplexData complexData : node.getComplexDataAttributes())
                dataAttributes.addAll(complexData.getData());
        }

        for (GmlData dataAttrib : dataAttributes)
        {
            GmlKey key = dataAttrib.getKey();

            directives
                    .add("key")
                    .attr("id", key.getId())
                    .attr("for", key.getTarget().toString())
                    .attr("attr.name", key.getAtrrName())
                    .attr("attr.type", key.getAttrType());

            if (key.getDefaultVal() != null)
            {
                directives
                        .add("default")
                        .set(key.getDefaultVal().toString())
                        .up();
            }

            directives.up();
        }
    }

    private void addNodeElements(GmlGraph graph, Directives directives)
    {
        for (GmlNode node : graph.getNodes())
        {
            directives
                    .add("node")
                    .attr("id", node.getId());

            addDataElements(node.getDataAttributes(), directives);
            addComplexDataElements(node.getComplexDataAttributes(), directives);

            for (GmlGraph subGraph : node.getSubGraphs())
                addGraph(subGraph, directives);

            directives.up();
        }
    }

    private void addEdgeElements(GmlGraph graph, Directives directives)
    {
        for (GmlEdge edge : graph.getEdges())
        {
            directives
                    .add("edge")
                    .attr("id", edge.getId())
                    .attr("source", edge.getSourceId())
                    .attr("target", edge.getTargetId());

            if (edge.getType() == GmlEdgeType.DIRECTED)
                directives.attr("directed", "true");

            addDataElements(edge.getDataAttributes(), directives);
            addComplexDataElements(edge.getComplexDataAttributes(), directives);

            directives.up();
        }
    }

    private void addComplexDataElements(List<GmlComplexData> complexDataAttributes, Directives directives)
    {
        for (GmlComplexData complexData : complexDataAttributes)
            addDataElements(complexData.getData(), directives);
    }

    private void addDataElements(List<GmlData> dataAttributes, Directives directives)
    {
        for (GmlData dataAttrib : dataAttributes)
        {
            directives
                    .add("data")
                    .attr("key", dataAttrib.getKey().getId());

            if (dataAttrib.getData() != null)
                directives
                        .set(dataAttrib.getData().toString())
                        .up();
            else
                directives
                        .set("")
                        .up();
        }
    }
}
