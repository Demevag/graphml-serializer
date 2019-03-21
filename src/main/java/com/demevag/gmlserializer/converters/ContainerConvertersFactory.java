/*
 * Copyright 2019  Viktor Khrulev
 * Licensed under the Apache License, Version 2.0
 */

package com.demevag.gmlserializer.converters;

public class ContainerConvertersFactory
{
    public static ContainerConverter getConverterForFieldType(ContainerType containerType)
    {
        switch (containerType)
        {
            case EDGE_COLLECTION:
            case NODE_COLLECTION:
            case COMPLEX_DATA_COLLECTION: return new CollectionConverter();

            case NODE_MAP: return new MapConverter();
        }

        throw new IllegalArgumentException("No convertor for "+containerType.name());
    }
}
