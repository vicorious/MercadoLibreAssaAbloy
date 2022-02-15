package com.example.springboot.wsdl;

import com.example.springboot.utils.Constantes;
import com.example.springboot.wsdl.mapping.*;

import com.example.springboot.wsdl.requesreportes.DATOS;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import org.springframework.ws.soap.client.core.SoapActionCallback;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.example.springboot.wsdl.request.Transaction;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.xml.bind.DatatypeConverter;
import javax.xml.soap.MessageFactory;
import javax.xml.ws.Response;
import java.util.Base64;

public class ConsumirComerssia {

  private static final Logger log = LoggerFactory.getLogger(ConsumirComerssia.class);

  /**
   * 
   * @param pi_sIdemp
   * @param pi_sEnvio
   * @return
   */
  @Bean
  public WmEnvioTransaccionesResponse envioTransacciones(String pi_sIdemp, Transaction pi_sEnvio) throws Exception
  {
      String callBack = "http://tempuri.org/wm_EnvioTransacciones";
      XmlMapper xmlMapper = new XmlMapper();
      xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
      xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      String xml = xmlMapper.writeValueAsString(pi_sEnvio);

      log.info("XML: "+ xml);
      String decodedValue = Base64.getEncoder().encodeToString(xml.getBytes());
      log.info("BASE64: "+ decodedValue);

      WmEnvioTransacciones envioTransacciones = new WmEnvioTransacciones();
      envioTransacciones.setPiSIdemp(pi_sIdemp);
      envioTransacciones.setPiSEnvio(decodedValue.getBytes());

      SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory(
              MessageFactory.newInstance());
      messageFactory.afterPropertiesSet();

      WebServiceTemplate webServiceTemplate = new WebServiceTemplate(
              messageFactory);
      Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

      marshaller.setContextPath("com.example.springboot.wsdl.mapping");
      marshaller.afterPropertiesSet();

      webServiceTemplate.setMarshaller(marshaller);
      webServiceTemplate.setUnmarshaller(marshaller);
      webServiceTemplate.afterPropertiesSet();

      WmEnvioTransaccionesResponse response = (WmEnvioTransaccionesResponse) webServiceTemplate
              .marshalSendAndReceive(Constantes.WSDL, envioTransacciones,
                      new SoapActionCallback(
                              callBack));

      return response;

  }

    /**
     *
     * @param datos
     * @return
     * @throws JsonProcessingException
     */
  public WmReporteResponse reportes(DATOS datos) throws Exception {
      String callBackReportes = "http://localhost:8081/servicioqueespererespuesta";
      XmlMapper xmlMapper = new XmlMapper();
      String xml = xmlMapper.writeValueAsString(datos);
      xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
      xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      log.info("XML REPORTE: "+ xml);
      WmReporte wmReporte = new WmReporte();
      wmReporte.setPiSEntrada(xml);

      SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory(
              MessageFactory.newInstance());
      messageFactory.afterPropertiesSet();

      WebServiceTemplate webServiceTemplate = new WebServiceTemplate(
              messageFactory);
      Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

      marshaller.setContextPath("com.example.springboot.wsdl.mapping");
      marshaller.afterPropertiesSet();

      webServiceTemplate.setMarshaller(marshaller);
      webServiceTemplate.setUnmarshaller(marshaller);
      webServiceTemplate.afterPropertiesSet();

      Response response = (Response) webServiceTemplate
              .marshalSendAndReceive(Constantes.WSDL, wmReporte,
                      new SoapActionCallback(
                              callBackReportes));

      WmReporteResponse responseF = (WmReporteResponse) response;

      return responseF;

  }
  

}