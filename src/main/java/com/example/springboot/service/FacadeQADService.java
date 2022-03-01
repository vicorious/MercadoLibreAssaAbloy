package com.example.springboot.service;

import com.example.springboot.dto.AccessToken;
import com.example.springboot.entity.IntegracionBoomiCuentas;
import com.example.springboot.entity.IntegracionBoomiOVEncabezado;
import com.example.springboot.entity.IntegrationBoomiOVDetalle;
import com.example.springboot.repository.IntegrationBoomiCuentasRepository;
import com.example.springboot.repository.IntegrationBoomiOVDetalleRepository;
import com.example.springboot.repository.IntegrationBoomiOVEncabezadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.json.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacadeQADService {

    @Autowired
    IntegrationBoomiCuentasRepository integrationBoomiCuentasRepository;

    @Autowired
    IntegrationBoomiOVDetalleRepository integrationBoomiOVDetalleRepository;

    @Autowired
    IntegrationBoomiOVEncabezadoRepository integrationBoomiOVEncabezadoRepository;

    @Autowired
    QADService qadService;

    /**
     * 
     */
    public FacadeQADService(){

    }

    /** 
     * 
    */
    public FacadeQADService(IntegrationBoomiCuentasRepository integrationBoomiCuentasRepository, IntegrationBoomiOVDetalleRepository integrationBoomiOVDetalleRepository, IntegrationBoomiOVEncabezadoRepository integrationBoomiOVEncabezadoRepository, QADService qadService) {
        this.integrationBoomiCuentasRepository = integrationBoomiCuentasRepository;
        this.integrationBoomiOVDetalleRepository = integrationBoomiOVDetalleRepository;
        this.integrationBoomiOVEncabezadoRepository = integrationBoomiOVEncabezadoRepository;
        this.qadService = qadService;
    }

    /**
     * 
     */
    public synchronized void ProcessOrdersAndShipping(String accessToken)
    {
        int ordersi = 0;
        String detalle_ids = new String();
        String cuentas_ids = new String();
        String encabezado_ids = new String();

        String orders = this.qadService.dummyOrders();
        //String orders = this.qadService.orders();

        JSONObject jsonObject = new JSONObject(orders);

        System.out.println(jsonObject.get("results"));

        IntegracionBoomiOVEncabezado integracionBoomiOVEncabezado = new IntegracionBoomiOVEncabezado();        
        
        JSONArray results = new JSONArray(jsonObject.get("results").toString());
        for(int i = 0; i < results.length(); i++) 
        {   
            System.out.println(i);
            JSONObject resu = (JSONObject) results.get(i);  
            System.out.println(resu);
            JSONArray payments = (JSONArray) resu.get("payments");
            System.out.println(payments);
            JSONObject payment = (JSONObject) payments.get(0);
            System.out.println(payment);

            //Solo aprovadas ("approved") y es estado "paid"
            if(!payment.get("status").toString().equalsIgnoreCase("approved") && !resu.get("status").toString().equalsIgnoreCase("paid"))
            { System.out.println("Salio"); return;}

            IntegracionBoomiCuentas integracionBoomiCuentas = new IntegracionBoomiCuentas();                        
            
            JSONObject buyer = (JSONObject) resu.get("buyer");
            JSONArray order_items = (JSONArray)resu.get("order_items");

            ordersi = order_items.length();

            JSONObject billing_info = (JSONObject) buyer.get("billing_info");
            JSONObject phone = (JSONObject) buyer.get("phone");

            integracionBoomiCuentas.setFe_nam1(buyer.get("first_name").toString());
            integracionBoomiCuentas.setFe_apel1(buyer.get("last_name").toString());
            integracionBoomiCuentas.setAd_gst_id(billing_info.get("doc_number").toString());
            integracionBoomiCuentas.setAd_misc1_id(billing_info.get("doc_type").toString());
            integracionBoomiCuentas.setAd_phone(phone.get("number").toString());

            System.out.println("111111111");

            for(int j = 0; j < order_items.length() ; j++)
            {
                System.out.println("j:"+j);
                JSONObject or = (JSONObject) order_items.get(j);
                JSONObject item = (JSONObject) or.get("item");
                System.out.println("ITEM: ");
                System.out.println(item.toString());
                IntegrationBoomiOVDetalle integrationBoomiOVDetalle = new IntegrationBoomiOVDetalle();
                try
                {
                    integrationBoomiOVDetalle.setSod_part(item.get("seller_sku").toString());
                }catch(Exception e)
                {
                    integrationBoomiOVDetalle.setSod_part(item.get("seller_custom_field").toString());
                }
                //integrationBoomiOVDetalle.setSod_part(or.get("seller_sku") == null ? or.get("seller_custom_field").toString() : or.get("seller_sku").toString());
                integrationBoomiOVDetalle.setSod_qty_inv(Integer.parseInt(or.get("quantity").toString()));
                integrationBoomiOVDetalle.setSod_price(Integer.parseInt(or.get("unit_price").toString()));

                //int detalle = (int) this.integrationBoomiOVDetalleRepository.count() + 1;
                //integrationBoomiOVDetalle.setId_Detalle(detalle);
                IntegrationBoomiOVDetalle boomiOVDetalle = this.integrationBoomiOVDetalleRepository.save(integrationBoomiOVDetalle);
                detalle_ids.concat(boomiOVDetalle.getId_Detalle() + "");
            }

            JSONObject shipping = (JSONObject) resu.get("shipping");

            integracionBoomiOVEncabezado.setSo_importeTotal(resu.get("total_amount").toString());
            integracionBoomiOVEncabezado.setSo_po(shipping.get("id").toString());

            //this.qadService.getShippingData(accessToken, resu.get("shipping").get("id"));
            String shippingData = this.qadService.getDummyShippingData();

            JSONObject jsonObjectShipping = new JSONObject(shippingData);
            JSONObject destination = (JSONObject) jsonObjectShipping.get("destination");
            JSONObject shipping_address = (JSONObject) destination.get("shipping_address");

            Object comment = shipping_address.get("comment");
            JSONObject city = (JSONObject) shipping_address.get("city");
            JSONObject state = (JSONObject) shipping_address.get("state");
            JSONObject neighborhood = (JSONObject) shipping_address.get("neighborhood");

            integracionBoomiCuentas.setAd_line1(shipping_address.get("address_line") + (comment == null ? " "+ comment.toString() : "") + " " + neighborhood.get("name").toString());
            integracionBoomiCuentas.setAd_city(city.get("name").toString());
            integracionBoomiCuentas.setAd_state(state.get("name").toString());
                            
            int cuenta = (int) this.integrationBoomiCuentasRepository.count() + 1;
            integracionBoomiCuentas.setId_Cuenta(cuenta);
            IntegracionBoomiCuentas boomiCuentas = this.integrationBoomiCuentasRepository.save(integracionBoomiCuentas);     
            cuentas_ids.concat(boomiCuentas.getId_Cuenta() + "");
        }

        int encabezado = (int) this.integrationBoomiOVEncabezadoRepository.count() + 1;
        integracionBoomiOVEncabezado.setIdEncabezado(encabezado);
        IntegracionBoomiOVEncabezado boomiOVEncabezado = this.integrationBoomiOVEncabezadoRepository.save(integracionBoomiOVEncabezado);

        encabezado_ids.concat(boomiOVEncabezado.getIdEncabezado() + "");

        this.email("MercadoLibre ejecutado exitosamente: Numero de SKUS: "+ordersi, "MERCADOLIBRE EJECUCION CORRECTA: ");

    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String TGToken() throws Exception {
        return this.qadService.getDummyTGToken();
    }

    /**
     *
     * @param tgToken
     * @return
     * @throws Exception
     */
    public AccessToken accessToken(String tgToken) throws Exception {
        return this.qadService.dummyAccessToken(tgToken);
    }

    /**
     *
     * @param content
     * @param subject
     */
    public void email(String content, String subject) {
        this.qadService.email(content, subject);
    }




}
