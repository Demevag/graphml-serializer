package com.demevag.gmlserializer.convertors;

import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlNodeConvertor extends ElementConvertor<GmlNode, GmlGraph>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlNode gmlElement) throws IllegalAccessException
    {
        return elementObject;
    }

    //todo: fix repeating
    @Override
    protected GmlElement extractGmlElementForFieldType(GmlNode gmlNode, FieldType fieldType, Field field)
    {
        String fieldName = field.getName();

        switch (fieldType)
        {
            case DATA:{
                for(GmlData gmlData : gmlNode.getDataAttributes())
                {
                    GmlKey key = gmlData.getKey();

                    if(key.getAtrrName().contains(fieldName))
                        return gmlData;
                }

                throw new IllegalStateException("No data for "+ fieldName);
            }
            case COMPLEX_DATA:{
                for(GmlComplexData complexData : gmlNode.getComplexDataAttributes())
                {
                    GmlKey key = complexData.getData().get(0).getKey();

                    if(key.getAtrrName().contains(Utils.getClassNameWithoutPackage(field.getType())))
                        return complexData;
                }

                throw new IllegalStateException("No complex data for " +fieldName);
            }
            case ID:{
                GmlKey keyForIdData = new GmlKey("id_field", GmlKeyTarget.EDGE, fieldName);
                return new GmlData(keyForIdData, gmlNode.getId());
            }
        }

        throw new IllegalArgumentException("Edge class can't contain "+fieldType.name()+" field");
    }

    @Override
    protected List<GmlElement> extractGmlElementsForContainerField(GmlNode gmlNode, FieldType fieldType, Field containerField, GmlGraph parentElement)
    {
        switch (fieldType)
        {
            case EDGE_COLLECTION:{
                List<GmlElement> edges = new ArrayList<>();

                for(GmlEdge edge : parentElement.getEdges())
                {
                    if(edge.getSourceId().equals(gmlNode.getId()))
                        edges.add(edge);
                }

                return edges;
            }
            case NODE_COLLECTION:{
                return new ArrayList<>(gmlNode.getSubGraphs().get(0).getNodes()); //todo: fix
            }
            case NODE_MAP:{
                return new ArrayList<>(gmlNode.getSubGraphs().get(0).getNodes()); //todo: fix
            }
        }

        throw new IllegalStateException("Node can't contain "+fieldType.name());
    }
}
