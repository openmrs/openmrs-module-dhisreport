package org.openmrs.module.dhisreport.api.adx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * <p>Java class for groupType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="groupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="dataValue" type="{urn:ihe:qrph:adx:2015}dataValueType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="orgUnit" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute name="period" use="required" type="{urn:ihe:qrph:adx:2015}periodType" />
 *       &lt;anyAttribute processContents='skip'/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "groupType", propOrder = { "dataValue" } )
public class GroupType
{

    @XmlElement( required = true )
    protected List<DataValueType> dataValue;

    @XmlAttribute( name = "orgUnit", required = true )
    @XmlJavaTypeAdapter( CollapsedStringAdapter.class )
    @XmlSchemaType( name = "token" )
    protected String orgUnit;

    @XmlAttribute( name = "period", required = true )
    protected String period;

    @XmlAttribute( name = "idScheme", required = false )
    protected String idScheme;

    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the dataValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataValueType }
     * 
     * 
     */
    public List<DataValueType> getDataValue()
    {
        if ( dataValue == null )
        {
            dataValue = new ArrayList<DataValueType>();
        }
        return this.dataValue;
    }

    /**
     * Gets the value of the orgUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrgUnit()
    {
        return orgUnit;
    }

    /**
     * Sets the value of the orgUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrgUnit( String value )
    {
        this.orgUnit = value;
    }

    /**
     * Gets the value of the period property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriod()
    {
        return period;
    }

    /**
     * Sets the value of the period property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriod( String value )
    {
        this.period = value;
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
    public Map<QName, String> getOtherAttributes()
    {
        return otherAttributes;
    }

    public String getIdScheme()
    {
        return idScheme;
    }

    public void setIdScheme( String idScheme )
    {
        this.idScheme = idScheme;
    }

}
