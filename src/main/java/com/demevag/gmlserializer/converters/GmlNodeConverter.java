package com.demevag.gmlserializer.converters;

import com.demevag.gmlserializer.elements.*;
import com.demevag.gmlserializer.parsers.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GmlNodeConverter extends ElementConvertor<GmlNode, GmlGraph>
{
    @Override
    protected Object convertSpecificFields(Object elementObject, List<Field> fields, GmlNode gmlElement) throws IllegalAccessException
    {
        return elementObject;
    }

    //todo: fix repeating
    //todo: add support for collection/map complexData
    @Override
    protected GmlElement extractGmlElementForFieldType(GmlNode gmlNode, ElementType elementType, Field field)
    {
        String fieldName = field.getName();

        switch (elementType)
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

        throw new IllegalArgumentException("Node class can't contain "+ elementType.name()+" field");
    }

    @Override
    protected List<GmlElement> extractGmlElementsForContainerField(GmlNode gmlNode, ContainerType elementType, Field containerField, GmlGraph parentElement)
    {
        switch (elementType)
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

            case COMPLEX_DATA_COLLECTION:{
                List<GmlElement> complexDataList = new ArrayList<>();

                Class containerDataClass = Utils.getCollectionArgClass(containerField);
                String containerDataClassName = Utils.getClassNameWithoutPackage(containerDataClass);

                for(GmlComplexData complexData : gmlNode.getComplexDataAttributes())
                {
                    GmlKey complexDataKey = complexData.getData().get(0).getKey();

                    if(complexDataKey.getAtrrName().contains(containerDataClassName))
                        complexDataList.add(complexData);
                }

                return complexDataList;

            }

            case NODE_MAP:{
                return new ArrayList<>(gmlNode.getSubGraphs().get(0).getNodes()); //todo: fix
            }
        }

        throw new IllegalStateException("Node can't contain "+ elementType.name());
    }
}
