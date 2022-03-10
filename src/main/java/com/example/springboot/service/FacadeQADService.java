package com.example.springboot.service;

import com.example.springboot.dto.AccessToken;
import com.example.springboot.entity.IntegracionBoomiCuentas;
import com.example.springboot.entity.IntegracionBoomiOVEncabezado;
import com.example.springboot.entity.IntegrationBoomiOVDetalle;
import com.example.springboot.enums.OrderStatus;
import com.example.springboot.repository.IntegrationBoomiCuentasRepository;
import com.example.springboot.repository.IntegrationBoomiOVDetalleRepository;
import com.example.springboot.repository.IntegrationBoomiOVEncabezadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.json.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.StringBuilder;

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
    public synchronized void ProcessOrdersAndShipping(String accessToken, String ffin)
    {
        int ordersi = 0;
        int resultados = 0;
        StringBuilder detalle_ids = new StringBuilder();
        String cuentas_ids = new String();
        String encabezado_ids = new String();
        StringBuilder errores = new StringBuilder();

        //String orders = this.qadService.dummyOrders();
        String orders = this.qadService.ordersByStateAndDate(accessToken, OrderStatus.PAID, ffin);

        JSONObject jsonObject = new JSONObject(orders);

        //System.out.println(jsonObject.get("results"));

        IntegracionBoomiOVEncabezado integracionBoomiOVEncabezado = new IntegracionBoomiOVEncabezado();        
        
        JSONArray results = new JSONArray(jsonObject.get("results").toString());
        resultados = results.length();
        for(int i = 0; i < results.length(); i++) 
        {   
            JSONObject resu = (JSONObject) results.get(i);  
            JSONArray payments = (JSONArray) resu.get("payments");
            JSONObject payment = (JSONObject) payments.get(0);

            //Solo aprovadas ("approved") y es estado "paid"
            if(!payment.get("status").toString().equalsIgnoreCase("approved") && !resu.get("status").toString().equalsIgnoreCase("paid"))
            { errores.append("No es una transaccion"); System.out.println("Status: "+resu.get("status").toString()); continue;}

            IntegracionBoomiCuentas integracionBoomiCuentas = new IntegracionBoomiCuentas();                        
            
            JSONObject buyer = (JSONObject) resu.get("buyer");
            JSONArray order_items = (JSONArray)resu.get("order_items");

            ordersi = order_items.length();

            try
            {
                JSONObject billing_info = (JSONObject) buyer.get("billing_info");
                integracionBoomiCuentas.setAd_gst_id(billing_info.get("doc_number").toString());
                integracionBoomiCuentas.setAd_misc1_id(billing_info.get("doc_type").toString());
            
            }catch(Exception ex)
            {
                errores.append("No billing info in buyer").append(" ");
            }

            try
            {
                JSONObject phone = (JSONObject) buyer.get("phone");
                integracionBoomiCuentas.setAd_phone(phone.get("number").toString());
            }catch(Exception ex)
            {
                errores.append("No phone in buyer").append(" ");
            }
            try
            {
                integracionBoomiCuentas.setFe_nam1(buyer.get("first_name").toString());
                integracionBoomiCuentas.setFe_apel1(buyer.get("last_name").toString());

            }catch(Exception ex)
            {
                errores.append("No firstname in buyer").append(" ");
            }                        
            //Un result, tiene muchos order items
            for(int j = 0; j < order_items.length() ; j++)
            {
                JSONObject or = (JSONObject) order_items.get(j);
                JSONObject item = (JSONObject) or.get("item");
                IntegrationBoomiOVDetalle integrationBoomiOVDetalle = new IntegrationBoomiOVDetalle();
                try
                {
                    integrationBoomiOVDetalle.setSod_part(item.get("seller_sku").toString());
                    errores.append("No seller_sku in item");
                }catch(Exception e)
                {
                    integrationBoomiOVDetalle.setSod_part(item.get("seller_custom_field").toString());
                }

                integrationBoomiOVDetalle.setSod_qty_inv(Integer.parseInt(or.get("quantity").toString()));
                integrationBoomiOVDetalle.setSod_price(Integer.parseInt(or.get("unit_price").toString()));

                this.integrationBoomiOVDetalleRepository.save(integrationBoomiOVDetalle);
            }                                                                                                                                                                                                                                                                                                   

            JSONObject shipping = (JSONObject) resu.get("shipping");

            detalle_ids.append("Producto: "+shipping.get("id").toString()+ " Con order_items: "+order_items.length() + " \n");            

            integracionBoomiOVEncabezado.setSo_importeTotal(resu.get("total_amount").toString());
            integracionBoomiOVEncabezado.setSo_po(shipping.get("id").toString());

            String shippingData = this.qadService.getShippingData(accessToken, shipping.get("id").toString());
            String orderBillingInfo = this.qadService.ordersBillingInfo(accessToken, resu.get("id").toString());
            String orderData = this.qadService.ordersData(accessToken, resu.get("id").toString());
            //String shippingData = this.qadService.getDummyShippingData();

            JSONObject orderDataJson = new JSONObject(orderData);
            JSONObject receiver_address_data = (JSONObject) orderDataJson.get("receiver_address");

            integracionBoomiCuentas.setAd_phone(receiver_address_data.get("receiver_phone").toString());

            JSONObject jsonOrderBilling = new JSONObject(orderBillingInfo);
            JSONObject order_billing_info = (JSONObject) jsonOrderBilling.get("billing_info");

            integracionBoomiCuentas.setAd_gst_id(order_billing_info.get("doc_number").toString());
            integracionBoomiCuentas.setAd_misc1_id(order_billing_info.get("doc_type").toString());

            JSONArray additional_info = (JSONArray) order_billing_info.get("additional_info");
            for(int k = 0; k < additional_info.length() ; k++)
            {
                JSONObject ai = (JSONObject) additional_info.get(j);
                if(ai.get("type").toString().equalsIgnoreCase("BUSINESS_NAME"))
                {
                    integracionBoomiCuentas.setFe_nam1(ai.get("value").toString());
                    integracionBoomiCuentas.setFe_apel1(ai.get("value").toString());
                    break;
                }
                
                if(ai.get("type").toString().equalsIgnoreCase("FIRST_NAME"))
                    integracionBoomiCuentas.setFe_nam1(ai.get("value").toString());
                else if(ai.get("type").toString().equalsIgnoreCase("LAST_NAME"))
                    integracionBoomiCuentas.setFe_apel1(ai.get("value").toString());

            }

            JSONObject jsonObjectShipping = new JSONObject(shippingData);         
            JSONObject shipping_address = (JSONObject) jsonObjectShipping.get("receiver_address");
            

            Object comment = shipping_address.get("comment");
            JSONObject city = (JSONObject) shipping_address.get("city");
            JSONObject state = (JSONObject) shipping_address.get("state");
            JSONObject neighborhood = (JSONObject) shipping_address.get("neighborhood");

            integracionBoomiCuentas.setAd_line1(shipping_address.get("address_line") + (comment == null ? " "+ comment.toString() : "") + " " + neighborhood.get("name").toString());
            integracionBoomiCuentas.setAd_city(city.get("name").toString());
            integracionBoomiCuentas.setAd_state(state.get("name").toString());

            //integracionBoomiCuentas.setAd_name(ad_name);
                            
            this.integrationBoomiCuentasRepository.save(integracionBoomiCuentas);     
            this.integrationBoomiOVEncabezadoRepository.save(integracionBoomiOVEncabezado);
        }

        //System.out.println(detalle_ids.toString());
        this.email("MercadoLibre ejecutado exitosamente: Numero de SKUS: "+detalle_ids.toString(), "MERCADOLIBRE EJECUCION CORRECTA: ");

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
     * @return
     * @throws Exception
     */
    public String TGTokenML() throws Exception {
        return this.qadService.getTGToken();
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
     * @param tgToken
     * @return
     * @throws Exception
     */
    public AccessToken accessTokenML(String tgToken) throws Exception {
        return this.qadService.getAccessToken(tgToken);
    }

    /**
     * 
     */
    public AccessToken refreshToken(String tgToken) throws Exception {
        return this.qadService.refreshAccessToken(tgToken);
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
