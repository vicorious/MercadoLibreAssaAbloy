package com.example.springboot.service;

import com.example.springboot.dto.AccessToken;
import com.example.springboot.email.EmailBody;
import com.example.springboot.enums.OrderStatus;
import com.example.springboot.utils.Constantes;
import com.sun.net.httpserver.HttpContext;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.client.HttpClient;
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

import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class QADService
{

    String emailURI = "http://localhost:8081/comerssia/notificaciones";

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public String getTGToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> params = new HashMap<>();
        params.put("response_type", Constantes.RESPONSE_TYPE);
        params.put("client_id", Constantes.APP_ID);
        params.put("redirect_uri", Constantes.REDIRECT_URI);

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
        headersResponse.get("Location");


        return response.getBody();

    }

    /**
     *
     * @return
     * @throws Exception
     */
    public String getDummyTGToken() throws Exception {
        return "TG-5b9032b4e23464aed1f959f-1234567";

    }

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public AccessToken getAccessToken(String TGCode) throws RefreshTokenException {
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
            map.add("grant_type", Constantes.GRANT_TYPE);
            map.add("client_id", Constantes.APP_ID);
            map.add("client_secret", Constantes.CLIENT_SECRET);
            map.add("code", TGCode);
            map.add("redirect_uri", Constantes.REDIRECT_URI);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

            RestTemplate template = new RestTemplate();
            String url = Constantes.URL_ACCESS_TOKEN;

            return template.postForObject(url, request, AccessToken.class);
        }catch(Exception ex)
        {
            this.email(ex.getMessage() + "<H1>SE INTENTARA CON EL REFRESHTOKEN</H1>", "Error en access token");
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
        return "{\n" +
                "  \"query\": \"\",\n" +
                "  \"display\": \"complete\",\n" +
                "  \"paging\": {\n" +
                "    \"total\": 1,\n" +
                "    \"offset\": 0,\n" +
                "    \"limit\": 50\n" +
                "  },\n" +
                "  \"results\": [\n" +
                "    {\n" +
                "      \"id\": 1068825849,\n" +
                "      \"comments\": null,\n" +
                "      \"status\": \"paid\",\n" +
                "      \"status_detail\": {\n" +
                "        \"description\": null,\n" +
                "        \"code\": null\n" +
                "      },\n" +
                "      \"date_created\": \"2016-02-25T15:53:38.000-04:00\",\n" +
                "      \"date_closed\": \"2016-02-25T15:53:37.000-04:00\",\n" +
                "      \"expiration_date\": \"2016-03-17T15:53:38.000-04:00\",\n" +
                "      \"date_last_updated\": \"2016-02-25T15:55:44.973Z\",\n" +
                "      \"hidden_for_seller\": false,\n" +
                "      \"currency_id\": \"ARS\",\n" +
                "      \"order_items\": [\n" +
                "        {\n" +
                "          \"currency_id\": \"ARS\",\n" +
                "          \"item\": {\n" +
                "            \"id\": \"MLA607850752\",\n" +
                "            \"title\": \"Item De Testeo, Por Favor No Ofertar --kc:off\",\n" +
                "            \"seller_custom_field\": null,\n" +
                "            \"variation_attributes\": [\n" +
                "            ],\n" +
                "            \"category_id\": \"MLA3530\",\n" +
                "            \"variation_id\": null\n" +
                "          },\n" +
                "          \"sale_fee\": 1.05,\n" +
                "          \"quantity\": 1,\n" +
                "          \"unit_price\": 10\n" +
                "        }\n" +
                "      ],\n" +
                "      \"total_amount\": 10,\n" +
                "      \"mediations\": [\n" +
                "      ],\n" +
                "      \"payments\": [\n" +
                "        {\n" +
                "          \"id\": 1833868697,\n" +
                "          \"order_id\": 2000003508419013,\n" +
                "          \"payer_id\": 207040551,\n" +
                "          \"collector\": {\n" +
                "            \"id\": 207035636\n" +
                "          },\n" +
                "          \"currency_id\": \"ARS\",\n" +
                "          \"status\": \"approved\",\n" +
                "          \"status_code\": \"0\",\n" +
                "          \"status_detail\": \"accredited\",\n" +
                "          \"transaction_amount\": 10,\n" +
                "          \"shipping_cost\": 0,\n" +
                "          \"overpaid_amount\": 0,\n" +
                "          \"total_paid_amount\": 10,\n" +
                "          \"marketplace_fee\": null,\n" +
                "          \"coupon_amount\": 0,\n" +
                "          \"date_created\": \"2016-02-25T15:55:42.000-04:00\",\n" +
                "          \"date_last_modified\": \"2016-02-25T15:55:42.000-04:00\",\n" +
                "          \"card_id\": null,\n" +
                "          \"reason\": \"Item De Testeo, Por Favor No Ofertar --kc:off\",\n" +
                "          \"activation_uri\": null,\n" +
                "          \"payment_method_id\": \"diners\",\n" +
                "          \"installments\": 9,\n" +
                "          \"issuer_id\": \"1028\",\n" +
                "          \"atm_transfer_reference\": {\n" +
                "            \"company_id\": null,\n" +
                "            \"transaction_id\": null\n" +
                "          },\n" +
                "          \"coupon_id\": null,\n" +
                "          \"operation_type\": \"regular_payment\",\n" +
                "          \"payment_type\": \"credit_card\",\n" +
                "          \"available_actions\": [\n" +
                "          ],\n" +
                "          \"installment_amount\": 1.11,\n" +
                "          \"deferred_period\": null,\n" +
                "          \"date_approved\": \"2016-02-25T15:55:42.000-04:00\",\n" +
                "          \"authorization_code\": \"1234567\",\n" +
                "          \"transaction_order_id\": \"1234567\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"shipping\": {\n" +
                "        \"substatus\": null,\n" +
                "        \"status\": \"to_be_agreed\",\n" +
                "        \"id\": null,\n" +
                "        \"service_id\": null,\n" +
                "        \"currency_id\": null,\n" +
                "        \"shipping_mode\": null,\n" +
                "        \"shipment_type\": null,\n" +
                "        \"sender_id\": null,\n" +
                "        \"picking_type\": null,\n" +
                "        \"date_created\": null,\n" +
                "        \"cost\": null,\n" +
                "        \"date_first_printed\": null\n" +
                "      },\n" +
                "      \"buyer\": {\n" +
                "        \"id\": 207040551,\n" +
                "        \"nickname\": \"TETE5029382\",\n" +
                "        \"email\": \"test_user_97424966@testuser.com\",\n" +
                "        \"phone\": {\n" +
                "          \"area_code\": \"01\",\n" +
                "          \"number\": \"1111-1111\",\n" +
                "          \"extension\": \"\",\n" +
                "          \"verified\": false\n" +
                "        },\n" +
                "        \"alternative_phone\": {\n" +
                "          \"area_code\": \"\",\n" +
                "          \"number\": \"\",\n" +
                "          \"extension\": \"\"\n" +
                "        },\n" +
                "        \"first_name\": \"Test\",\n" +
                "        \"last_name\": \"Test\",\n" +
                "        \"billing_info\": {\n" +
                "          \"doc_type\": null,\n" +
                "          \"doc_number\": null\n" +
                "        }\n" +
                "      },\n" +
                "      \"seller\": {\n" +
                "        \"id\": 207035636,\n" +
                "        \"nickname\": \"TETE9544096\",\n" +
                "        \"email\": \"test_user_50828007@testuser.com\",\n" +
                "        \"phone\": {\n" +
                "          \"area_code\": \"01\",\n" +
                "          \"number\": \"1111-1111\",\n" +
                "          \"extension\": \"\",\n" +
                "          \"verified\": false\n" +
                "        },\n" +
                "        \"alternative_phone\": {\n" +
                "          \"area_code\": \"\",\n" +
                "          \"number\": \"\",\n" +
                "          \"extension\": \"\"\n" +
                "        },\n" +
                "        \"first_name\": \"Test\",\n" +
                "        \"last_name\": \"Test\"\n" +
                "      },\n" +
                "      \"feedback\": {\n" +
                "        \"sale\": null,\n" +
                "        \"purchase\": null\n" +
                "      },\n" +
                "      \"tags\": [\n" +
                "        \"not_delivered\",\n" +
                "        \"paid\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"sort\": {\n" +
                "    \"id\": \"date_asc\",\n" +
                "    \"name\": \"Date ascending\"\n" +
                "  },\n" +
                "  \"available_sorts\": [\n" +
                "    {\n" +
                "      \"id\": \"date_desc\",\n" +
                "      \"name\": \"Date descending\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"filters\": [\n" +
                "  ],\n" +
                "  \"available_filters\": [\n" +
                "    {\n" +
                "      \"id\": \"order.status\",\n" +
                "      \"name\": \"Order Status\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"paid\",\n" +
                "          \"name\": \"Order Paid\",\n" +
                "          \"results\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"confirmed\",\n" +
                "          \"name\": \"Order Confirmed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"payment_in_process\",\n" +
                "          \"name\": \"Payment in Process\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"payment_required\",\n" +
                "          \"name\": \"Payment Required\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"cancelled\",\n" +
                "          \"name\": \"Order Cancelled\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"invalid\",\n" +
                "          \"name\": \"Invalid\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"shipping.status\",\n" +
                "      \"name\": \"Shipping Status\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"to_be_agreed\",\n" +
                "          \"name\": \"To be agreed\",\n" +
                "          \"results\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"pending\",\n" +
                "          \"name\": \"Pending\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"handling\",\n" +
                "          \"name\": \"Handling\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"ready_to_ship\",\n" +
                "          \"name\": \"Ready to ship\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"shipped\",\n" +
                "          \"name\": \"Shipped\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"delivered\",\n" +
                "          \"name\": \"Delivered\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_delivered\",\n" +
                "          \"name\": \"Not delivered\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_verified\",\n" +
                "          \"name\": \"Not verified\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"cancelled\",\n" +
                "          \"name\": \"Cancelled\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"closed\",\n" +
                "          \"name\": \"Closed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"error\",\n" +
                "          \"name\": \"Error\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"active\",\n" +
                "          \"name\": \"Active\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_specified\",\n" +
                "          \"name\": \"Not specified\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"stale_ready_to_ship\",\n" +
                "          \"name\": \"Stale ready to ship\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"stale_shipped\",\n" +
                "          \"name\": \"Stale shipped\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"feedback.sale.rating\",\n" +
                "      \"name\": \"Feedback rating\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"negative\",\n" +
                "          \"name\": \"Negative\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"neutral\",\n" +
                "          \"name\": \"Neutral\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"positive\",\n" +
                "          \"name\": \"Positive\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"feedback.purchase.rating\",\n" +
                "      \"name\": \"Feedback rating\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"negative\",\n" +
                "          \"name\": \"Negative\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"neutral\",\n" +
                "          \"name\": \"Neutral\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"positive\",\n" +
                "          \"name\": \"Positive\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"feedback.sale.fulfilled\",\n" +
                "      \"name\": \"Feedback sale fulfilled\",\n" +
                "      \"type\": \"boolean\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"F\",\n" +
                "          \"name\": \"Transaction was aborted\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"T\",\n" +
                "          \"name\": \"Transaction actually happened\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"feedback.purchase.fulfilled\",\n" +
                "      \"name\": \"Feedback purchase fulfilled\",\n" +
                "      \"type\": \"boolean\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"F\",\n" +
                "          \"name\": \"Transaction was aborted\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"T\",\n" +
                "          \"name\": \"Transaction actually happened\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"shipping.service_id\",\n" +
                "      \"name\": \"Shipping Service\",\n" +
                "      \"type\": \"long\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"61\",\n" +
                "          \"name\": \"Estándar\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"64\",\n" +
                "          \"name\": \"Prioritario\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"151\",\n" +
                "          \"name\": \"Estándar\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"154\",\n" +
                "          \"name\": \"Prioritario\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"321\",\n" +
                "          \"name\": \"Colecta Retiro Sucursal\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"341\",\n" +
                "          \"name\": \"Estándar\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"251\",\n" +
                "          \"name\": \"Otros\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"63\",\n" +
                "          \"name\": \"Estándar\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"153\",\n" +
                "          \"name\": \"Estándar\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"351\",\n" +
                "          \"name\": \"Prioritario\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"311\",\n" +
                "          \"name\": \"Colecta Normal\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"381\",\n" +
                "          \"name\": \"Prioritario\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"62\",\n" +
                "          \"name\": \"Prioritario\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"312\",\n" +
                "          \"name\": \"Colecta Express\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"152\",\n" +
                "          \"name\": \"Estándar\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"81\",\n" +
                "          \"name\": \"Moto Express\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"shipping.substatus\",\n" +
                "      \"name\": \"Shipping Substatus\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"cost_exceeded\",\n" +
                "          \"name\": \"Cost exceeded\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"regenerating\",\n" +
                "          \"name\": \"Regenerating\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"waiting_for_label_generation\",\n" +
                "          \"name\": \"Waiting for label generation\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"ready_to_print\",\n" +
                "          \"name\": \"Ready to print\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"invoice_pending\",\n" +
                "          \"name\": \"Invoice pending\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"printed\",\n" +
                "          \"name\": \"Printed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"in_pickup_list\",\n" +
                "          \"name\": \"In pikcup list\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"ready_for_pkl_creation\",\n" +
                "          \"name\": \"Ready for pkl creation\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"ready_for_pickup\",\n" +
                "          \"name\": \"Ready for pickup\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"picked_up\",\n" +
                "          \"name\": \"Picked up\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"stale\",\n" +
                "          \"name\": \"Stale shipped\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"in_hub\",\n" +
                "          \"name\": \"In hub\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"measures_ready\",\n" +
                "          \"name\": \"Measures and weight ready\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"waiting_for_carrier_authorization\",\n" +
                "          \"name\": \"Waiting for carrier authorization\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"authorized_by_carrier\",\n" +
                "          \"name\": \"Authorized by carrier\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"in_plp\",\n" +
                "          \"name\": \"In PLP\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"delayed\",\n" +
                "          \"name\": \"Delayed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"waiting_for_withdrawal\",\n" +
                "          \"name\": \"Waiting for withdrawal\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"contact_with_carrier_required\",\n" +
                "          \"name\": \"Contact with carrier required\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"receiver_absent\",\n" +
                "          \"name\": \"Receiver absent\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"reclaimed\",\n" +
                "          \"name\": \"Reclaimed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_localized\",\n" +
                "          \"name\": \"Not localized\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"forwarded_to_third\",\n" +
                "          \"name\": \"Forwarded to third party\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"soon_deliver\",\n" +
                "          \"name\": \"Soon deliver\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"refused_delivery\",\n" +
                "          \"name\": \"Delivery refused\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"bad_address\",\n" +
                "          \"name\": \"Bad address\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"negative_feedback\",\n" +
                "          \"name\": \"Stale shipped forced to not delivered due to negative feedback by buyer\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"need_review\",\n" +
                "          \"name\": \"Need to review carrier status to understand what happened\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"damaged\",\n" +
                "          \"name\": \"damaged\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"fulfilled_feedback\",\n" +
                "          \"name\": \"Fulfilled by buyer feedback\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"no_action_taken\",\n" +
                "          \"name\": \"No action taken by buyer\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"double_refund\",\n" +
                "          \"name\": \"Double Refund\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"returning_to_sender\",\n" +
                "          \"name\": \"Returning to sender\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"retained\",\n" +
                "          \"name\": \"Retained\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"stolen\",\n" +
                "          \"name\": \"Stolen\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"returned\",\n" +
                "          \"name\": \"Returned\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"confiscated\",\n" +
                "          \"name\": \"confiscated\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"to_review\",\n" +
                "          \"name\": \"Closed shipment\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"destroyed\",\n" +
                "          \"name\": \"Destroyed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"claimed_me\",\n" +
                "          \"name\": \"Stale shipped with claim that was forced to not delivered\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"feedback.status\",\n" +
                "      \"name\": \"Feedback Status\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"pending\",\n" +
                "          \"name\": \"Waiting for your feedback\",\n" +
                "          \"results\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"waiting_buyer\",\n" +
                "          \"name\": \"Waiting for Buyer's feedback\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"tags\",\n" +
                "      \"name\": \"Tags\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"paid\",\n" +
                "          \"name\": \"Order Paid\",\n" +
                "          \"results\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_delivered\",\n" +
                "          \"name\": \"Not Delivered\",\n" +
                "          \"results\": 1\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"delivered\",\n" +
                "          \"name\": \"Delivered\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_paid\",\n" +
                "          \"name\": \"Order Not Paid\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"claim_closed\",\n" +
                "          \"name\": \"Claim Closed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"claim_opened\",\n" +
                "          \"name\": \"Claim Opened\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"not_processed\",\n" +
                "          \"name\": \"Not processed order\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"processed\",\n" +
                "          \"name\": \"Processed order\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": \"mediations.status\",\n" +
                "      \"name\": \"Mediation Status\",\n" +
                "      \"type\": \"text\",\n" +
                "      \"values\": [\n" +
                "        {\n" +
                "          \"id\": \"claim_opened\",\n" +
                "          \"name\": \"Claim opened\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"claim_closed\",\n" +
                "          \"name\": \"Claim closed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"dispute_opened\",\n" +
                "          \"name\": \"Dispute opened\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"dispute_closed\",\n" +
                "          \"name\": \"Dispute closed\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"seller_dispute_opened\",\n" +
                "          \"name\": \"Seller dispute opened\",\n" +
                "          \"results\": 0\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"seller_dispute_closed\",\n" +
                "          \"name\": \"Seller dispute closed\",\n" +
                "          \"results\": 0\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
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
        //emailBody.setEmail("jessica.buitrago@sii-group.co");
        emailBody.setEmail("alejandro.lindarte@sii-group.co");
        //emailBody.setEmail("carlosjavier.tejerorojas@assaabloy.com");
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