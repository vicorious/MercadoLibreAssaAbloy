//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.02.14 at 11:16:22 AM COT 
//


package com.example.springboot.wsdl.mapping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="pi_sIdemp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pi_sNombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pi_sXml" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;element name="pi_sPlano" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "piSIdemp",
    "piSNombre",
    "piSXml",
    "piSPlano"
})
@XmlRootElement(name = "wm_CargaArchivoPlano")
public class WmCargaArchivoPlano {

    @XmlElement(name = "pi_sIdemp")
    protected String piSIdemp;
    @XmlElement(name = "pi_sNombre")
    protected String piSNombre;
    @XmlElement(name = "pi_sXml")
    protected byte[] piSXml;
    @XmlElement(name = "pi_sPlano")
    protected byte[] piSPlano;

    /**
     * Gets the value of the piSIdemp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPiSIdemp() {
        return piSIdemp;
    }

    /**
     * Sets the value of the piSIdemp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPiSIdemp(String value) {
        this.piSIdemp = value;
    }

    /**
     * Gets the value of the piSNombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPiSNombre() {
        return piSNombre;
    }

    /**
     * Sets the value of the piSNombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPiSNombre(String value) {
        this.piSNombre = value;
    }

    /**
     * Gets the value of the piSXml property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPiSXml() {
        return piSXml;
    }

    /**
     * Sets the value of the piSXml property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPiSXml(byte[] value) {
        this.piSXml = value;
    }

    /**
     * Gets the value of the piSPlano property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPiSPlano() {
        return piSPlano;
    }

    /**
     * Sets the value of the piSPlano property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPiSPlano(byte[] value) {
        this.piSPlano = value;
    }

}
