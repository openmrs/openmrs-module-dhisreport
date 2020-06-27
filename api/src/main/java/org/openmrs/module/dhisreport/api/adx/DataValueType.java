package org.openmrs.module.dhisreport.api.adx;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * <p>Java class for dataValueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="annotation" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="dataElement" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;anyAttribute processContents='skip'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataValueType", propOrder = {"annotation"})
public class DataValueType {

	protected Object annotation;

	@XmlAttribute(name = "dataElement", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "token")
	protected String dataElement;

	@XmlAttribute(name = "value", required = true)
	protected BigDecimal value;

	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Gets the value of the annotation property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Object }
	 *     
	 */
	public Object getAnnotation() {
		return annotation;
	}

	/**
	 * Sets the value of the annotation property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link Object }
	 *     
	 */
	public void setAnnotation(Object value) {
		this.annotation = value;
	}

	/**
	 * Gets the value of the dataElement property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getDataElement() {
		return dataElement;
	}

	/**
	 * Sets the value of the dataElement property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setDataElement(String value) {
		this.dataElement = value;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link BigDecimal }
	 *     
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link BigDecimal }
	 *     
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed property on this class.
	 * 
	 * <p>
	 * the map is keyed by the name of the attribute and 
	 * the value is the string value of the attribute.
	 * 
	 * the map returned by this method is live, and you can add new attribute
	 * by updating the map directly. Because of this design, there's no setter.
	 * 
	 * 
	 * @return
	 *     always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

}
