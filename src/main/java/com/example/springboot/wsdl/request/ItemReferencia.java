package com.example.springboot.wsdl.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;


@JacksonXmlRootElement(localName = "itemReferencia")
public class ItemReferencia {

    @JacksonXmlProperty(isAttribute = true, localName = "Tiporef")
    private String tiporef;
    @JacksonXmlProperty(localName = "REFCodClasificacion")
    private String REFCodClasificacion;
    @JacksonXmlProperty(localName = "REFCodigo1")
    private String REFCodigo1;
    @JacksonXmlProperty(localName = "REFCodigo2")
    private String REFCodigo2;
    @JacksonXmlProperty(localName = "REFNombreCorto")
    private String REFNombreCorto;
    @JacksonXmlProperty(localName = "REFNombreLargo")
    private String REFNombreLargo;
    @JacksonXmlProperty(localName = "CARCodigo1")
    private String CARCodigo1;
    @JacksonXmlProperty(localName = "REFPrecioLista")
    private String REFPrecioLista;
    @JacksonXmlProperty(localName = "IRFBruto")
    private String IRFBruto;
    @JacksonXmlProperty(localName = "IRFDescuento")
    private String IRFDescuento;
    @JacksonXmlProperty(localName = "IRFPago")
    private String IRFPago;
    @JacksonXmlProperty(localName = "IRFCantidad")
    private String IRFCantidad;
    @JacksonXmlProperty(localName = "IRFValorImpuesto")
    private String IRFValorImpuesto;
    @JacksonXmlProperty(localName = "IRFImpuesto")
    private String IRFImpuesto;
    @JacksonXmlProperty(localName = "REFEsCombo")
    private String REFEsCombo;
    @JacksonXmlProperty(localName = "REFUltimoCosto")
    private String REFUltimoCosto;
    @JacksonXmlProperty(localName = "PRVCodigo")
    private String PRVCodigo;
    @JacksonXmlProperty(localName = "REFManejaLotes")
    private String REFManejaLotes;
    @JacksonXmlProperty(localName = "REFCapturaSerial")
    private String REFCapturaSerial;
    @JacksonXmlProperty(localName = "REFFactorConversion")
    private String REFFactorConversion;
    @JacksonXmlProperty(localName = "REFInventario")
    private String REFInventario;
    @JacksonXmlProperty(localName = "REFEsParaVenta")
    private String REFEsParaVenta;
    @JacksonXmlProperty(localName = "estado")
    private String estado;
    @JacksonXmlProperty(localName = "IRFPagNoVenta")
    private String IRFPagNoVenta;
    @JacksonXmlProperty(localName = "IRFVenta")
    private String IRFVenta;
    @JacksonXmlProperty(localName = "IRFValorImpuestoNeto")
    private String IRFValorImpuestoNeto;
    @JacksonXmlProperty(localName = "IRFComision")
    private String IRFComision;
    @JacksonXmlProperty(localName = "IRFNeto")
    private String IRFNeto;
    @JacksonXmlProperty(localName = "REFCodigoIngresado")
    private String REFCodigoIngresado;
    @JacksonXmlProperty(localName = "RREFPuntos")
    private String RREFPuntos;
    @JacksonXmlProperty(localName = "ICPPresentacion")
    private String ICPpresentacion;
    @JacksonXmlProperty(localName = "ICPDescripcion")
    private String ICPdescripcion;
    @JacksonXmlProperty(localName = "ICPCadena")
    private String ICPcadena;
    @JacksonXmlProperty(localName = "ICPLetra")
    private String ICPletra;

    public void setTiporef(String tiporef) {
        this.tiporef = tiporef;
    }

    public void setREFCodClasificacion(String REFCodClasificacion) {
        this.REFCodClasificacion = REFCodClasificacion;
    }

    public void setREFCodigo1(String REFCodigo1) {
        this.REFCodigo1 = REFCodigo1;
    }

    public void setREFCodigo2(String REFCodigo2) {
        this.REFCodigo2 = REFCodigo2;
    }

    public void setREFNombreCorto(String REFNombreCorto) {
        this.REFNombreCorto = REFNombreCorto;
    }

    public void setREFNombreLargo(String REFNombreLargo) {
        this.REFNombreLargo = REFNombreLargo;
    }

    public void setCARCodigo1(String CARCodigo1) {
        this.CARCodigo1 = CARCodigo1;
    }

    public void setREFPrecioLista(String REFPrecioLista) {
        this.REFPrecioLista = REFPrecioLista;
    }

    public void setIRFBruto(String IRFBruto) {
        this.IRFBruto = IRFBruto;
    }

    public void setIRFDescuento(String IRFDescuento) {
        this.IRFDescuento = IRFDescuento;
    }

    public void setIRFPago(String IRFPago) {
        this.IRFPago = IRFPago;
    }

    public void setIRFCantidad(String IRFCantidad) {
        this.IRFCantidad = IRFCantidad;
    }

    public void setIRFValorImpuesto(String IRFValorImpuesto) {
        this.IRFValorImpuesto = IRFValorImpuesto;
    }

    public void setIRFImpuesto(String IRFImpuesto) {
        this.IRFImpuesto = IRFImpuesto;
    }

    public void setREFEsCombo(String REFEsCombo) {
        this.REFEsCombo = REFEsCombo;
    }

    public void setREFUltimoCosto(String REFUltimoCosto) {
        this.REFUltimoCosto = REFUltimoCosto;
    }

    public void setPRVCodigo(String PRVCodigo) {
        this.PRVCodigo = PRVCodigo;
    }

    public void setREFManejaLotes(String REFManejaLotes) {
        this.REFManejaLotes = REFManejaLotes;
    }

    public void setREFCapturaSerial(String REFCapturaSerial) {
        this.REFCapturaSerial = REFCapturaSerial;
    }

    public void setREFFactorConversion(String REFFactorConversion) {
        this.REFFactorConversion = REFFactorConversion;
    }

    public void setREFInventario(String REFInventario) {
        this.REFInventario = REFInventario;
    }

    public void setREFEsParaVenta(String REFEsParaVenta) {
        this.REFEsParaVenta = REFEsParaVenta;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIRFPagNoVenta(String IRFPagNoVenta) {
        this.IRFPagNoVenta = IRFPagNoVenta;
    }

    public void setIRFVenta(String IRFVenta) {
        this.IRFVenta = IRFVenta;
    }

    public void setIRFValorImpuestoNeto(String IRFValorImpuestoNeto) {
        this.IRFValorImpuestoNeto = IRFValorImpuestoNeto;
    }

    public void setIRFComision(String IRFComision) {
        this.IRFComision = IRFComision;
    }

    public void setIRFNeto(String IRFNeto) {
        this.IRFNeto = IRFNeto;
    }

    public void setREFCodigoIngresado(String REFCodigoIngresado) {
        this.REFCodigoIngresado = REFCodigoIngresado;
    }

    public void setRREFPuntos(String RREFPuntos) {
        this.RREFPuntos = RREFPuntos;
    }

    public void setICPpresentacion(String ICPpresentacion) {
        this.ICPpresentacion = ICPpresentacion;
    }

    public void setICPdescripcion(String ICPdescripcion) {
        this.ICPdescripcion = ICPdescripcion;
    }

    public void setICPcadena(String ICPcadena) {
        this.ICPcadena = ICPcadena;
    }

    public void setICPletra(String ICPletra) {
        this.ICPletra = ICPletra;
    }
}