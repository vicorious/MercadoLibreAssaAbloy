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
@Entity(name="DefinitivasCuentas")
public class DefinitivasCuentas implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_Cuenta")
    private Integer id_Cuenta;
    @Column(name = "id_Cliente")
    private String id_Cliente;
    @Column(name = "ad_addr")
    private Integer ad_addr;
    @Column(name = "ad_name")
    private String ad_name;
    @Column(name = "fe_apel1")
    private String fe_apel1;
    @Column(name = "fe_apel2")
    private String getFe_apel2;
    @Column(name = "fe_nam1")
    private String fe_nam1;
    @Column(name = "fe_nam2")
    private String fe_nam2;
    @Column(name = "ad_line1")
    private String ad_line1;
    @Column(name = "ad_city")
    private Integer ad_city;
    @Column(name = "ad_state")
    private Integer ad_state;
    @Column(name = "ad_ctry")
    private Integer ad_ctry;
    @Column(name = "ad_country")
    private Integer ad_country;
    @Column(name = "ad_zip")
    private Integer ad_zip;
    @Column(name = "ad_format")
    private Integer  ad_format;
    @Column(name = "ad_attn")
    private String ad_attn;
    @Column(name = "ad_phone")
    private String ad_phone;
    @Column(name = "ad_date")
    private String ad_date;
    @Column(name = "ad_mod_date")
    private String ad_mod_date;
    @Column(name = "ad_sort")
    private String ad_sort;
    @Column(name = "cm_slspsn")
    private Integer cm_slspsn;
    @Column(name = "cm_ shipvia")
    private Integer cm_shipvia;
    @Column(name = "cm_ar_acct")
    private String cm_ar_acct;
    @Column(name = "cm_ar_sub")
    private String cm_ar_sub;
    @Column(name = "so_schannel")
    private String so_schannel;
    @Column(name = "cm_rmks")
    private String cm_rmks;
    @Column(name = "cm_type")
    private Integer cm_type;
    @Column(name = "cm_curr")
    private String cm_curr;
    @Column(name = "cm_site")
    private String cm_site;
    @Column(name = "cm_class")
    private Integer cm_class;
    @Column(name = "cm_sic")
    private String cm_sic;
    @Column(name = "ad_taxable")
    private Integer ad_taxable;
    @Column(name = "ad_tax_zone")
    private String ad_tax_zone;
    @Column(name = "ad_taxc")
    private String ad_taxc;
    @Column(name = "ad_tax_usage")
    private String ad_tax_usage;
    @Column(name = "ad_tax_in")
    private Integer ad_tax_in;
    @Column(name = "ad_gst_id")
    private String ad_gst_id;
    @Column(name = "ad_pst_id")
    private Integer ad_pst_id;
    @Column(name = "ad_misc1_id")
    private String ad_misc1_id;
    @Column(name = "cm_cr_limit")
    private String cm_cr_limit;
    @Column(name = "cm_cr_terms")
    private String cm_cr_terms;
    @Column(name = "cm_cr_hold")
    private Integer cm_cr_hold;
    @Column(name = "cm_bill")
    private String cm_bill;
    @Column(name = "cm_cr_update")
    private String cm_cr_update;
    @Column(name = "cm_cr_review")
    private String cm_cr_review;
    @Column(name = "cm_fr_terms")
    private String cm_fr_terms;
    @Column(name = "fe_crt_type")
    private Integer fe_crt_type;
    @Column(name = "fe_prs_type")
    private Integer fe_prs_type;
    @Column(name = "fe_exp_country")
    private Integer fe_exp_country;
    @Column(name = "fe_email1")
    private String fe_email1;
    @Column(name = "fe_email2")
    private String fe_email2;
    @Column(name = "fe_response")
    private String fe_response;
    @Column(name = "cm_chr06")
    private Integer cm_chr06;
    @Column(name = "cm_user1")
    private String cm_user1;
    @Column(name = "cm_user2")
    private String cm_user2;
    @Column(name = "cm_qadc01")
    private Integer cm_qadc01;
    @Column(name = "cm_qadc02")
    private Integer cm_qadc02;
    @Column(name = "cm_qadc07")
    private Integer cm_qadc07;
    @Column(name = "ad_type")
    private String ad_type;
    @Column(name = "ad_chr05")
    private String ad_chr05;
    @Column(name = "ad_ref")
    private String ad_ref;
    @Column(name = "pd_canal")
    private String pd_canal;
    @Column(name = "pd_subCanal")
    private String pd_subCanal;
    @Column(name = "cm_balance")
    private Integer cm_balance;
    @Column(name = "fechaSolicitud")
    private String fechaSolicitud;
    @Column(name = "fechaSync")
    private String fechaSync;
    @Column(name = "usuarioIntegracion")
    private String usuarioIntegracion;

    public DefinitivasCuentas() {
    }

    public DefinitivasCuentas(String id_Cliente, Integer ad_addr, String ad_name, String fe_apel1, String getFe_apel2, String fe_nam1, String fe_nam2, String ad_line1, Integer ad_city, Integer ad_state, Integer ad_ctry, Integer ad_country, Integer ad_zip, Integer ad_format, String ad_attn, String ad_phone, String ad_date, String ad_mod_date, String ad_sort, Integer cm_slspsn, Integer cm_shipvia, String cm_ar_acct, String cm_ar_sub, String so_schannel, String cm_rmks, Integer cm_type, String cm_curr, String cm_site, Integer cm_class, String cm_sic, Integer ad_taxable, String ad_tax_zone, String ad_taxc, String ad_tax_usage, Integer ad_tax_in, String ad_gst_id, Integer ad_pst_id, String ad_misc1_id, String cm_cr_limit, String cm_cr_terms, Integer cm_cr_hold, String cm_bill, String cm_cr_update, String cm_cr_review, String cm_fr_terms, Integer fe_crt_type, Integer fe_prs_type, Integer fe_exp_country, String fe_email1, String fe_email2, String fe_response, Integer cm_chr06, String cm_user1, String cm_user2, Integer cm_qadc01, Integer cm_qadc02, Integer cm_qadc07, String ad_type, String ad_chr05, String ad_ref, String pd_canal, String pd_subCanal, Integer cm_balance, String fechaSolicitud, String fechaSync, String usuarioIntegracion) {
        this.id_Cliente = id_Cliente;
        this.ad_addr = ad_addr;
        this.ad_name = ad_name;
        this.fe_apel1 = fe_apel1;
        this.getFe_apel2 = getFe_apel2;
        this.fe_nam1 = fe_nam1;
        this.fe_nam2 = fe_nam2;
        this.ad_line1 = ad_line1;
        this.ad_city = ad_city;
        this.ad_state = ad_state;
        this.ad_ctry = ad_ctry;
        this.ad_country = ad_country;
        this.ad_zip = ad_zip;
        this.ad_format = ad_format;
        this.ad_attn = ad_attn;
        this.ad_phone = ad_phone;
        this.ad_date = ad_date;
        this.ad_mod_date = ad_mod_date;
        this.ad_sort = ad_sort;
        this.cm_slspsn = cm_slspsn;
        this.cm_shipvia = cm_shipvia;
        this.cm_ar_acct = cm_ar_acct;
        this.cm_ar_sub = cm_ar_sub;
        this.so_schannel = so_schannel;
        this.cm_rmks = cm_rmks;
        this.cm_type = cm_type;
        this.cm_curr = cm_curr;
        this.cm_site = cm_site;
        this.cm_class = cm_class;
        this.cm_sic = cm_sic;
        this.ad_taxable = ad_taxable;
        this.ad_tax_zone = ad_tax_zone;
        this.ad_taxc = ad_taxc;
        this.ad_tax_usage = ad_tax_usage;
        this.ad_tax_in = ad_tax_in;
        this.ad_gst_id = ad_gst_id;
        this.ad_pst_id = ad_pst_id;
        this.ad_misc1_id = ad_misc1_id;
        this.cm_cr_limit = cm_cr_limit;
        this.cm_cr_terms = cm_cr_terms;
        this.cm_cr_hold = cm_cr_hold;
        this.cm_bill = cm_bill;
        this.cm_cr_update = cm_cr_update;
        this.cm_cr_review = cm_cr_review;
        this.cm_fr_terms = cm_fr_terms;
        this.fe_crt_type = fe_crt_type;
        this.fe_prs_type = fe_prs_type;
        this.fe_exp_country = fe_exp_country;
        this.fe_email1 = fe_email1;
        this.fe_email2 = fe_email2;
        this.fe_response = fe_response;
        this.cm_chr06 = cm_chr06;
        this.cm_user1 = cm_user1;
        this.cm_user2 = cm_user2;
        this.cm_qadc01 = cm_qadc01;
        this.cm_qadc02 = cm_qadc02;
        this.cm_qadc07 = cm_qadc07;
        this.ad_type = ad_type;
        this.ad_chr05 = ad_chr05;
        this.ad_ref = ad_ref;
        this.pd_canal = pd_canal;
        this.pd_subCanal = pd_subCanal;
        this.cm_balance = cm_balance;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaSync = fechaSync;
        this.usuarioIntegracion = usuarioIntegracion;
    }

    public String getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(String id_Cliente) {
        this.id_Cliente = id_Cliente;
    }

    public Integer getAd_addr() {
        return ad_addr;
    }

    public void setAd_addr(Integer ad_addr) {
        this.ad_addr = ad_addr;
    }

    public String getAd_name() {
        return ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getFe_apel1() {
        return fe_apel1;
    }

    public void setFe_apel1(String fe_apel1) {
        this.fe_apel1 = fe_apel1;
    }

    public String getGetFe_apel2() {
        return getFe_apel2;
    }

    public void setGetFe_apel2(String getFe_apel2) {
        this.getFe_apel2 = getFe_apel2;
    }

    public String getFe_nam1() {
        return fe_nam1;
    }

    public void setFe_nam1(String fe_nam1) {
        this.fe_nam1 = fe_nam1;
    }

    public String getFe_nam2() {
        return fe_nam2;
    }

    public void setFe_nam2(String fe_nam2) {
        this.fe_nam2 = fe_nam2;
    }

    public String getAd_line1() {
        return ad_line1;
    }

    public void setAd_line1(String ad_line1) {
        this.ad_line1 = ad_line1;
    }

    public Integer getAd_city() {
        return ad_city;
    }

    public void setAd_city(Integer ad_city) {
        this.ad_city = ad_city;
    }

    public Integer getAd_state() {
        return ad_state;
    }

    public void setAd_state(Integer ad_state) {
        this.ad_state = ad_state;
    }

    public Integer getAd_ctry() {
        return ad_ctry;
    }

    public void setAd_ctry(Integer ad_ctry) {
        this.ad_ctry = ad_ctry;
    }

    public Integer getAd_country() {
        return ad_country;
    }

    public void setAd_country(Integer ad_country) {
        this.ad_country = ad_country;
    }

    public Integer getAd_zip() {
        return ad_zip;
    }

    public void setAd_zip(Integer ad_zip) {
        this.ad_zip = ad_zip;
    }

    public Integer getAd_format() {
        return ad_format;
    }

    public void setAd_format(Integer ad_format) {
        this.ad_format = ad_format;
    }

    public String getAd_attn() {
        return ad_attn;
    }

    public void setAd_attn(String ad_attn) {
        this.ad_attn = ad_attn;
    }

    public String getAd_phone() {
        return ad_phone;
    }

    public void setAd_phone(String ad_phone) {
        this.ad_phone = ad_phone;
    }

    public String getAd_date() {
        return ad_date;
    }

    public void setAd_date(String ad_date) {
        this.ad_date = ad_date;
    }

    public String getAd_mod_date() {
        return ad_mod_date;
    }

    public void setAd_mod_date(String ad_mod_date) {
        this.ad_mod_date = ad_mod_date;
    }

    public String getAd_sort() {
        return ad_sort;
    }

    public void setAd_sort(String ad_sort) {
        this.ad_sort = ad_sort;
    }

    public Integer getCm_slspsn() {
        return cm_slspsn;
    }

    public void setCm_slspsn(Integer cm_slspsn) {
        this.cm_slspsn = cm_slspsn;
    }

    public Integer getCm_shipvia() {
        return cm_shipvia;
    }

    public void setCm_shipvia(Integer cm_shipvia) {
        this.cm_shipvia = cm_shipvia;
    }

    public String getCm_ar_acct() {
        return cm_ar_acct;
    }

    public void setCm_ar_acct(String cm_ar_acct) {
        this.cm_ar_acct = cm_ar_acct;
    }

    public String getCm_ar_sub() {
        return cm_ar_sub;
    }

    public void setCm_ar_sub(String cm_ar_sub) {
        this.cm_ar_sub = cm_ar_sub;
    }

    public String getSo_schannel() {
        return so_schannel;
    }

    public void setSo_schannel(String so_schannel) {
        this.so_schannel = so_schannel;
    }

    public String getCm_rmks() {
        return cm_rmks;
    }

    public void setCm_rmks(String cm_rmks) {
        this.cm_rmks = cm_rmks;
    }

    public Integer getCm_type() {
        return cm_type;
    }

    public void setCm_type(Integer cm_type) {
        this.cm_type = cm_type;
    }

    public String getCm_curr() {
        return cm_curr;
    }

    public void setCm_curr(String cm_curr) {
        this.cm_curr = cm_curr;
    }

    public String getCm_site() {
        return cm_site;
    }

    public void setCm_site(String cm_site) {
        this.cm_site = cm_site;
    }

    public Integer getCm_class() {
        return cm_class;
    }

    public void setCm_class(Integer cm_class) {
        this.cm_class = cm_class;
    }

    public String getCm_sic() {
        return cm_sic;
    }

    public void setCm_sic(String cm_sic) {
        this.cm_sic = cm_sic;
    }

    public Integer getAd_taxable() {
        return ad_taxable;
    }

    public void setAd_taxable(Integer ad_taxable) {
        this.ad_taxable = ad_taxable;
    }

    public String getAd_tax_zone() {
        return ad_tax_zone;
    }

    public void setAd_tax_zone(String ad_tax_zone) {
        this.ad_tax_zone = ad_tax_zone;
    }

    public String getAd_taxc() {
        return ad_taxc;
    }

    public void setAd_taxc(String ad_taxc) {
        this.ad_taxc = ad_taxc;
    }

    public String getAd_tax_usage() {
        return ad_tax_usage;
    }

    public void setAd_tax_usage(String ad_tax_usage) {
        this.ad_tax_usage = ad_tax_usage;
    }

    public Integer getAd_tax_in() {
        return ad_tax_in;
    }

    public void setAd_tax_in(Integer ad_tax_in) {
        this.ad_tax_in = ad_tax_in;
    }

    public String getAd_gst_id() {
        return ad_gst_id;
    }

    public void setAd_gst_id(String ad_gst_id) {
        this.ad_gst_id = ad_gst_id;
    }

    public Integer getAd_pst_id() {
        return ad_pst_id;
    }

    public void setAd_pst_id(Integer ad_pst_id) {
        this.ad_pst_id = ad_pst_id;
    }

    public String getAd_misc1_id() {
        return ad_misc1_id;
    }

    public void setAd_misc1_id(String ad_misc1_id) {
        this.ad_misc1_id = ad_misc1_id;
    }

    public String getCm_cr_limit() {
        return cm_cr_limit;
    }

    public void setCm_cr_limit(String cm_cr_limit) {
        this.cm_cr_limit = cm_cr_limit;
    }

    public String getCm_cr_terms() {
        return cm_cr_terms;
    }

    public void setCm_cr_terms(String cm_cr_terms) {
        this.cm_cr_terms = cm_cr_terms;
    }

    public Integer getCm_cr_hold() {
        return cm_cr_hold;
    }

    public void setCm_cr_hold(Integer cm_cr_hold) {
        this.cm_cr_hold = cm_cr_hold;
    }

    public String getCm_bill() {
        return cm_bill;
    }

    public void setCm_bill(String cm_bill) {
        this.cm_bill = cm_bill;
    }

    public String getCm_cr_update() {
        return cm_cr_update;
    }

    public void setCm_cr_update(String cm_cr_update) {
        this.cm_cr_update = cm_cr_update;
    }

    public String getCm_cr_review() {
        return cm_cr_review;
    }

    public void setCm_cr_review(String cm_cr_review) {
        this.cm_cr_review = cm_cr_review;
    }

    public String getCm_fr_terms() {
        return cm_fr_terms;
    }

    public void setCm_fr_terms(String cm_fr_terms) {
        this.cm_fr_terms = cm_fr_terms;
    }

    public Integer getFe_crt_type() {
        return fe_crt_type;
    }

    public void setFe_crt_type(Integer fe_crt_type) {
        this.fe_crt_type = fe_crt_type;
    }

    public Integer getFe_prs_type() {
        return fe_prs_type;
    }

    public void setFe_prs_type(Integer fe_prs_type) {
        this.fe_prs_type = fe_prs_type;
    }

    public Integer getFe_exp_country() {
        return fe_exp_country;
    }

    public void setFe_exp_country(Integer fe_exp_country) {
        this.fe_exp_country = fe_exp_country;
    }

    public String getFe_email1() {
        return fe_email1;
    }

    public void setFe_email1(String fe_email1) {
        this.fe_email1 = fe_email1;
    }

    public String getFe_email2() {
        return fe_email2;
    }

    public void setFe_email2(String fe_email2) {
        this.fe_email2 = fe_email2;
    }

    public String getFe_response() {
        return fe_response;
    }

    public void setFe_response(String fe_response) {
        this.fe_response = fe_response;
    }

    public Integer getCm_chr06() {
        return cm_chr06;
    }

    public void setCm_chr06(Integer cm_chr06) {
        this.cm_chr06 = cm_chr06;
    }

    public String getCm_user1() {
        return cm_user1;
    }

    public void setCm_user1(String cm_user1) {
        this.cm_user1 = cm_user1;
    }

    public String getCm_user2() {
        return cm_user2;
    }

    public void setCm_user2(String cm_user2) {
        this.cm_user2 = cm_user2;
    }

    public Integer getCm_qadc01() {
        return cm_qadc01;
    }

    public void setCm_qadc01(Integer cm_qadc01) {
        this.cm_qadc01 = cm_qadc01;
    }

    public Integer getCm_qadc02() {
        return cm_qadc02;
    }

    public void setCm_qadc02(Integer cm_qadc02) {
        this.cm_qadc02 = cm_qadc02;
    }

    public Integer getCm_qadc07() {
        return cm_qadc07;
    }

    public void setCm_qadc07(Integer cm_qadc07) {
        this.cm_qadc07 = cm_qadc07;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getAd_chr05() {
        return ad_chr05;
    }

    public void setAd_chr05(String ad_chr05) {
        this.ad_chr05 = ad_chr05;
    }

    public String getAd_ref() {
        return ad_ref;
    }

    public void setAd_ref(String ad_ref) {
        this.ad_ref = ad_ref;
    }

    public String getPd_canal() {
        return pd_canal;
    }

    public void setPd_canal(String pd_canal) {
        this.pd_canal = pd_canal;
    }

    public String getPd_subCanal() {
        return pd_subCanal;
    }

    public void setPd_subCanal(String pd_subCanal) {
        this.pd_subCanal = pd_subCanal;
    }

    public Integer getCm_balance() {
        return cm_balance;
    }

    public void setCm_balance(Integer cm_balance) {
        this.cm_balance = cm_balance;
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
