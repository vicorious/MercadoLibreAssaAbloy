package com.example.springboot.wsdl.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;


@JacksonXmlRootElement(localName = "detalle")
public class Detalle {

    @JacksonXmlElementWrapper(localName = "items")
    @JacksonXmlProperty(localName = "item")
    private List<Item> items;

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
