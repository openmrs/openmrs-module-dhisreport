/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHISReporting module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.dhisreport.api.dxf2;


import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author bobj
 */
public class DataValueSetTest {
    @Test
    public void marshallDataValueSet() throws Exception
    {
        DataValueSet dvset = new DataValueSet();
        dvset.setDataSet( "ANC");
        dvset.setOrgUnit( "OU1");
        dvset.setPeriod( "2012-09");
        List<DataValue> dataValues = dvset.getDataValues();
        dataValues.add( new DataValue("DE1", "45"));
        dataValues.add( new DataValue("DE2", "45"));
        dataValues.add( new DataValue("DE3", "r543efdgty2", "53"));
        
        Writer xmlWriter = new StringWriter();
        
        JAXBContext jaxbContext = JAXBContext.newInstance( DataValueSet.class );

        Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
        jaxbmarshaller.marshal( dvset, xmlWriter);
        String xml = xmlWriter.toString();
        
        System.out.println(xml);
        // TODO :  some xpath tests on this
        StringReader xmlReader = new StringReader(xml);
        
    }
    
    @Test
    public void unMarshallDataValueSet() throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "dvset.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( DataValueSet.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        DataValueSet dvset = (DataValueSet) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        assertEquals(5, dvset.getDataValues().size());
    }

}
