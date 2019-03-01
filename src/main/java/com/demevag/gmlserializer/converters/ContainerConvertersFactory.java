package com.demevag.gmlserializer.converters;

public class ContainerConvertersFactory
{
    public static ContainerConverter getConverterForFieldType(ContainerType containerType)
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
