package com.demevag.gmlserializer.convertors;

public class ContainerConvertersFactory
{
    public static ContainerConvertor getConverterForFieldType(ContainerType containerType)
    {
        switch (containerType)
        {
            case EDGE_COLLECTION:
            case NODE_COLLECTION: return new CollectionConverter();

            case NODE_MAP: return null;
        }

        throw new IllegalArgumentException("No convertor for "+containerType.name());
    }
}