package org.openmrs.module.dhisreport.api.adx;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.ihe.qrph.adx package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory
{

    private final static QName _Adx_QNAME = new QName( "urn:ihe:qrph:adx:2015", "adx" );

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.ihe.qrph.adx
     * 
     */
    public ObjectFactory()
    {
    }

    /**
     * Create an instance of {@link AdxType }
     * 
     */
    public AdxType createAdxType()
    {
        return new AdxType();
    }

    /**
     * Create an instance of {@link DataValueType }
     * 
     */
    public DataValueType createDataValueType()
    {
        return new DataValueType();
    }

    /**
     * Create an instance of {@link GroupType }
     * 
     */
    public GroupType createGroupType()
    {
        return new GroupType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxType }{@code >}}
     * 
     */
    @XmlElementDecl( namespace = "urn:ihe:qrph:adx:2015", name = "adx" )
    public JAXBElement<AdxType> createAdx( AdxType value )
    {
        return new JAXBElement<AdxType>( _Adx_QNAME, AdxType.class, null, value );
    }

}
