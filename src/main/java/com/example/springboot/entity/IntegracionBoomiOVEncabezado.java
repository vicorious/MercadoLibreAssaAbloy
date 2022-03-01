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
public class IntegracionBoomiOVEncabezado  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "idEncabezado")
    private Integer idEncabezado;
    @Column(name = "id")
    private String id;
    @Column(name = "so_cust")
    private String so_cust;
    @Column(name = "so_ship")
    private String so_ship;
    @Column(name = "so_bill")
    private String so_bill;
    @Column(name = "so_nbr")
    private String so__nbr;
    @Column(name = "so_type")
    private String so_type;
    @Column(name = "so_estado")
    private String so_estado;
    @Column(name = "so_SolicitudDesbloqueo")
    private String so_SolicitudDesbloqueo;
    @Column(name = "so_SolicituDesbloqueoExitoso")
    private String SolicitudDesbloqueoExitoso;
    @Column(name = "so_po")
    private String so_po;
    @Column(name = "so_cr_terms")
    private String so_cr_terms;
    @Column(name = "so_curr")
    private String so_curr;
    @Column(name = "so_importeTotal")
    private String so_importeTotal;
    @Column(name = "so_site")
    private Integer so_site;
    @Column(name = "so_ord_date")
    private String so_ord_date;
    @Column(name = "so_req_date")
    private String so_req_date;
    @Column(name = "so_due_date")
    private String so_due_date;
    @Column(name = "so_rmks")
    private String so_rmks;
    @Column(name = "so_chanel")
    private String so_chanel;
    @Column(name = "so_project")
    private String so_project;
    @Column(name = "so_tax_usage")
    private String so_tax_usage;
    @Column(name = "so_tax_env")
    private String so_tax_env;
    @Column(name = "so_taxc")
    private String so_taxc;
    @Column(name = "so_taxable")
    private Integer so_taxable;
    @Column(name = "so_ar_acct")
    private Integer so_ar_acct;
    @Column(name = "so_slspsn")
    private String so_slspsn;
    @Column(name = "so_disc_pct")
    private Integer so_disc_pst;
    @Column(name = "so_dec01")
    private Integer so_dec01;
    @Column(name = "so_shipvia")
    private String so_shipvia;
    @Column(name = "so_chr01")
    private String so_chr01;
    @Column(name = "fechaCreacion")
    private String fechaCreacion;
    @Column(name = "so_date_inv")
    private String so_date_inv;
    @Column(name = "so_ex_rate")
    private Integer so_ex_rate;
    @Column(name = "fechaSolicitud")
    private String fechaSolicitud;
    @Column(name = "fechaSync")
    private String fechaSync;
    @Column(name = "usuarioIntegracion")
    private String usuarioIntegracion;

    public IntegracionBoomiOVEncabezado() {
    }

    public IntegracionBoomiOVEncabezado(String id, String so_cust, String so_ship, String so_bill, String so__nbr, String so_type, String so_estado, String so_SolicitudDesbloqueo, String solicitudDesbloqueoExitoso, String so_po, String so_cr_terms, String so_curr, String so_importeTotal, Integer so_site, String so_ord_date, String so_req_date, String so_due_date, String so_rmks, String so_chanel, String so_project, String so_tax_usage, String so_tax_env, String so_taxc, Integer so_taxable, Integer so_ar_acct, String so_slspsn, Integer so_disc_pst, Integer so_dec01, String so_shipvia, String so_chr01, String fechaCreacion, String so_date_inv, Integer so_ex_rate, String fechaSolicitud, String fechaSync, String usuarioIntegracion) {
        this.id = id;
        this.so_cust = so_cust;
        this.so_ship = so_ship;
        this.so_bill = so_bill;
        this.so__nbr = so__nbr;
        this.so_type = so_type;
        this.so_estado = so_estado;
        this.so_SolicitudDesbloqueo = so_SolicitudDesbloqueo;
        SolicitudDesbloqueoExitoso = solicitudDesbloqueoExitoso;
        this.so_po = so_po;
        this.so_cr_terms = so_cr_terms;
        this.so_curr = so_curr;
        this.so_importeTotal = so_importeTotal;
        this.so_site = so_site;
        this.so_ord_date = so_ord_date;
        this.so_req_date = so_req_date;
        this.so_due_date = so_due_date;
        this.so_rmks = so_rmks;
        this.so_chanel = so_chanel;
        this.so_project = so_project;
        this.so_tax_usage = so_tax_usage;
        this.so_tax_env = so_tax_env;
        this.so_taxc = so_taxc;
        this.so_taxable = so_taxable;
        this.so_ar_acct = so_ar_acct;
        this.so_slspsn = so_slspsn;
        this.so_disc_pst = so_disc_pst;
        this.so_dec01 = so_dec01;
        this.so_shipvia = so_shipvia;
        this.so_chr01 = so_chr01;
        this.fechaCreacion = fechaCreacion;
        this.so_date_inv = so_date_inv;
        this.so_ex_rate = so_ex_rate;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaSync = fechaSync;
        this.usuarioIntegracion = usuarioIntegracion;
    }

    public Integer getIdEncabezado() {
        return idEncabezado;
    }

    public void setIdEncabezado(Integer idEncabezado) {
        this.idEncabezado = idEncabezado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSo_cust() {
        return so_cust;
    }

    public void setSo_cust(String so_cust) {
        this.so_cust = so_cust;
    }

    public String getSo_ship() {
        return so_ship;
    }

    public void setSo_ship(String so_ship) {
        this.so_ship = so_ship;
    }

    public String getSo_bill() {
        return so_bill;
    }

    public void setSo_bill(String so_bill) {
        this.so_bill = so_bill;
    }

    public String getSo__nbr() {
        return so__nbr;
    }

    public void setSo__nbr(String so__nbr) {
        this.so__nbr = so__nbr;
    }

    public String getSo_type() {
        return so_type;
    }

    public void setSo_type(String so_type) {
        this.so_type = so_type;
    }

    public String getSo_estado() {
        return so_estado;
    }

    public void setSo_estado(String so_estado) {
        this.so_estado = so_estado;
    }

    public String getSo_SolicitudDesbloqueo() {
        return so_SolicitudDesbloqueo;
    }

    public void setSo_SolicitudDesbloqueo(String so_SolicitudDesbloqueo) {
        this.so_SolicitudDesbloqueo = so_SolicitudDesbloqueo;
    }

    public String getSolicitudDesbloqueoExitoso() {
        return SolicitudDesbloqueoExitoso;
    }

    public void setSolicitudDesbloqueoExitoso(String solicitudDesbloqueoExitoso) {
        SolicitudDesbloqueoExitoso = solicitudDesbloqueoExitoso;
    }

    public String getSo_po() {
        return so_po;
    }

    public void setSo_po(String so_po) {
        this.so_po = so_po;
    }

    public String getSo_cr_terms() {
        return so_cr_terms;
    }

    public void setSo_cr_terms(String so_cr_terms) {
        this.so_cr_terms = so_cr_terms;
    }

    public String getSo_curr() {
        return so_curr;
    }

    public void setSo_curr(String so_curr) {
        this.so_curr = so_curr;
    }

    public String getSo_importeTotal() {
        return so_importeTotal;
    }

    public void setSo_importeTotal(String so_importeTotal) {
        this.so_importeTotal = so_importeTotal;
    }

    public Integer getSo_site() {
        return so_site;
    }

    public void setSo_site(Integer so_site) {
        this.so_site = so_site;
    }

    public String getSo_ord_date() {
        return so_ord_date;
    }

    public void setSo_ord_date(String so_ord_date) {
        this.so_ord_date = so_ord_date;
    }

    public String getSo_req_date() {
        return so_req_date;
    }

    public void setSo_req_date(String so_req_date) {
        this.so_req_date = so_req_date;
    }

    public String getSo_due_date() {
        return so_due_date;
    }

    public void setSo_due_date(String so_due_date) {
        this.so_due_date = so_due_date;
    }

    public String getSo_rmks() {
        return so_rmks;
    }

    public void setSo_rmks(String so_rmks) {
        this.so_rmks = so_rmks;
    }

    public String getSo_chanel() {
        return so_chanel;
    }

    public void setSo_chanel(String so_chanel) {
        this.so_chanel = so_chanel;
    }

    public String getSo_project() {
        return so_project;
    }

    public void setSo_project(String so_project) {
        this.so_project = so_project;
    }

    public String getSo_tax_usage() {
        return so_tax_usage;
    }

    public void setSo_tax_usage(String so_tax_usage) {
        this.so_tax_usage = so_tax_usage;
    }

    public String getSo_tax_env() {
        return so_tax_env;
    }

    public void setSo_tax_env(String so_tax_env) {
        this.so_tax_env = so_tax_env;
    }

    public String getSo_taxc() {
        return so_taxc;
    }

    public void setSo_taxc(String so_taxc) {
        this.so_taxc = so_taxc;
    }

    public Integer getSo_taxable() {
        return so_taxable;
    }

    public void setSo_taxable(Integer so_taxable) {
        this.so_taxable = so_taxable;
    }

    public Integer getSo_ar_acct() {
        return so_ar_acct;
    }

    public void setSo_ar_acct(Integer so_ar_acct) {
        this.so_ar_acct = so_ar_acct;
    }

    public String getSo_slspsn() {
        return so_slspsn;
    }

    public void setSo_slspsn(String so_slspsn) {
        this.so_slspsn = so_slspsn;
    }

    public Integer getSo_disc_pst() {
        return so_disc_pst;
    }

    public void setSo_disc_pst(Integer so_disc_pst) {
        this.so_disc_pst = so_disc_pst;
    }

    public Integer getSo_dec01() {
        return so_dec01;
    }

    public void setSo_dec01(Integer so_dec01) {
        this.so_dec01 = so_dec01;
    }

    public String getSo_shipvia() {
        return so_shipvia;
    }

    public void setSo_shipvia(String so_shipvia) {
        this.so_shipvia = so_shipvia;
    }

    public String getSo_chr01() {
        return so_chr01;
    }

    public void setSo_chr01(String so_chr01) {
        this.so_chr01 = so_chr01;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getSo_date_inv() {
        return so_date_inv;
    }

    public void setSo_date_inv(String so_date_inv) {
        this.so_date_inv = so_date_inv;
    }

    public Integer getSo_ex_rate() {
        return so_ex_rate;
    }

    public void setSo_ex_rate(Integer so_ex_rate) {
        this.so_ex_rate = so_ex_rate;
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