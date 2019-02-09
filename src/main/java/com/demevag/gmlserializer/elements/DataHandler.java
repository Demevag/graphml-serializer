package com.demevag.gmlserializer.elements;

import java.util.List;

public interface DataHandler
{
    void addDataAttribute(GmlData data);
    List<GmlData> getDataAttributes();
    void setDataAttributes(List<GmlData> dataAttributes);

    void setId(String id);
    String getId();
}
