package com.example.springboot.service;

import com.example.springboot.dto.AccessToken;
import com.example.springboot.email.EmailBody;
import com.example.springboot.enums.OrderStatus;
import com.example.springboot.utils.Constantes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.example.springboot.repository.QADRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
@Service
public class QADService
{
    @Autowired
    QADRepository repository;

    String emailURI = "http://localhost:8081/comerssia/notificaciones";

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public String getTGToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_TG_TOKEN + "?response_type=" +
                Constantes.RESPONSE_TYPE + "&client_id="+Constantes.APP_ID +
                "&redirect_uri="+Constantes.REDIRECT_URI;

        return template.getForEntity(url, String.class).getBody();

    }

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public AccessToken getAccessToken(String TGCode) throws Exception {
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

    }

    /**
     *
     * @return Response
     * @throws Exception excepciones del servicio
     */
    public AccessToken refreshAccessToken(String previousToken) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", Constantes.GRANT_REFRESH_TYPE);
        map.add("client_id", Constantes.APP_ID);
        map.add("client_secret", Constantes.CLIENT_SECRET);
        map.add("refresh_token", previousToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        RestTemplate template = new RestTemplate();
        String url = Constantes.URL_ACCESS_TOKEN;

        return template.postForObject(url, request, AccessToken.class);

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
        emailBody.setEmail("jessica.buitrago@sii-group.co");
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