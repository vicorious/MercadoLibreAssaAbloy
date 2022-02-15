package com.example.springboot.wsdl.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


@JacksonXmlRootElement(localName = "encabezado")
@JsonIgnoreProperties
public class Encabezado {

    @JacksonXmlProperty(localName = "ENCDescripcion")
    private String ENCDescripcion;
    @JacksonXmlProperty(localName = "GMVCodigo")
    private String GMVCodigo;
    @JacksonXmlProperty(localName = "MOVCodigo")
    private String MOVCodigo;
    @JacksonXmlProperty(localName = "movimiento")
    private String movimiento;
    @JacksonXmlProperty(localName = "USUCodigo")
    private String USUCodigo;
    @JacksonXmlProperty(localName = "CAJCodigo")
    private String CAJCodigo;
    @JacksonXmlProperty(localName = "IDEMP")
    private String IDEMP;
    @JacksonXmlProperty(localName = "ALMCodigo")
    private String ALMCodigo;
    @JacksonXmlProperty(localName = "ALMNombre")
    private String ALMNombre;
    @JacksonXmlProperty(localName = "MONCodigo")
    private String MONCodigo;
    @JacksonXmlProperty(localName = "ENCFechaTrx")
    private String ENCFechaTrx;
    @JacksonXmlProperty(localName = "ENCHoraTrx")
    private String ENCHoraTrx;
    @JacksonXmlProperty(localName = "ENCModo")
    private String ENCModo;
    @JacksonXmlProperty(localName = "ENCTipoProc")
    private String ENCTipoProc;
    @JacksonXmlProperty(localName = "ENCConsTrx")
    private String ENCConsTrx;
    @JacksonXmlProperty(localName = "ENCTasaConversion")
    private String ENCTasaConversion;
    @JacksonXmlProperty(localName = "ENCTotalReferencias")
    private String ENCTotalReferencias;
    @JacksonXmlProperty(localName = "ENCBruto")
    private String ENCBruto;
    @JacksonXmlProperty(localName = "ENCDescuento")
    private String ENCDescuento;
    @JacksonXmlProperty(localName = "ENCPagNoVenta")
    private String ENCPagNoVenta;
    @JacksonXmlProperty(localName = "ENCVenta")
    private String ENCVenta;
    @JacksonXmlProperty(localName = "ENCImpuestos")
    private String ENCImpuestos;
    @JacksonXmlProperty(localName = "ENCComision")
    private String ENCComision;
    @JacksonXmlProperty(localName = "ENCNeto")
    private String ENCNeto;
    @JacksonXmlProperty(localName = "ENCRecaudo")
    private String ENCRecaudo;
    @JacksonXmlProperty(localName = "ENCImpuestoAsumido")
    private String ENCImpuestoAsumido;
    @JacksonXmlProperty(localName = "ENCPuntos")
    private String ENCPuntos;
    @JacksonXmlProperty(localName = "ENCEstadoLinea")
    private String ENCEstadoLinea;
    @JacksonXmlProperty(localName = "ENCRespuesta")
    private String ENCRespuesta;
    @JacksonXmlProperty(localName = "ENCDescRespuesta")
    private String ENCDescRespuesta;

    public void setENCDescripcion(String ENCDescripcion) {
        this.ENCDescripcion = ENCDescripcion;
    }

    public void setGMVCodigo(String GMVCodigo) {
        this.GMVCodigo = GMVCodigo;
    }

    public void setMOVCodigo(String MOVCodigo) {
        this.MOVCodigo = MOVCodigo;
    }

    public void setMovimiento(String movimiento) {
        this.movimiento = movimiento;
    }

    public void setUSUCodigo(String USUCodigo) {
        this.USUCodigo = USUCodigo;
    }

    public void setCAJCodigo(String CAJCodigo) {
        this.CAJCodigo = CAJCodigo;
    }

    public void setIDEMP(String IDEMP) {
        this.IDEMP = IDEMP;
    }

    public void setALMCodigo(String ALMCodigo) {
        this.ALMCodigo = ALMCodigo;
    }

    public void setALMNombre(String ALMNombre) {
        this.ALMNombre = ALMNombre;
    }

    public void setMONCodigo(String MONCodigo) {
        this.MONCodigo = MONCodigo;
    }

    public void setENCFechaTrx(String ENCFechaTrx) {
        this.ENCFechaTrx = ENCFechaTrx;
    }

    public void setENCHoraTrx(String ENCHoraTrx) {
        this.ENCHoraTrx = ENCHoraTrx;
    }

    public void setENCModo(String ENCModo) {
        this.ENCModo = ENCModo;
    }

    public void setENCTipoProc(String ENCTipoProc) {
        this.ENCTipoProc = ENCTipoProc;
    }

    public void setENCConsTrx(String ENCConsTrx) {
        this.ENCConsTrx = ENCConsTrx;
    }

    public void setENCTasaConversion(String ENCTasaConversion) {
        this.ENCTasaConversion = ENCTasaConversion;
    }

    public void setENCTotalReferencias(String ENCTotalReferencias) {
        this.ENCTotalReferencias = ENCTotalReferencias;
    }

    public void setENCBruto(String ENCBruto) {
        this.ENCBruto = ENCBruto;
    }

    public void setENCDescuento(String ENCDescuento) {
        this.ENCDescuento = ENCDescuento;
    }

    public void setENCPagNoVenta(String ENCPagNoVenta) {
        this.ENCPagNoVenta = ENCPagNoVenta;
    }

    public void setENCVenta(String ENCVenta) {
        this.ENCVenta = ENCVenta;
    }

    public void setENCImpuestos(String ENCImpuestos) {
        this.ENCImpuestos = ENCImpuestos;
    }

    public void setENCComision(String ENCComision) {
        this.ENCComision = ENCComision;
    }

    public void setENCNeto(String ENCNeto) {
        this.ENCNeto = ENCNeto;
    }

    public void setENCRecaudo(String ENCRecaudo) {
        this.ENCRecaudo = ENCRecaudo;
    }

    public void setENCImpuestoAsumido(String ENCImpuestoAsumido) {
        this.ENCImpuestoAsumido = ENCImpuestoAsumido;
    }

    public void setENCPuntos(String ENCPuntos) {
        this.ENCPuntos = ENCPuntos;
    }

    public void setENCEstadoLinea(String ENCEstadoLinea) {
        this.ENCEstadoLinea = ENCEstadoLinea;
    }

    public void setENCRespuesta(String ENCRespuesta) {
        this.ENCRespuesta = ENCRespuesta;
    }

    public void setENCDescRespuesta(String ENCDescRespuesta) {
        this.ENCDescRespuesta = ENCDescRespuesta;
    }

    @JsonIgnore
    public String getUSUCodigo() {
        return USUCodigo;
    }
}
    