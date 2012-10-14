/**
 * Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 * This file is part of DHISReporting module.
 *
 * Billing module is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Billing module is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Billing module. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 *
 */
package org.openmrs.module.dhisreport.api.dxf2;


import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.dom.DOMResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.InputSource;

/**
 *
 * @author bobj
 */
public class DataValueSetTest
{

    @Test
    public void marshallDataValueSet() throws Exception
    {
        DataValueSet dvset = new DataValueSet();
        dvset.setDataSet( "ANC" );
        dvset.setOrgUnit( "OU1" );
        dvset.setPeriod( "2012-09" );
        List<DataValue> dataValues = dvset.getDataValues();
        dataValues.add( new DataValue( "DE1", "45" ) );
        dataValues.add( new DataValue( "DE2", "45" ) );
        dataValues.add( new DataValue( "DE3", "r543efdgty2", "53" ) );

        Writer xmlWriter = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance( DataValueSet.class );

        Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
        jaxbmarshaller.marshal( dvset, xmlWriter );
        String xml = xmlWriter.toString();


        System.out.println( xml );
        assertEquals( "3", xpathTest( "count(//d:dataValue)", xml ) );
        assertEquals( "53", xpathTest( "//d:dataValue[3]/@value", xml ) );
        assertEquals( "r543efdgty2", xpathTest( "//d:dataValue[3]/@categoryOptionCombo", xml ) );

    }

    @Test
    public void unMarshallDataValueSet() throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "dvset.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( DataValueSet.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        DataValueSet dvset = (DataValueSet) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        assertEquals( 5, dvset.getDataValues().size() );
    }

    protected String xpathTest( String xpathString, String xml ) throws XPathExpressionException
    {
        InputSource source = new InputSource( new StringReader( xml ) );
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext( new Dxf2NamespaceResolver() );

        return (String) xpath.evaluate( xpathString, source );
    }

    // we need this to resolve dxf2 namespace in xpath
    protected class Dxf2NamespaceResolver implements NamespaceContext
    {

        @Override
        public String getNamespaceURI( String prefix )
        {
            if ( prefix == null )
            {
                throw new IllegalArgumentException( "No prefix provided!" );
            } else
            {
                if ( prefix.equals( "d" ) )
                {
                    return "http://dhis2.org/schema/dxf/2.0";
                } else
                {
                    return XMLConstants.NULL_NS_URI;
                }
            }
        }

        @Override
        public String getPrefix( String namespaceURI )
        {
            // Not needed in this context.
            return null;
        }

        @Override
        public Iterator getPrefixes( String namespaceURI )
        {
            // Not needed in this context.
            return null;
        }
    }
}
