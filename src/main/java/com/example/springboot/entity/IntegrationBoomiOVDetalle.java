package com.example.springboot.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import java.io.Serializable;
@Entity
public class IntegrationBoomiOVDetalle  implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id_Detalle")
    private int id_Detalle;
    @Column(name = "id")
    private  String id ;
    @Column(name = "sod_nbr")
    private  String sod_nbr;
    @Column(name = "sod_loc")
    private  String sod_loc;
    @Column(name = "sod_ship")
    private  String sod_ship;
    @Column(name = "sod_part")
    private  String sod_part;
    @Column(name = "sod_line")
    private  Integer sod_line;
    @Column(name = "sod_desc")
    private Integer sod_desc;
    @Column(name = "sod_um")
    private String sod_um;
    @Column(name = "sod_taxable")
    private Integer sod_taxable;
    @Column(name = "sod_price")
    private Integer sod_price;
    @Column(name = "sod_precioCliente")
    private String sod_precioCliente;
    @Column(name = "sod_precioLista")
    private String sod_precioLista;
    @Column(name = "sod_qty_ord")
    private Integer sod_qty_ord;
    @Column(name = "sod_qty_all")
    private Integer sod_qty_all;
    @Column(name = "sod_qty_inv")
    private Integer sod_qty_inv;
    @Column(name = "sod_qty_ship")
    private Integer sod_qty_ship;
    @Column(name = "sod_order_category")
    private Integer sod_order_category;
    @Column(name = "xdvd_Reason")
    private String xdvd_Reason;
    @Column(name = "xdvd_Date")
    private String xdvd_Date;
    @Column(name = "pctDescuentoComercial")
    private String pctDescuentoComercial;
    @Column(name = "pctDescuentoPromocion")
    private String pctDescuentoEspecial;
    @Column(name = "fechaSolicitud")
    private String fechaSolicitud;
    @Column(name = "fechaSync")
    private String fechaSync;
    @Column(name = "usuarioIntegracion")
    private String  usuarioIntegracion;

    public IntegrationBoomiOVDetalle() {
    }

    public IntegrationBoomiOVDetalle(String sod_nbr, String sod_loc, String sod_ship, String sod_part, Integer sod_line, Integer sod_desc, String sod_um, Integer sod_taxable, Integer sod_price, String sod_precioCliente, String sod_precioLista, Integer sod_qty_ord, Integer sod_qty_all, Integer sod_qty_inv, Integer sod_qty_ship, Integer sod_order_category, String xdvd_Reason, String xdvd_Date, String pctDescuentoComercial, String pctDescuentoEspecial, String fechaSolicitud, String fechaSync, String usuarioIntegracion) {
        this.sod_nbr = sod_nbr;
        this.sod_loc = sod_loc;
        this.sod_ship = sod_ship;
        this.sod_part = sod_part;
        this.sod_line = sod_line;
        this.sod_desc = sod_desc;
        this.sod_um = sod_um;
        this.sod_taxable = sod_taxable;
        this.sod_price = sod_price;
        this.sod_precioCliente = sod_precioCliente;
        this.sod_precioLista = sod_precioLista;
        this.sod_qty_ord = sod_qty_ord;
        this.sod_qty_all = sod_qty_all;
        this.sod_qty_inv = sod_qty_inv;
        this.sod_qty_ship = sod_qty_ship;
        this.sod_order_category = sod_order_category;
        this.xdvd_Reason = xdvd_Reason;
        this.xdvd_Date = xdvd_Date;
        this.pctDescuentoComercial = pctDescuentoComercial;
        this.pctDescuentoEspecial = pctDescuentoEspecial;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaSync = fechaSync;
        this.usuarioIntegracion = usuarioIntegracion;
    }

    public int getId_Detalle() {
        return id_Detalle;
    }

    public void setId_Detalle(int id_Detalle) {
        this.id_Detalle = id_Detalle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSod_nbr() {
        return sod_nbr;
    }

    public void setSod_nbr(String sod_nbr) {
        this.sod_nbr = sod_nbr;
    }

    public String getSod_loc() {
        return sod_loc;
    }

    public void setSod_loc(String sod_loc) {
        this.sod_loc = sod_loc;
    }

    public String getSod_ship() {
        return sod_ship;
    }

    public void setSod_ship(String sod_ship) {
        this.sod_ship = sod_ship;
    }

    public String getSod_part() {
        return sod_part;
    }

    public void setSod_part(String sod_part) {
        this.sod_part = sod_part;
    }

    public Integer getSod_line() {
        return sod_line;
    }

    public void setSod_line(Integer sod_line) {
        this.sod_line = sod_line;
    }

    public Integer getSod_desc() {
        return sod_desc;
    }

    public void setSod_desc(Integer sod_desc) {
        this.sod_desc = sod_desc;
    }

    public String getSod_um() {
        return sod_um;
    }

    public void setSod_um(String sod_um) {
        this.sod_um = sod_um;
    }

    public Integer getSod_taxable() {
        return sod_taxable;
    }

    public void setSod_taxable(Integer sod_taxable) {
        this.sod_taxable = sod_taxable;
    }

    public Integer getSod_price() {
        return sod_price;
    }

    public void setSod_price(Integer sod_price) {
        this.sod_price = sod_price;
    }

    public String getSod_precioCliente() {
        return sod_precioCliente;
    }

    public void setSod_precioCliente(String sod_precioCliente) {
        this.sod_precioCliente = sod_precioCliente;
    }

    public String getSod_precioLista() {
        return sod_precioLista;
    }

    public void setSod_precioLista(String sod_precioLista) {
        this.sod_precioLista = sod_precioLista;
    }

    public Integer getSod_qty_ord() {
        return sod_qty_ord;
    }

    public void setSod_qty_ord(Integer sod_qty_ord) {
        this.sod_qty_ord = sod_qty_ord;
    }

    public Integer getSod_qty_all() {
        return sod_qty_all;
    }

    public void setSod_qty_all(Integer sod_qty_all) {
        this.sod_qty_all = sod_qty_all;
    }

    public Integer getSod_qty_inv() {
        return sod_qty_inv;
    }

    public void setSod_qty_inv(Integer sod_qty_inv) {
        this.sod_qty_inv = sod_qty_inv;
    }

    public Integer getSod_qty_ship() {
        return sod_qty_ship;
    }

    public void setSod_qty_ship(Integer sod_qty_ship) {
        this.sod_qty_ship = sod_qty_ship;
    }

    public Integer getSod_order_category() {
        return sod_order_category;
    }

    public void setSod_order_category(Integer sod_order_category) {
        this.sod_order_category = sod_order_category;
    }

    public String getXdvd_Reason() {
        return xdvd_Reason;
    }

    public void setXdvd_Reason(String xdvd_Reason) {
        this.xdvd_Reason = xdvd_Reason;
    }

    public String getXdvd_Date() {
        return xdvd_Date;
    }

    public void setXdvd_Date(String xdvd_Date) {
        this.xdvd_Date = xdvd_Date;
    }

    public String getPctDescuentoComercial() {
        return pctDescuentoComercial;
    }

    public void setPctDescuentoComercial(String pctDescuentoComercial) {
        this.pctDescuentoComercial = pctDescuentoComercial;
    }

    public String getPctDescuentoEspecial() {
        return pctDescuentoEspecial;
    }

    public void setPctDescuentoEspecial(String pctDescuentoEspecial) {
        this.pctDescuentoEspecial = pctDescuentoEspecial;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaSync() {
        return fechaSync;
    }

    public void setFechaSync(String fechaSync) {
        this.fechaSync = fechaSync;
    }

    public String getUsuarioIntegracion() {
        return usuarioIntegracion;
    }

    public void setUsuarioIntegracion(String usuarioIntegracion) {
        this.usuarioIntegracion = usuarioIntegracion;
    }


}
