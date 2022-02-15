package com.example.springboot.wsdl.responsereportes;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "RESPUESTA")
public class RESPUESTA {

    @JacksonXmlProperty(localName = "ERROR")
    public ERROR ERROR;
    @JacksonXmlProperty(localName = "REGISTROS")
    public REGISTROS REGISTROS;

    public com.example.springboot.wsdl.responsereportes.ERROR getERROR() {
        return ERROR;
    }

    public void setERROR(com.example.springboot.wsdl.responsereportes.ERROR ERROR) {
        this.ERROR = ERROR;
    }

    public com.example.springboot.wsdl.responsereportes.REGISTROS getREGISTROS() {
        return REGISTROS;
    }

    public void setREGISTROS(com.example.springboot.wsdl.responsereportes.REGISTROS REGISTROS) {
        this.REGISTROS = REGISTROS;
    }
}
