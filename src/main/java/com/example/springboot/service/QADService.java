package com.example.springboot.service;

import com.example.springboot.dto.AccessToken;
import com.example.springboot.email.EmailBody;
import com.example.springboot.enums.OrderStatus;
import com.example.springboot.utils.Constantes;
import com.sun.net.httpserver.HttpContext;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.springboot.exception.RefreshTokenException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.FileWriter; 

import java.net.DatagramSocket;
import java.net.InetAddress;



@Component
@Service
public class QADService
{

    String emailURI = "http://localhost:8081/comerssia/notificaciones";


    org.slf4j.Logger logger = LoggerFactory.getLogger(QADService.class);

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public String getTGToken() throws Exception {

        logger.info("getTGToken");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> params = new HashMap<>();
        params.put("response_type", Constantes.RESPONSE_TYPE);
        params.put("client_id", Constantes.APP_ID);
        params.put("redirect_uri", Constantes.REDIRECT_URI);

        //logger.info("Parametros: "+params.toString());

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_TG_TOKEN;

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        HttpClient httpClient =
                HttpClientBuilder.create()
                        .setRedirectStrategy(new LaxRedirectStrategy())
                        .build();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        factory.setHttpClient(httpClient);

        template.setRequestFactory(factory);
        
        ResponseEntity<String> response = template.exchange(
                url, HttpMethod.GET, requestEntity, String.class, params);

        HttpHeaders headersResponse = response.getHeaders();
        headersResponse.forEach((key, value) -> {
            logger.info(String.format("Header '%s' = %s", key, value));
        });        

        //logger.info("Response: "+response.getBody());
        String location = response.getHeaders().getLocation() == null ? "" : response.getHeaders().getLocation().toString();
        logger.info("Location: "+location);


        return response.getBody();

    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String getDummyTGToken() throws Exception {
        return "TG-621f9cbadcd27d001bf8de73-31050589";

    }

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public AccessToken getAccessToken(String TGCode) throws RefreshTokenException {
        try
        {
            logger.info("getAccessToken:");
            logger.info("TGToken: "+TGCode);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("grant_type", Constantes.GRANT_TYPE);
            map.add("client_id", Constantes.APP_ID);
            map.add("client_secret", Constantes.CLIENT_SECRET);
            map.add("code", TGCode);
            map.add("redirect_uri", Constantes.REDIRECT_URI);

            //logger.info("Parametros: "+map.toString());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            RestTemplate template = new RestTemplate();
            String url = Constantes.URL_ACCESS_TOKEN;

            return template.postForObject(url, request, AccessToken.class);
        }catch(Exception ex)
        {
            ex.printStackTrace();
            logger.info("SE INTENTARA REFRESCAR EL TOKEN");
            //this.email(ex.getMessage() + "<H1>SE INTENTARA CON EL REFRESHTOKEN</H1>", "Error en access token");
            return this.refreshAccessToken(TGCode);
        }

    }

    /**
     *
     * @return
     */
    public AccessToken dummyAccessToken(String tgtoken){
        return new AccessToken("APP_USR-123456-090515-8cc4448aac10d5105474e1351-1234567",
                "bearer", 10800, "offline_access read write", 1234567, tgtoken);
    }

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public AccessToken refreshAccessToken(String previousTGToken) throws RefreshTokenException {
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("grant_type", Constantes.GRANT_REFRESH_TYPE);
            map.add("client_id", Constantes.APP_ID);
            map.add("client_secret", Constantes.CLIENT_SECRET);
            map.add("refresh_token", previousTGToken);

            String ip = new String();
            String name = new String();

            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
              }
            
            Path myPath = Paths.get(Constantes.FILE_TG);

            if (Files.exists(myPath)) {
                FileWriter myWriter = new FileWriter(Constantes.FILE_TG);
                myWriter.write(previousTGToken.concat(" ip: ".concat(ip)));                
                myWriter.close();
                logger.info(Constantes.FILE_TG);
            } else {            
                Files.createFile(myPath);
                FileWriter myWriter = new FileWriter(Constantes.FILE_TG);
                myWriter.write(previousTGToken.concat(" ip: ".concat(ip)));
                myWriter.close();
                logger.info(Constantes.FILE_TG);
            }

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            RestTemplate template = new RestTemplate();
            String url = Constantes.URL_ACCESS_TOKEN;

            return template.postForObject(url, request, AccessToken.class);
        }catch(Exception ex){
            this.email(ex.getMessage() , "Error en RefreshToken");
            throw new RefreshTokenException(ex.getMessage());
        }

    }

    /**
     *
     * @param accessToken
     * @return
     */
    public String orders(String accessToken){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&access_token="+accessToken;

        return template.getForEntity(url, String.class).getBody();
    }

    public String dummyOrders(){
        
String myvar = "{"+
"    \"query\": \"\","+
"    \"results\": ["+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja Fuerte Digital PequeÃ±a Yale EstÃ¡ndar - EconÃ³mica\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 212900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 212900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-16T13:52:42.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"153709\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20245567041,"+
"                    \"date_last_modified\": \"2022-02-19T13:05:31.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 212900,"+
"                    \"date_created\": \"2022-02-16T13:52:39.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 8874123957,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"551\","+
"                    \"payment_method_id\": \"visa\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 146501701,"+
"                    \"order_id\": 5267426494,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-09T13:52:43.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41186698161"+
"            },"+
"            \"date_closed\": \"2022-02-16T13:52:43.000-04:00\","+
"            \"id\": 5267426494,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO585630568\","+
"                        \"title\": \"Caja Fuerte Digital PequeÃ±a Yale EstÃ¡ndar - EconÃ³mica\","+
"                        \"category_id\": \"MCO177806\","+
"                        \"variation_id\": 65210327634,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": ["+
"                            {"+
"                                \"name\": \"Color\","+
"                                \"id\": \"COLOR\","+
"                                \"value_id\": \"52049\","+
"                                \"value_name\": \"Negro\""+
"                            }"+
"                        ],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0035136\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 212900,"+
"                    \"full_unit_price\": 212900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 29806,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-02-19T17:07:36.109Z\","+
"            \"last_updated\": \"2022-02-19T13:07:35.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-16T13:52:38.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 146501701,"+
"                \"nickname\": \"PAAN9634804\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 212900,"+
"            \"paid_amount\": 212900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja De Efectivo Mediana - Cash Box - Caja Menor\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 58900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 58900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-16T21:53:49.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"205349\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20256986872,"+
"                    \"date_last_modified\": \"2022-02-23T22:50:25.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 58900,"+
"                    \"date_created\": \"2022-02-16T21:53:47.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 9041630988,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"204\","+
"                    \"payment_method_id\": \"debmaster\","+
"                    \"payment_type\": \"debit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 119595591,"+
"                    \"order_id\": 5268762230,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-09T21:53:49.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41187854003"+
"            },"+
"            \"date_closed\": \"2022-02-16T21:53:49.000-04:00\","+
"            \"id\": 5268762230,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO561857620\","+
"                        \"title\": \"Caja De Efectivo Mediana - Cash Box - Caja Menor\","+
"                        \"category_id\": \"MCO177805\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0011292\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 58900,"+
"                    \"full_unit_price\": 58900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 9246,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-02-24T02:52:30.875Z\","+
"            \"last_updated\": \"2022-02-23T22:52:30.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-16T21:53:46.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 119595591,"+
"                \"nickname\": \"GALÃ‰NICA\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 58900,"+
"            \"paid_amount\": 58900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja De Seguridad Yale Para Llaves Con CombinaciÃ³n\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 122900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 122900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-16T22:10:42.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"096340\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20257243344,"+
"                    \"date_last_modified\": \"2022-02-19T13:57:47.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 122900,"+
"                    \"date_created\": \"2022-02-16T22:10:37.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 9001976666,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"205\","+
"                    \"payment_method_id\": \"visa\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 188411509,"+
"                    \"order_id\": 5268803057,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-09T22:10:42.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41187887496"+
"            },"+
"            \"date_closed\": \"2022-02-16T22:10:42.000-04:00\","+
"            \"id\": 5268803057,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO585665769\","+
"                        \"title\": \"Caja De Seguridad Yale Para Llaves Con CombinaciÃ³n\","+
"                        \"category_id\": \"MCO172798\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0016144\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 122900,"+
"                    \"full_unit_price\": 122900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 17206,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-02-19T17:59:53.606Z\","+
"            \"last_updated\": \"2022-02-19T13:59:53.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-16T22:10:37.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 188411509,"+
"                \"nickname\": \"HCTORLEONARDOMILLNVEGA\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 122900,"+
"            \"paid_amount\": 122900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja Fuerte Digital PequeÃ±a\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 262900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 262900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-20T13:08:11.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"T08338\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20336707359,"+
"                    \"date_last_modified\": \"2022-02-25T12:48:14.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 262900,"+
"                    \"date_created\": \"2022-02-20T13:08:09.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": null,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"551\","+
"                    \"payment_method_id\": \"master\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 17436089,"+
"                    \"order_id\": 5276712230,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-13T13:08:11.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41194717790"+
"            },"+
"            \"date_closed\": \"2022-02-20T13:08:11.000-04:00\","+
"            \"id\": 5276712230,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO457285928\","+
"                        \"title\": \"Caja Fuerte Digital PequeÃ±a\","+
"                        \"category_id\": \"MCO177806\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": \"0035102\","+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de 1 aÃ±o a partir de la fecha de compra. La instalaciÃ³n debe ser realizada por un tÃ©cnico Yale certificado.\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": null"+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 262900,"+
"                    \"full_unit_price\": 262900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 36806,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-02-25T16:50:48.127Z\","+
"            \"last_updated\": \"2022-02-25T12:50:20.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-20T13:08:08.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 17436089,"+
"                \"nickname\": \"JUCAR87\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 262900,"+
"            \"paid_amount\": 262900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja De Efectivo Grande - Cash Box - Caja Menor\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 70900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 70900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-22T13:23:39.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"421701\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20380318063,"+
"                    \"date_last_modified\": \"2022-02-28T11:41:31.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 70900,"+
"                    \"date_created\": \"2022-02-22T13:23:36.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 9032572682,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"12475\","+
"                    \"payment_method_id\": \"visa\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 188732854,"+
"                    \"order_id\": 5282276103,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-15T13:23:40.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41199517351"+
"            },"+
"            \"date_closed\": \"2022-02-22T13:23:40.000-04:00\","+
"            \"id\": 5282276103,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO558873656\","+
"                        \"title\": \"Caja De Efectivo Grande - Cash Box - Caja Menor\","+
"                        \"category_id\": \"MCO177805\","+
"                        \"variation_id\": 54065926425,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": ["+
"                            {"+
"                                \"name\": \"Color\","+
"                                \"id\": \"COLOR\","+
"                                \"value_id\": null,"+
"                                \"value_name\": \"Negro\""+
"                            }"+
"                        ],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0011293\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 70900,"+
"                    \"full_unit_price\": 70900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 9926,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-02-28T15:43:37.371Z\","+
"            \"last_updated\": \"2022-02-28T11:43:36.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-22T13:23:35.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 188732854,"+
"                \"nickname\": \"MORALESDUVERNEY\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 70900,"+
"            \"paid_amount\": 70900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja Fuerte Digital PequeÃ±a\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 262900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 262900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": null,"+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": null,"+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20392015270,"+
"                    \"date_last_modified\": \"2022-02-23T13:50:26.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": null,"+
"                    \"date_created\": \"2022-02-22T22:00:51.000-04:00\","+
"                    \"activation_uri\": \"https://www.mercadolibre.com.co/payments/20392015270/ticket?caller_id=133919085&payment_method_id=efecty&payment_id=20392015270&payment_method_reference_id=9933505607&hash=12ffbf14-2a69-4c4a-9134-455cba823812\","+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": null,"+
"                    \"status_detail\": \"by_payer\","+
"                    \"issuer_id\": null,"+
"                    \"payment_method_id\": \"efecty\","+
"                    \"payment_type\": \"ticket\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": \"9933505607\","+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 133919085,"+
"                    \"order_id\": 5283717867,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"cancelled\","+
"                    \"transaction_order_id\": null"+
"                },"+
"                {"+
"                    \"reason\": \"Caja Fuerte Digital PequeÃ±a\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 262900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 262900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-23T13:53:20.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": null,"+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20403807370,"+
"                    \"date_last_modified\": \"2022-02-28T18:15:10.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": null,"+
"                    \"date_created\": \"2022-02-23T13:50:26.000-04:00\","+
"                    \"activation_uri\": \"https://www.mercadopago.com/mco/payments/bank_transfer/helper?payment_id=20403807370&caller_id=133919085&hash=a0814888-b0e8-43a4-b873-c2a599a3c89b\","+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": null,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": null,"+
"                    \"payment_method_id\": \"pse\","+
"                    \"payment_type\": \"bank_transfer\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": \"1507\""+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 133919085,"+
"                    \"order_id\": 5283717867,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-16T13:55:22.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41200775131"+
"            },"+
"            \"date_closed\": \"2022-02-23T13:55:22.000-04:00\","+
"            \"id\": 5283717867,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO457285928\","+
"                        \"title\": \"Caja Fuerte Digital PequeÃ±a\","+
"                        \"category_id\": \"MCO177806\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": \"0035102\","+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de 1 aÃ±o a partir de la fecha de compra. La instalaciÃ³n debe ser realizada por un tÃ©cnico Yale certificado.\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": null"+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 262900,"+
"                    \"full_unit_price\": 262900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 36806,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-02-28T22:17:16.162Z\","+
"            \"last_updated\": \"2022-02-28T18:17:15.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-22T22:00:51.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 133919085,"+
"                \"nickname\": \"LINDASACHICA\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 262900,"+
"            \"paid_amount\": 262900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja Fuerte Mediana\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 318900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 318900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-25T17:13:36.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"161336\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20456978089,"+
"                    \"date_last_modified\": \"2022-03-02T10:58:18.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 318900,"+
"                    \"date_created\": \"2022-02-25T17:13:33.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": null,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"12467\","+
"                    \"payment_method_id\": \"visa\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 235724947,"+
"                    \"order_id\": 5290740509,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-18T17:13:37.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41206848852"+
"            },"+
"            \"date_closed\": \"2022-02-25T17:13:37.000-04:00\","+
"            \"id\": 5290740509,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO517574654\","+
"                        \"title\": \"Caja Fuerte Mediana\","+
"                        \"category_id\": \"MCO177806\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0035100\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 318900,"+
"                    \"full_unit_price\": 318900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 44646,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-03-02T15:00:23.866Z\","+
"            \"last_updated\": \"2022-03-02T11:00:23.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-25T17:13:33.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 235724947,"+
"                \"nickname\": \"YANITHMORA\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 318900,"+
"            \"paid_amount\": 318900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja De Seguridad Yale Para Llaves Con CombinaciÃ³n\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 122900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 122900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-26T23:55:47.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 1,"+
"                    \"authorization_code\": \"205967\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20489442800,"+
"                    \"date_last_modified\": \"2022-02-26T23:57:45.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 122900,"+
"                    \"date_created\": \"2022-02-26T23:55:45.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 9044256831,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"12475\","+
"                    \"payment_method_id\": \"master\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 202459957,"+
"                    \"order_id\": 5293127910,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": null,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-19T23:55:47.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41208892525"+
"            },"+
"            \"date_closed\": \"2022-02-26T23:55:47.000-04:00\","+
"            \"id\": 5293127910,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO585665769\","+
"                        \"title\": \"Caja De Seguridad Yale Para Llaves Con CombinaciÃ³n\","+
"                        \"category_id\": \"MCO172798\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0016144\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 122900,"+
"                    \"full_unit_price\": 122900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 17206,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-03-02T01:58:12.610Z\","+
"            \"last_updated\": \"2022-02-26T23:57:48.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-26T23:55:44.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"not_delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 202459957,"+
"                \"nickname\": \"JUANFRANCISCOJURADOPEZ\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 122900,"+
"            \"paid_amount\": 122900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja Fuerte Digital PequeÃ±a\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 262900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 262900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-02-28T01:49:46.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 24,"+
"                    \"authorization_code\": \"400818\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20507717675,"+
"                    \"date_last_modified\": \"2022-03-02T16:09:59.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 10954.17,"+
"                    \"date_created\": \"2022-02-28T01:49:43.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 9091100352,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"205\","+
"                    \"payment_method_id\": \"visa\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 1008272383,"+
"                    \"order_id\": 5294913141,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": true,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-21T01:49:46.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41210385256"+
"            },"+
"            \"date_closed\": \"2022-02-28T01:49:46.000-04:00\","+
"            \"id\": 5294913141,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO457285928\","+
"                        \"title\": \"Caja Fuerte Digital PequeÃ±a\","+
"                        \"category_id\": \"MCO177806\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": \"0035102\","+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de 1 aÃ±o a partir de la fecha de compra. La instalaciÃ³n debe ser realizada por un tÃ©cnico Yale certificado.\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": null"+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 262900,"+
"                    \"full_unit_price\": 262900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 36806,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-03-02T20:12:05.398Z\","+
"            \"last_updated\": \"2022-03-02T16:12:04.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-02-28T01:49:42.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 1008272383,"+
"                \"nickname\": \"ARANGODAVID20220120192900\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 262900,"+
"            \"paid_amount\": 262900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        },"+
"        {"+
"            \"payments\": ["+
"                {"+
"                    \"reason\": \"Caja De Efectivo Mediana - Cash Box - Caja Menor\","+
"                    \"status_code\": null,"+
"                    \"total_paid_amount\": 58900,"+
"                    \"operation_type\": \"regular_payment\","+
"                    \"transaction_amount\": 58900,"+
"                    \"transaction_amount_refunded\": 0,"+
"                    \"date_approved\": \"2022-03-02T11:00:44.000-04:00\","+
"                    \"collector\": {"+
"                        \"id\": 268091189"+
"                    },"+
"                    \"coupon_id\": null,"+
"                    \"installments\": 3,"+
"                    \"authorization_code\": \"T00770\","+
"                    \"taxes_amount\": 0,"+
"                    \"id\": 20554799493,"+
"                    \"date_last_modified\": \"2022-03-02T11:02:41.000-04:00\","+
"                    \"coupon_amount\": 0,"+
"                    \"available_actions\": ["+
"                        \"refund\""+
"                    ],"+
"                    \"shipping_cost\": 0,"+
"                    \"installment_amount\": 19633.33,"+
"                    \"date_created\": \"2022-03-02T11:00:42.000-04:00\","+
"                    \"activation_uri\": null,"+
"                    \"overpaid_amount\": 0,"+
"                    \"card_id\": 9006982322,"+
"                    \"status_detail\": \"accredited\","+
"                    \"issuer_id\": \"12439\","+
"                    \"payment_method_id\": \"master\","+
"                    \"payment_type\": \"credit_card\","+
"                    \"deferred_period\": null,"+
"                    \"atm_transfer_reference\": {"+
"                        \"transaction_id\": null,"+
"                        \"company_id\": null"+
"                    },"+
"                    \"site_id\": \"MCO\","+
"                    \"payer_id\": 489500237,"+
"                    \"order_id\": 5300163546,"+
"                    \"currency_id\": \"COP\","+
"                    \"status\": \"approved\","+
"                    \"transaction_order_id\": null"+
"                }"+
"            ],"+
"            \"fulfilled\": null,"+
"            \"taxes\": {"+
"                \"amount\": null,"+
"                \"currency_id\": null,"+
"                \"id\": null"+
"            },"+
"            \"order_request\": {"+
"                \"change\": null,"+
"                \"return\": null"+
"            },"+
"            \"expiration_date\": \"2022-03-23T11:00:45.000-04:00\","+
"            \"feedback\": {"+
"                \"buyer\": null,"+
"                \"seller\": null"+
"            },"+
"            \"shipping\": {"+
"                \"id\": 41214883128"+
"            },"+
"            \"date_closed\": \"2022-03-02T11:00:44.000-04:00\","+
"            \"id\": 5300163546,"+
"            \"manufacturing_ending_date\": null,"+
"            \"order_items\": ["+
"                {"+
"                    \"item\": {"+
"                        \"id\": \"MCO561857620\","+
"                        \"title\": \"Caja De Efectivo Mediana - Cash Box - Caja Menor\","+
"                        \"category_id\": \"MCO177805\","+
"                        \"variation_id\": null,"+
"                        \"seller_custom_field\": null,"+
"                        \"global_price\": null,"+
"                        \"net_weight\": null,"+
"                        \"variation_attributes\": [],"+
"                        \"warranty\": \"GarantÃ­a de fÃ¡brica: 1 aÃ±os\","+
"                        \"condition\": \"new\","+
"                        \"seller_sku\": \"0011292\""+
"                    },"+
"                    \"quantity\": 1,"+
"                    \"unit_price\": 58900,"+
"                    \"full_unit_price\": 58900,"+
"                    \"currency_id\": \"COP\","+
"                    \"manufacturing_days\": null,"+
"                    \"picked_quantity\": null,"+
"                    \"requested_quantity\": {"+
"                        \"measure\": \"unit\","+
"                        \"value\": 1"+
"                    },"+
"                    \"sale_fee\": 9246,"+
"                    \"listing_type_id\": \"gold_special\","+
"                    \"base_exchange_rate\": null,"+
"                    \"base_currency_id\": null,"+
"                    \"bundle\": null,"+
"                    \"element_id\": null"+
"                }"+
"            ],"+
"            \"date_last_updated\": \"2022-03-02T15:02:55.196Z\","+
"            \"last_updated\": \"2022-03-02T11:02:45.000-04:00\","+
"            \"comment\": null,"+
"            \"pack_id\": null,"+
"            \"coupon\": {"+
"                \"amount\": 0,"+
"                \"id\": null"+
"            },"+
"            \"shipping_cost\": null,"+
"            \"date_created\": \"2022-03-02T11:00:40.000-04:00\","+
"            \"pickup_id\": null,"+
"            \"status_detail\": null,"+
"            \"tags\": ["+
"                \"not_delivered\","+
"                \"paid\""+
"            ],"+
"            \"buyer\": {"+
"                \"id\": 489500237,"+
"                \"nickname\": \"ERIKAYOHANAORTIZRODRIGUEZ\""+
"            },"+
"            \"seller\": {"+
"                \"id\": 268091189,"+
"                \"nickname\": \"YALE COLOMBIA\""+
"            },"+
"            \"total_amount\": 58900,"+
"            \"paid_amount\": 58900,"+
"            \"currency_id\": \"COP\","+
"            \"status\": \"paid\""+
"        }"+
"    ],"+
"    \"sort\": {"+
"        \"id\": \"date_asc\","+
"        \"name\": \"Date ascending\""+
"    },"+
"    \"available_sorts\": ["+
"        {"+
"            \"id\": \"date_desc\","+
"            \"name\": \"Date descending\""+
"        }"+
"    ],"+
"    \"filters\": ["+
"        {"+
"            \"id\": \"order.status\","+
"            \"name\": \"Order Status\","+
"            \"type\": \"text\","+
"            \"values\": ["+
"                {"+
"                    \"id\": \"paid\","+
"                    \"name\": \"Order Paid\""+
"                }"+
"            ]"+
"        }"+
"    ],"+
"    \"paging\": {"+
"        \"total\": 10,"+
"        \"offset\": 0,"+
"        \"limit\": 51"+
"    },"+
"    \"display\": \"complete\""+
"}";
	

        return myvar;
    }

    /**
     *
     * @param accessToken
     * @param offset
     * @return
     */
    public String orders(String accessToken, int offset){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&access_token="+accessToken + "&offset="+offset;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param limit
     * @return
     */
    public String ordersLimit(String accessToken, int limit){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&access_token="+accessToken + "&limit="+limit;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param orderStatus
     * @return
     */
    public String ordersByState(String accessToken, OrderStatus orderStatus)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&order.status=" + orderStatus.getValue() +
                "&access_token="+accessToken;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param orderStatus
     * @param offset
     * @return
     */
    public String ordersByState(String accessToken, OrderStatus orderStatus, int offset)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&order.status=" + orderStatus.getValue() +
                "&access_token="+accessToken + "&offset="+offset;

        return template.getForEntity(url, String.class).getBody();
    }


    /**
     *
     * @param accessToken
     * @param orderStatus
     * @param limit
     * @return
     */
    public String ordersByStateLimit(String accessToken, OrderStatus orderStatus, int limit)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&order.status=" + orderStatus.getValue() +
                "&access_token="+accessToken + "&limit="+limit;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param orderStatus
     * @return
     */
    public String ordersByStateAndDate(String accessToken, OrderStatus orderStatus, Date actualDate)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-ddTHH:mm:ss.000-00:00");
        Timestamp timestamp = new Timestamp(actualDate.getTime());
        String date = sdf2.format(timestamp);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&order.date_closed.from="+date +
                "&order.status=" + orderStatus.getValue() +
                "&access_token="+accessToken;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param orderStatus
     * @return
     */
    public String ordersByStateAndDate(String accessToken, OrderStatus orderStatus, Date actualDate, int offset)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-ddTHH:mm:ss.000-00:00");
        Timestamp timestamp = new Timestamp(actualDate.getTime());
        String date = sdf2.format(timestamp);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&order.date_closed.from="+date +
                "&order.status=" + orderStatus.getValue() +
                "&access_token="+accessToken +
                "&offset="+offset;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param orderStatus
     * @return
     */
    public String ordersByStateAndDateLimit(String accessToken, OrderStatus orderStatus, Date actualDate, int limit)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-ddTHH:mm:ss.000-00:00");
        Timestamp timestamp = new Timestamp(actualDate.getTime());
        String date = sdf2.format(timestamp);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ORDERS_SEARCH + "?seller=" +
                Constantes.SELLER + "&order.date_closed.from="+date +
                "&order.status=" + orderStatus.getValue() +
                "&access_token="+accessToken +
                "&limit="+limit;

        return template.getForEntity(url, String.class).getBody();
    }

    /**
     *
     * @param accessToken
     * @param shippingID
     * @return
     */
    public String getShippingData(String accessToken, String shippingID)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_SHIPPING.replace("SHIPMENT_ID", shippingID) +
                "?access_token="+accessToken;

        return template.getForEntity(url, String.class).getBody();
    }

    public String getDummyShippingData()
    {
        return "{\n" +
                "    \"id\": 123456789,\n" +
                "    \"external_reference\": null,\n" +
                "    \"status\": \"ready_to_ship\",\n" +
                "    \"substatus\": \"printed\",\n" +
                "    \"date_created\": \"2019-09-02T15:12:31.000-04:00\",\n" +
                "    \"last_updated\": \"2019-09-02T15:16:52.000-04:00\",\n" +
                "    \"declared_value\": 120,\n" +
                "    \"dimensions\": {\n" +
                "        \"height\": 9,\n" +
                "        \"width\": 25,\n" +
                "        \"length\": 32,\n" +
                "        \"weight\": 310\n" +
                "    },\n" +
                "    \"logistic\": {\n" +
                "        \"direction\": \"forward\",\n" +
                "        \"mode\": \"me2\",\n" +
                "        \"type\": \"drop_off\"\n" +
                "    },\n" +
                "    \"source\": {\n" +
                "        \"site_id\": \"MLB\",\n" +
                "        \"market_place\": \"MELI\",\n" +
                "        \"application_id\": null\n" +
                "    },\n" +
                "    \"tracking_number\": \"1011234567890\",\n" +
                "    \"origin\": {\n" +
                "        \"type\": \"selling_address\",\n" +
                "        \"sender_id\": 4321345667,\n" +
                "        \"shipping_address\": {\n" +
                "            \"address_id\": 1035444445,\n" +
                "            \"address_line\": \"XXXXXXX\",\n" +
                "            \"street_name\": \"XXXXXXX\",\n" +
                "            \"street_number\": \"XXXXXXX\",\n" +
                "            \"comment\": \"XXXXXXX\",\n" +
                "            \"zip_code\": \"02554123\",\n" +
                "            \"city\": {\n" +
                "                \"id\": \"BR-SP-44\",\n" +
                "                \"name\": \"São Paulo\"\n" +
                "            },\n" +
                "            \"state\": {\n" +
                "                \"id\": \"BR-SP\",\n" +
                "                \"name\": \"São Paulo\"\n" +
                "            },\n" +
                "            \"country\": {\n" +
                "                \"id\": \"BR\",\n" +
                "                \"name\": \"Brasil\"\n" +
                "            },\n" +
                "            \"neighborhood\": {\n" +
                "                \"id\": null,\n" +
                "                \"name\": \"Vila Diva (Zona Norte)\"\n" +
                "            },\n" +
                "            \"municipality\": {\n" +
                "                \"id\": null,\n" +
                "                \"name\": null\n" +
                "            },\n" +
                "            \"agency\": {\n" +
                "                \"carrier_id\": null,\n" +
                "                \"agency_id\": null,\n" +
                "                \"description\": null,\n" +
                "                \"phone\": null,\n" +
                "                \"open_hours\": null\n" +
                "            },\n" +
                "            \"types\": [\n" +
                "                \"billing\",\n" +
                "                \"default_selling_address\",\n" +
                "                \"shipping\"\n" +
                "            ],\n" +
                "            \"latitude\": 0,\n" +
                "            \"longitude\": 0,\n" +
                "            \"geolocation_type\": \"ROOFTOP\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"destination\": {\n" +
                "        \"type\": \"buying_address\",\n" +
                "        \"receiver_id\": 4322312345,\n" +
                "        \"receiver_name\": \"Teste Joaozinho\",\n" +
                "        \"receiver_phone\": \"48 12345678\",\n" +
                "        \"comments\": null,\n" +
                "        \"shipping_address\": {\n" +
                "            \"address_id\": 123456789,\n" +
                "            \"address_line\": \"Avenida Brigadeiro Faria 123456\",\n" +
                "            \"street_name\": \"Avenida Brigadeiro Faria\",\n" +
                "            \"street_number\": \"123456\",\n" +
                "            \"comment\": null,\n" +
                "            \"zip_code\": \"0713123456\",\n" +
                "            \"city\": {\n" +
                "                \"id\": \"BR-SP-41\",\n" +
                "                \"name\": \"Guarulhos\"\n" +
                "            },\n" +
                "            \"state\": {\n" +
                "                \"id\": \"BR-SP\",\n" +
                "                \"name\": \"São Paulo\"\n" +
                "            },\n" +
                "            \"country\": {\n" +
                "                \"id\": \"BR\",\n" +
                "                \"name\": \"Brasil\"\n" +
                "            },\n" +
                "            \"neighborhood\": {\n" +
                "                \"id\": null,\n" +
                "                \"name\": \"Cocaia\"\n" +
                "            },\n" +
                "            \"municipality\": {\n" +
                "                \"id\": null,\n" +
                "                \"name\": null\n" +
                "            },\n" +
                "            \"agency\": {\n" +
                "                \"carrier_id\": null,\n" +
                "                \"agency_id\": null,\n" +
                "                \"description\": null,\n" +
                "                \"phone\": null,\n" +
                "                \"open_hours\": null\n" +
                "            },\n" +
                "            \"types\": [\n" +
                "                \"default_buying_address\"\n" +
                "            ],\n" +
                "            \"latitude\": -23.442744,\n" +
                "            \"longitude\": -46.522703,\n" +
                "            \"geolocation_type\": \"ROOFTOP\",\n" +
                "            \"delivery_preference\": \"residential\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"lead_time\": {\n" +
                "        \"option_id\": 1964123456,\n" +
                "        \"shipping_method\": {\n" +
                "            \"id\": 182,\n" +
                "            \"name\": \"Expresso\",\n" +
                "            \"type\": \"express\",\n" +
                "            \"deliver_to\": \"address\"\n" +
                "        },\n" +
                "        \"currency_id\": \"BRL\",\n" +
                "        \"cost\": 0,\n" +
                "        \"list_cost\": 15.45,\n" +
                "        \"cost_type\": \"free\",\n" +
                "        \"service_id\": 22,\n" +
                "        \"delivery_type\": \"estimated\",\n" +
                "        \"estimated_schedule_limit\": {\n" +
                "            \"date\": null\n" +
                "        },\n" +
                "        \"estimated_delivery_time\": {\n" +
                "            \"type\": \"known_frame\",\n" +
                "            \"date\": \"2019-09-04T00:00:00.000-03:00\",\n" +
                "            \"unit\": \"hour\",\n" +
                "            \"offset\": {\n" +
                "                \"date\": \"2019-09-05T00:00:00.000-03:00\",\n" +
                "                \"shipping\": 24\n" +
                "            },\n" +
                "            \"time_frame\": {\n" +
                "                \"from\": null,\n" +
                "                \"to\": null\n" +
                "            },\n" +
                "            \"pay_before\": null,\n" +
                "            \"shipping\": 24,\n" +
                "            \"handling\": 24,\n" +
                "            \"schedule\": null\n" +
                "        },\n" +
                "        \"estimated_delivery_limit\": {\n" +
                "            \"date\": \"2019-09-19T00:00:00.000-03:00\",\n" +
                "            \"offset\": 240\n" +
                "        },\n" +
                "        \"estimated_delivery_final\": {\n" +
                "            \"date\": \"2020-01-03T00:00:00.000-03:00\",\n" +
                "            \"offset\": 1920\n" +
                "        },\n" +
                "        \"estimated_delivery_extended\": {\n" +
                "            \"date\": \"2019-09-12T00:00:00.000-03:00\",\n" +
                "            \"offset\": 120\n" +
                "        },\n" +
                "        \"estimated_handling_limit\": {\n" +
                "            \"date\": \"2019-09-03T00:00:00.000-03:00\"\n" +
                "        },\n" +
                "        \"delay\": [\n" +
                "            \"handling_delayed\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"tags\": [\n" +
                "        \"test_shipment\"\n" +
                "    ]\n" +
                "}";
    }

    /**
     *
     * @param accessToken
     * @param shippingID
     * @return
     */
    public String getShippingDataImageReport(String accessToken, String... shippingID)
    {
        String shipping = String.join(",", shippingID);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_SHIPPING.replace("SHIPPING_ID", shipping) +
                "?access_token="+accessToken;

        return template.getForEntity(url, String.class).getBody();
    }




    /**
     *
     * @param content Contenido del correo electronico
     * @param subject Asunto del correo
     */
    public void email(String content, String subject)
    {

        String myvar = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"+
                "    <html xmlns=\"http://www.w3.org/1999/xhtml\">"+
                "    <head>"+
                "      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
                "      <title>[SUBJECT]</title>"+
                "      <style type=\"text/css\">"+
                "      body {"+
                "       padding-top: 0 !important;"+
                "       padding-bottom: 0 !important;"+
                "       padding-top: 0 !important;"+
                "       padding-bottom: 0 !important;"+
                "       margin:0 !important;"+
                "       width: 100% !important;"+
                "       -webkit-text-size-adjust: 100% !important;"+
                "       -ms-text-size-adjust: 100% !important;"+
                "       -webkit-font-smoothing: antialiased !important;"+
                "     }"+
                "     .tableContent img {"+
                "       border: 0 !important;"+
                "       display: block !important;"+
                "       outline: none !important;"+
                "     }"+
                "     a{"+
                "      color:#382F2E;"+
                "    }"+
                ""+
                "    p, h1{"+
                "      color:#382F2E;"+
                "      margin:0;"+
                "    }"+
                "    p{"+
                "      text-align:left;"+
                "      color:#999999;"+
                "      font-size:14px;"+
                "      font-weight:normal;"+
                "      line-height:19px;"+
                "    }"+
                ""+
                "    a.link1{"+
                "      color:#382F2E;"+
                "    }"+
                "    a.link2{"+
                "      font-size:16px;"+
                "      text-decoration:none;"+
                "      color:#ffffff;"+
                "    }"+
                ""+
                "    h2{"+
                "      text-align:left;"+
                "       color:#222222; "+
                "       font-size:19px;"+
                "      font-weight:normal;"+
                "    }"+
                "    div,p,ul,h1{"+
                "      margin:0;"+
                "    }"+
                ""+
                "    .bgBody{"+
                "      background: #ffffff;"+
                "    }"+
                "    .bgItem{"+
                "      background: #ffffff;"+
                "    }"+
                "	"+
                "@media only screen and (max-width:480px)"+
                "		"+
                "{"+
                "		"+
                "table[class=\"MainContainer\"], td[class=\"cell\"] "+
                "	{"+
                "		width: 100% !important;"+
                "		height:auto !important; "+
                "	}"+
                "td[class=\"specbundle\"] "+
                "	{"+
                "		width:100% !important;"+
                "		float:left !important;"+
                "		font-size:13px !important;"+
                "		line-height:17px !important;"+
                "		display:block !important;"+
                "		padding-bottom:15px !important;"+
                "	}"+
                "		"+
                "td[class=\"spechide\"] "+
                "	{"+
                "		display:none !important;"+
                "	}"+
                "	    img[class=\"banner\"] "+
                "	{"+
                "	          width: 100% !important;"+
                "	          height: auto !important;"+
                "	}"+
                "		td[class=\"left_pad\"] "+
                "	{"+
                "			padding-left:15px !important;"+
                "			padding-right:15px !important;"+
                "	}"+
                "		 "+
                "}"+
                "	"+
                "@media only screen and (max-width:540px) "+
                ""+
                "{"+
                "		"+
                "table[class=\"MainContainer\"], td[class=\"cell\"] "+
                "	{"+
                "		width: 100% !important;"+
                "		height:auto !important; "+
                "	}"+
                "td[class=\"specbundle\"] "+
                "	{"+
                "		width:100% !important;"+
                "		float:left !important;"+
                "		font-size:13px !important;"+
                "		line-height:17px !important;"+
                "		display:block !important;"+
                "		padding-bottom:15px !important;"+
                "	}"+
                "		"+
                "td[class=\"spechide\"] "+
                "	{"+
                "		display:none !important;"+
                "	}"+
                "	    img[class=\"banner\"] "+
                "	{"+
                "	          width: 100% !important;"+
                "	          height: auto !important;"+
                "	}"+
                "	.font {"+
                "		font-size:18px !important;"+
                "		line-height:22px !important;"+
                "		"+
                "		}"+
                "		.font1 {"+
                "		font-size:18px !important;"+
                "		line-height:22px !important;"+
                "		"+
                "		}"+
                "}"+
                ""+
                "    </style>"+
                ""+
                "<script type=\"colorScheme\" class=\"swatch active\">"+
                "{"+
                "    \"name\":\"Default\","+
                "    \"bgBody\":\"ffffff\","+
                "    \"link\":\"382F2E\","+
                "    \"color\":\"999999\","+
                "    \"bgItem\":\"ffffff\","+
                "    \"title\":\"222222\""+
                "}"+
                "</script>"+
                ""+
                "  </head>"+
                "  <body paddingwidth=\"0\" paddingheight=\"0\"   style=\"padding-top: 0; padding-bottom: 0; padding-top: 0; padding-bottom: 0; background-repeat: repeat; width: 100% !important; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-font-smoothing: antialiased;\" offset=\"0\" toppadding=\"0\" leftpadding=\"0\">"+
                "    <table bgcolor=\"#ffffff\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"tableContent\" align=\"center\"  style='font-family:Helvetica, Arial,serif;'>"+
                "  <tbody>"+
                "    <tr>"+
                "      <td><table width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" bgcolor=\"#ffffff\" class=\"MainContainer\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td valign=\"top\" width=\"40\"> </td>"+
                "      <td><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "  <!-- =============================== Header ====================================== -->   "+
                "    <tr>"+
                "    	<td height='75' class=\"spechide\"></td>"+
                "        "+
                "        <!-- =============================== Body ====================================== -->"+
                "    </tr>"+
                "    <tr>"+
                "      <td class='movableContentContainer ' valign='top'>"+
                "      	<div class=\"movableContent\" style=\"border: 0px; padding-top: 0px; position: relative;\">"+
                "        	<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td height=\"35\"></td>"+
                "    </tr>"+
                "    <tr>"+
                "      <td><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td valign=\"top\" align=\"center\" class=\"specbundle\"><div class=\"contentEditableContainer contentTextEditable\">"+
                "                                <div class=\"contentEditable\">"+
                "                                  <p style='text-align:center;margin:0;font-family:Georgia,Time,sans-serif;font-size:26px;color:#222222;'><span class=\"specbundle2\"><span class=\"font1\">Welcome to </span></span></p>"+
                "                                </div>"+
                "                              </div></td>"+
                "      <td valign=\"top\" class=\"specbundle\"><div class=\"contentEditableContainer contentTextEditable\">"+
                "                                <div class=\"contentEditable\">"+
                "                                  <p style='text-align:center;margin:0;font-family:Georgia,Time,sans-serif;font-size:26px;color:#1A54BA;'><span class=\"font\">[CLIENTS.COMPANY_NAME]</span> </p>"+
                "                                </div>"+
                "                              </div></td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "</td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "        </div>"+
                "        <div class=\"movableContent\" style=\"border: 0px; padding-top: 0px; position: relative;\">"+
                "        	<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                          <tr>"+
                "                            <td valign='top' align='center'>"+
                "                              <div class=\"contentEditableContainer contentImageEditable\">"+
                "                                <div class=\"contentEditable\">"+
                "                                  <img src=\"images/line.png\" width='251' height='43' alt='' data-default=\"placeholder\" data-max-width=\"560\">"+
                "                                </div>"+
                "                              </div>"+
                "                            </td>"+
                "                          </tr>"+
                "                        </table>"+
                "        </div>"+
                "        <div class=\"movableContent\" style=\"border: 0px; padding-top: 0px; position: relative;\">"+
                "        	<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                          <tr><td height='55'></td></tr>"+
                "                          <tr>"+
                "                            <td align='left'>"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                <div class=\"contentEditable\" align='center'>"+
                "                                  <h2 >Will this be your welcome email?</h2>"+
                "                                </div>"+
                "                              </div>"+
                "                            </td>"+
                "                          </tr>"+
                ""+
                "                          <tr><td height='15'> </td></tr>"+
                ""+
                "                          <tr>"+
                "                            <td align='left'>"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                <div class=\"contentEditable\" align='center'>"+
                "                                  <p >"+
                "                                    Hereâ€™s what you can say: Thanks again for signing up to the newsletter! Youâ€™re all set up, and will be getting the emails once per week. Meanwhile, you can check out our <a target='_blank' href='#' class='link1' >Getting Started</a> section to get the most out of your new account. "+
                "                                    <br>"+
                "                                    <br>"+
                "                                    Have questions? Get in touch with us via Facebook or Twitter, or email our support team."+
                "                                    <br>"+
                "                                    <br>"+
                "                                    Cheers,"+
                "                                    <br>"+
                "                                    <span style='color:#222222;'>Peter Parker</span>"+
                "                                  </p>"+
                "                                </div>"+
                "                              </div>"+
                "                            </td>"+
                "                          </tr>"+
                ""+
                "                          <tr><td height='55'></td></tr>"+
                ""+
                "                          <tr>"+
                "                            <td align='center'>"+
                "                              <table>"+
                "                                <tr>"+
                "                                  <td align='center' bgcolor='#1A54BA' style='background:#1A54BA; padding:15px 18px;-webkit-border-radius: 4px; -moz-border-radius: 4px; border-radius: 4px;'>"+
                "                                    <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                      <div class=\"contentEditable\" align='center'>"+
                "                                        <a target='_blank' href='#' class='link2' style='color:#ffffff;'>Activate your Account</a>"+
                "                                      </div>"+
                "                                    </div>"+
                "                                  </td>"+
                "                                </tr>"+
                "                              </table>"+
                "                            </td>"+
                "                          </tr>"+
                "                          <tr><td height='20'></td></tr>"+
                "                        </table>"+
                "        </div>"+
                "        <div class=\"movableContent\" style=\"border: 0px; padding-top: 0px; position: relative;\">"+
                "        	<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td height='65'>"+
                "    </tr>"+
                "    <tr>"+
                "      <td  style='border-bottom:1px solid #DDDDDD;'></td>"+
                "    </tr>"+
                "    <tr><td height='25'></td></tr>"+
                "    <tr>"+
                "      <td><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td valign=\"top\" class=\"specbundle\"><div class=\"contentEditableContainer contentTextEditable\">"+
                "                                      <div class=\"contentEditable\" align='center'>"+
                "                                        <p  style='text-align:left;color:#CCCCCC;font-size:12px;font-weight:normal;line-height:20px;'>"+
                "                                          <span style='font-weight:bold;'>[CLIENTS.COMPANY_NAME]</span>"+
                "                                          <br>"+
                "                                          [CLIENTS.ADDRESS]"+
                "                                          <br>"+
                "                                          <a target='_blank' href=\"[FORWARD]\">Forward to a friend</a><br>"+
                "                                          <a target=\"_blank\" class='link1' class='color:#382F2E;' href=\"[UNSUBSCRIBE]\">Unsubscribe</a>"+
                "                                          <br>"+
                "                                          <a target='_blank' class='link1' class='color:#382F2E;' href=\"[SHOWEMAIL]\">Show this email in your browser</a>"+
                "                                        </p>"+
                "                                      </div>"+
                "                                    </div></td>"+
                "      <td valign=\"top\" width=\"30\" class=\"specbundle\"> </td>"+
                "      <td valign=\"top\" class=\"specbundle\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "  <tbody>"+
                "    <tr>"+
                "      <td valign='top' width='52'>"+
                "                                    <div class=\"contentEditableContainer contentFacebookEditable\">"+
                "                                      <div class=\"contentEditable\">"+
                "                                        <a target='_blank' href=\"#\"><img src=\"images/facebook.png\" width='52' height='53' alt='facebook icon' data-default=\"placeholder\" data-max-width=\"52\" data-customIcon=\"true\"></a>"+
                "                                      </div>"+
                "                                    </div>"+
                "                                  </td>"+
                "      <td valign=\"top\" width=\"16\"> </td>"+
                "      <td valign='top' width='52'>"+
                "                                    <div class=\"contentEditableContainer contentTwitterEditable\">"+
                "                                      <div class=\"contentEditable\">"+
                "                                        <a target='_blank' href=\"#\"><img src=\"images/twitter.png\" width='52' height='53' alt='twitter icon' data-default=\"placeholder\" data-max-width=\"52\" data-customIcon=\"true\"></a>"+
                "                                      </div>"+
                "                                    </div>"+
                "                                  </td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "</td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "</td>"+
                "    </tr>"+
                "    <tr><td height='88'></td></tr>"+
                "  </tbody>"+
                "</table>"+
                ""+
                "        </div>"+
                "        "+
                "        <!-- =============================== footer ====================================== -->"+
                "      "+
                "      </td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "</td>"+
                "      <td valign=\"top\" width=\"40\"> </td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "</td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                "</td>"+
                "    </tr>"+
                "  </tbody>"+
                "</table>"+
                ""+
                "<!--Default Zone"+
                ""+
                "      <div class=\"customZone\" data-type=\"image\">"+
                "          <div class=\"movableContent\">"+
                "            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "            <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "              <tr>"+
                "                <td height='42'> </td>"+
                "              </tr>"+
                "              <tr>"+
                "                <td>"+
                "                  <div class=\"contentEditableContainer contentImageEditable\">"+
                "                      <div class=\"contentEditable\">"+
                "                          <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/temp_img_1.png\" data-default=\"placeholder\" data-max-width=\"540\">"+
                "                      </div>"+
                "                  </div>"+
                "                </td>"+
                "              </tr>"+
                "            </table>"+
                "            </td></tr></table>"+
                "          </div>"+
                "      </div>"+
                "      "+
                "      <div class=\"customZone\" data-type=\"text\">"+
                "          <div class='movableContent'>"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                          <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                            <tr><td height='10'> </td></tr>"+
                "                            <tr>"+
                "                              <td align='left' valign='top'>"+
                "                                <div class=\"contentEditableContainer contentTextEditable\">"+
                "                        <div class=\"contentEditable\" >"+
                "                                <h2>Weâ€™re going to blow your mind.</h2>"+
                "                                </div>"+
                "                      </div>"+
                "                              </td>"+
                "                            </tr>"+
                "                            <tr><td height='15'> </td></tr>"+
                "                            <tr>"+
                "                              <td align='left' valign='top'>"+
                "                                <div class=\"contentEditableContainer contentTextEditable\">"+
                "                        <div class=\"contentEditable\" >"+
                "                                <p >Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."+
                "                                <br><br>"+
                "                                Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.</p>"+
                "                                </div>"+
                "                      </div>"+
                "                              </td>"+
                "                            </tr>"+
                "                            <tr><td height='25'> </td></tr>"+
                "                            <tr>"+
                "                              <td align='right' valign='top'>"+
                "                                <div class=\"contentEditableContainer contentTextEditable\">"+
                "                        <div class=\"contentEditable\" >"+
                "                                <a target='_blank' href=\"#\" class='link1'>Read more â†’</a>"+
                "                                </div>"+
                "                      </div>"+
                "                              </td>"+
                "                            </tr>"+
                "                          </table>"+
                "                          </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                "      "+
                "      <div class=\"customZone\" data-type=\"imageText\">"+
                "          <div class='movableContent'>"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                      <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                        <tr><td height='10'> </td></tr>"+
                "                        <tr>"+
                "                          <td valign='top'>"+
                "                            <div class=\"contentEditableContainer contentImageEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                <img src=\"images/macCat.jpg\" alt='product image' data-default=\"placeholder\" data-max-width=\"255\" width='255' height='154'>"+
                "                              </div>"+
                "                            </div>"+
                "                          </td>"+
                ""+
                "                          <td valign='top'>"+
                "                            <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                <table width=\"255\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                                  <tr>"+
                "                                    <td valign='top'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <h2>Chresmographion</h2>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='12'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-right:30px;'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <p >Chamber between the pronaos and the cella in Greek temples where oracles were delivered.</p>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='25'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-bottom:15px;'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <a target='_blank' href=\"#\" class='link1'>Find out more â†’</a>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                "                                </table>"+
                "                          </td>"+
                "                        </tr>"+
                "                      </table>"+
                "                      </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                "      "+
                "      <div class=\"customZone\" data-type=\"Textimage\">"+
                "          <div class='movableContent'>"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                      <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                        <tr><td height='10'> </td></tr>"+
                "                        <tr>"+
                "                          <td valign='top'>"+
                "                                <table width=\"255\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                                  <tr>"+
                "                                    <td valign='top'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <h2>Chresmographion</h2>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='12'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-right:30px;'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <p >Chamber between the pronaos and the cella in Greek temples where oracles were delivered.</p>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='25'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-bottom:15px;'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <a target='_blank' href=\"#\" class='link1'>Find out more â†’</a>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                "                                </table>"+
                "                          </td>"+
                ""+
                "                          <td valign='top'>"+
                "                            <div class=\"contentEditableContainer contentImageEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                <img src=\"images/macCat.jpg\" alt='product image' data-default=\"placeholder\" data-max-width=\"255\" width='255' height='154'>"+
                "                              </div>"+
                "                            </div>"+
                "                          </td>"+
                "                        </tr>"+
                "                      </table>"+
                "                      </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"textText\">"+
                "          <div class='movableContent'>"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                      <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                        <tr><td height='10'> </td></tr>"+
                "                        <tr>"+
                "                          <td valign='top'>"+
                "                                <table width=\"255\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                                  <tr>"+
                "                                    <td valign='top'>"+
                "                                       <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <h2>Barrel Vault</h2>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='12'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-right:30px;'>"+
                "                                       <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <p >An architectural element formed by the extrusion of a single curve (or pair of curves, in the case of a pointed barrel vault) along a given distance.</p>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='25'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-bottom:15px;'>"+
                "                                       <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <a target='_blank' href=\"#\" class='link1'>Find out more â†’</a>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                "                                </table>"+
                "                          </td>"+
                ""+
                "                          <td valign='top'>"+
                "                                <table width=\"255\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                                  <tr>"+
                "                                    <td valign='top'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <h2>Chresmographion</h2>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='12'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-right:30px;'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <p >Chamber between the pronaos and the cella in Greek temples where oracles were delivered.</p>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                ""+
                "                                  <tr><td height='25'></td></tr>"+
                ""+
                "                                  <tr>"+
                "                                    <td valign='top' style='padding-bottom:15px;'>"+
                "                                      <div class=\"contentEditableContainer contentTextEditable\">"+
                "                              <div class=\"contentEditable\" >"+
                "                                      <a target='_blank' href=\"#\" class='link1'>Find out more â†’</a>"+
                "                                      </div>"+
                "                            </div>"+
                "                                    </td>"+
                "                                  </tr>"+
                "                                </table>"+
                "                          </td>"+
                "                        </tr>"+
                "                      </table>"+
                "                      </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"qrcode\">"+
                "          <div class=\"movableContent\">"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                  <table width='520' cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"margin-top:10px;\">"+
                "                    <tr>"+
                "                    <td height='42'> </td>"+
                "                    <td height='42'> </td>"+
                "              </tr>"+
                "                      <tr>"+
                "                          <td valign='top' valign=\"top\" width=\"75\" style='padding:0 20px;'>"+
                "                              <div class=\"contentQrcodeEditable contentEditableContainer\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/qr_code.png\" width=\"75\" height=\"75\">"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td valign='top' valign=\"top\" style=\"padding:0 20px 0 20px;\">"+
                "                              <div class=\"contentTextEditable contentEditableContainer\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <p >Etiam bibendum nunc in lacus bibendum porta. Vestibulum nec nulla et eros ornare condimentum. Proin facilisis, dui in mollis blandit. Sed non dui magna, quis tincidunt enim. Morbi vehicula pharetra lacinia. Cras tincidunt, justo at fermentum feugiat, eros orci accumsan dolor, eu ultricies eros dolor quis sapien.</p>"+
                "                                      <p >Integer in elit in tortor posuere molestie non a velit. Pellentesque consectetur, nisi a euismod scelerisque.</p>"+
                "                                      <p style='text-align:right;margin:0;font-family:Georgia, serif;'><a target='_blank' href='#' class='link1'>Read more â†’</a></p>"+
                "                                      <br>"+
                "                                      <br>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                      </tr>"+
                "                  </table>"+
                "              </td></tr></table>"+
                "          </div>"+
                "      </div>"+
                "      "+
                "      <div class=\"customZone\" data-type=\"gmap\">"+
                "          <div class=\"movableContent\">"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                ""+
                "                  <table width='520' cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"margin-top:10px;\">"+
                "                    <tr>"+
                "                    <td height='42'> </td>"+
                "                    <td height='42'> </td>"+
                "              </tr>"+
                "                      <tr>"+
                "                          <td valign='top' style='padding:0 20px;'>"+
                "                              <div class=\"contentEditableContainer contentGmapEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/gmap_example.png\" width=\"175\" data-default=\"placeholder\">"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td valign='top' style=\"padding:0 20px 0 20px;\">"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <h2>This is a subtitle</h2>"+
                "                                      <p >Etiam bibendum nunc in lacus bibendum porta. Vestibulum nec nulla et eros ornare condimentum. Proin facilisis, dui in mollis blandit. Sed non dui magna, quis tincidunt enim. Morbi vehicula pharetra lacinia. Cras tincidunt, justo at fermentum feugiat, eros orci accumsan dolor, eu ultricies eros dolor quis sapien.</p>"+
                "                                      <p >Integer in elit in tortor posuere molestie non a velit. Pellentesque consectetur, nisi a euismod scelerisque.</p>"+
                "                                      <p style='text-align:right;margin:0;'><a target='_blank' href=\"#\" class='link1'>Read more â†’</a></p>"+
                "                                      <br>"+
                "                                      <br>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                      </tr>"+
                "                  </table>"+
                "              </td></tr></table>"+
                "          </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"social\">"+
                "          <div class=\"movableContent\">"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                  <table width='520' cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">"+
                "                    <tr>"+
                "                    <td height='42' colspan='4'> </td>"+
                "              </tr>"+
                "                      <tr>"+
                "                          <td valign='top' colspan=\"4\" style='padding:0 20px;'>"+
                "                              <div class=\"contentTextEditable contentEditableContainer\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <h2 >This is a subtitle</h2>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                      </tr>"+
                "                      <tr>"+
                "                          <td width='62' valign='top' valign=\"top\" width=\"62\" style='padding:0 0 0 20px;'>"+
                "                              <div class=\"contentEditableContainer contentTwitterEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/icon_twitter.png\" width=\"42\" height=\"42\" data-customIcon=\"true\" data-max-width='42' data-noText=\"false\">"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td width='216' valign='top' >"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <p >Follow us on twitter to stay up to date with company news and other information.</p>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td width='62' valign='top' valign=\"top\" width=\"62\">"+
                "                              <div class=\"contentEditableContainer contentFacebookEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/icon_facebook.png\" width=\"42\" height=\"42\" data-customIcon=\"true\" data-max-width='42' data-noText=\"false\">"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td width='216' valign='top' style='padding:0 20px 0 0;'>"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <p >Like us on Facebook to keep up with our news, updates and other discussions.</p>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                      </tr>"+
                "                  </table>"+
                "              </td></tr></table>"+
                "          </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"twitter\">"+
                "          <div class=\"movableContent\">"+
                "          <table width=\"100%\"  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                  <table width='520' cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">"+
                "                    <tr>"+
                "                    <td height='42'> </td>"+
                "                    <td height='42'> </td>"+
                "              </tr>"+
                "                      <tr>"+
                "                          <td valign='top' valign=\"top\" width=\"62\" style='padding:0 0 0 20px;'>"+
                "                              <div class=\"contentEditableContainer contentTwitterEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/icon_twitter.png\" width=\"42\" height=\"42\" data-customIcon=\"true\" data-max-width='42' data-noText=\"false\">"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td valign='top' style='padding:0 20px 0 0;'>"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <p >Follow us on twitter to stay up to date with company news and other information.</p>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                      </tr>"+
                "                  </table>"+
                "              </td></tr></table>"+
                "          </div>"+
                "      </div>"+
                "      "+
                "      <div class=\"customZone\" data-type=\"facebook\">"+
                "          <div class=\"movableContent\">"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                  <table width='520' cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">"+
                "                    <tr>"+
                "                    <td height='42'> </td>"+
                "                    <td height='42'> </td>"+
                "              </tr>"+
                "                      <tr>"+
                "                          <td valign='top' valign=\"top\" width=\"62\" style='padding:0 0 0 20px;'>"+
                "                              <div class=\"contentEditableContainer contentFacebookEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <img src=\"/applications/Mail_Interface/3_3/modules/User_Interface/core/v31_campaigns/images/neweditor/default/icon_facebook.png\" width=\"42\" height=\"42\" data-customIcon=\"true\" data-max-width='42' data-noText=\"false\">"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                          <td valign='top' style='padding:0 20px 0 0;'>"+
                "                              <div class=\"contentEditableContainer contentTextEditable\">"+
                "                                  <div class=\"contentEditable\">"+
                "                                      <p >\"Like us on Facebook to keep up with our news, updates and other discussions.</p>"+
                "                                  </div>"+
                "                              </div>"+
                "                          </td>"+
                "                      </tr>"+
                "                  </table>"+
                "              </td></tr></table>"+
                "          </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"line\">"+
                "          <div class='movableContent'>"+
                "          <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "              <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" >"+
                "                <tr><td height='10'> </td></tr>"+
                "                <tr><td style='border-bottom:1px solid #DDDDDD'></td></tr>"+
                "                <tr><td height='10'> </td></tr>"+
                "              </table>"+
                "              </td></tr></table>"+
                "            </div>"+
                "      </div>"+
                ""+
                "      "+
                "      <div class=\"customZone\" data-type=\"colums1v2\"><div class='movableContent'>"+
                "      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                          <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" >"+
                "                            <tr><td height=\"10\"> </td></tr>"+
                "                            <tr>"+
                "                              <td align=\"left\" valign=\"top\">"+
                "                                <table width='100%'  border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'  >"+
                "                                  <tr><td colspan='3' height='25'> </td></tr>"+
                "                                  <tr>"+
                "                                    <td width='25'> </td>"+
                "                                    <td class=\"newcontent\">"+
                "                                  "+
                "                                    </td>"+
                "                                    <td width='25'> </td>"+
                "                                  </tr>"+
                "                                  <tr><td colspan='3' height='25'> </td></tr>"+
                "                                </table>"+
                "                              </td>"+
                "                            </tr>"+
                "                          </table>"+
                "                          </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"colums2v2\"><div class='movableContent'>"+
                "      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                      <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'>"+
                "                        <tr><td height=\"10\" colspan='3'> </td></tr>"+
                "                        <tr>"+
                "                          <td width='250'  valign='top' >"+
                "                            <table width='100%' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'  >"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                              <tr>"+
                "                                <td width='25'> </td>"+
                "                                <td class=\"newcontent\">"+
                "                              "+
                "                                </td>"+
                "                                <td width='25'> </td>"+
                "                              </tr>"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                            </table>"+
                "                          </td>"+
                ""+
                "                          <td width='20'> </td>"+
                "                          "+
                "                          <td width='250' valign='top' >"+
                "                            <table width='100%' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'  >"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                              <tr>"+
                "                                <td width='25'> </td>"+
                "                                <td class=\"newcontent\">"+
                "                              "+
                "                                </td>"+
                "                                <td width='25'> </td>"+
                "                              </tr>"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                            </table>"+
                "                          </td>"+
                "                        </tr>"+
                "                        "+
                "                      </table>"+
                "                      </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"colums3v2\"><div class='movableContent'>"+
                "      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>"+
                "                      <table width=\"520\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'>"+
                "                        <tr><td height=\"10\" colspan='5'> </td></tr>"+
                "                        <tr>"+
                "                          <td width='165'  valign='top' >"+
                "                            <table width='100%' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'  >"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                              <tr>"+
                "                                <td width='25'> </td>"+
                "                                <td class=\"newcontent\">"+
                "                              "+
                "                                </td>"+
                "                                <td width='25'> </td>"+
                "                              </tr>"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                            </table>"+
                "                          </td>"+
                ""+
                "                          <td width='12'> </td>"+
                "                          "+
                "                          <td width='165'  valign='top' >"+
                "                            <table width='100%' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'  >"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                              <tr>"+
                "                                <td width='25'> </td>"+
                "                                <td class=\"newcontent\">"+
                "                              "+
                "                                </td>"+
                "                                <td width='25'> </td>"+
                "                              </tr>"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                            </table>"+
                "                          </td>"+
                ""+
                "                          <td width='12'> </td>"+
                "                          "+
                "                          <td width='165'  valign='top' >"+
                "                            <table width='100%' border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" valign='top'  >"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                              <tr>"+
                "                                <td width='25'> </td>"+
                "                                <td class=\"newcontent\">"+
                "                              "+
                "                                </td>"+
                "                                <td width='25'> </td>"+
                "                              </tr>"+
                "                              <tr><td colspan='3' height='25'> </td></tr>"+
                "                            </table>"+
                "                          </td>"+
                "                        </tr>"+
                "                        "+
                "                      </table>"+
                "                      </td></tr></table>"+
                "                    </div>"+
                "      </div>"+
                ""+
                "      <div class=\"customZone\" data-type=\"textv2\">"+
                "        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"+
                "                            <tr><td height='10'> </td></tr>"+
                "                            <tr>"+
                "                              <td align='left' valign='top'>"+
                "                                <div class=\"contentEditableContainer contentTextEditable\">"+
                "                        <div class=\"contentEditable\" >"+
                "                                <h2>Weâ€™re going to blow your mind.</h2>"+
                "                                </div>"+
                "                      </div>"+
                "                              </td>"+
                "                            </tr>"+
                "                            <tr><td height='15'> </td></tr>"+
                "                            <tr>"+
                "                              <td align='left' valign='top'>"+
                "                                <div class=\"contentEditableContainer contentTextEditable\">"+
                "                        <div class=\"contentEditable\" >"+
                "                                <p >Lorem Ipsum is simply dummy text of the printing and typesetting industry. </p>"+
                "                                </div>"+
                "                      </div>"+
                "                              </td>"+
                "                            </tr>"+
                "                            <tr><td height='25'> </td></tr>"+
                "                            <tr>"+
                "                              <td align='right' valign='top'>"+
                "                                <div class=\"contentEditableContainer contentTextEditable\">"+
                "                        <div class=\"contentEditable\" >"+
                "                                <a target='_blank' href=\"#\" class='link1'>Read more â†’</a>"+
                "                                </div>"+
                "                      </div>"+
                "                              </td>"+
                "                            </tr>"+
                "                          </table>"+
                "      </div>"+
                ""+
                ""+
                ""+
                "    -->"+
                "    <!--Default Zone End-->"+
                ""+
                "    "+
                "      </body>"+
                "      </html>";


        EmailBody emailBody = new EmailBody();
        // emailBody.setEmail("jessica.buitrago@sii-group.co");
         //emailBody.setEmail("carlos.rodriguez2@assaabloy.com");
         emailBody.setEmail("alejandro.lindarte@assaabloy.com");
        // emailBody.setEmail("carlosjavier.tejerorojas@assaabloy.com");
        emailBody.setContent(myvar+"<h1>"+content+"</h1>");
        emailBody.setSubject(subject);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmailBody> request =
                new HttpEntity<>(emailBody, headers);
        RestTemplate template = new RestTemplate();
        template.postForObject(this.emailURI, request, String.class);
    }



}