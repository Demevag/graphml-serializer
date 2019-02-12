package com.demevag.gmlserializer.parsers;

import com.demevag.gmlserializer.elements.GmlEdge;
import com.demevag.gmlserializer.elements.GmlEdgeType;
import com.demevag.gmlserializer.elements.GmlElement;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GmlEdgeCollectionParser implements ContainerParser
{

    private Object parentObject;
    private GmlEdgeType defaultEdgeType;
    private String sourceId;
    private String sourceClassName;

    public GmlEdgeCollectionParser(Object object, GmlEdgeType defaultEdgeType)
    {
        this.parentObject = object;
        this.defaultEdgeType = defaultEdgeType;

        this.sourceId = null;
        this.sourceClassName = null;
    }

    public GmlEdgeCollectionParser(Object object, GmlEdgeType defaultEdgeType, String sourceId, String sourceClassName)
    {
        this.parentObject = object;
        this.defaultEdgeType = defaultEdgeType;

        this.sourceId = sourceId;
        this.sourceClassName = sourceClassName;
    }

    public List<? extends GmlElement> parse(Field field) throws IllegalAccessException
    {
        Collection collection = (Collection) Utils.getFieldData(field, parentObject);
        ElementParser edgeParser = new GmlEdgeParser(parentObject, defaultEdgeType);

        List<GmlEdge> edges = new ArrayList<GmlEdge>();

        for (Object o : collection)
        {
            GmlEdge edge = (GmlEdge) edgeParser.parse(o);

            if(sourceClassName != null && sourceId != null)
            {
                edge.setSourceId(sourceClassName + "_" + sourceId);
            }

            edges.add(edge);
        }


        return edges;
    }

    public List<? extends GmlElement> parse(Field[] fields) throws IllegalAccessException
    {
        List<GmlEdge> edges = new ArrayList<>();

        for(Field field : fields)
        {
            if(Utils.isCollectionOfEdges(field))
                edges.addAll((Collection<? extends GmlEdge>) parse(field));
        }

        return edges;
    }
}
