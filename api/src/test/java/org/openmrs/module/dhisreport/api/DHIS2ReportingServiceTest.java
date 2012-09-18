/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.dhisreport.api;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.hisp.dhis.dxf2.datavalueset.DataValue;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.dhis.DhisException;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.core.io.ClassPathResource;

/**
 * Tests {@link ${DHIS2ReportingService}}.
 */
public class DHIS2ReportingServiceTest extends BaseModuleContextSensitiveTest
{

    @Test
    public void shouldSetupContext()
    {
        assertNotNull( Context.getService( DHIS2ReportingService.class ) );
    }

    @Test
    public void setServerParamsTest() throws MalformedURLException
    {
        String user = "system";
        String pass = "System123";
        URL url = new URL( "http://apps.dhis2.org/dev" );

        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        service.setDHISParams( url, user, pass );
    }

    @Ignore
    @Test
    public void postDhisReportTest() throws Exception
    {
        String user = "system";
        String pass = "System123";
        URL url = new URL( "http://apps.dhis2.org/dev" );

        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        service.setDHISParams( url, user, pass );
        ClassPathResource resource = new ClassPathResource( "dvset.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( DataValueSet.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        DataValueSet dvset = (DataValueSet) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        ImportSummary summary = service.postDataValueSet( dvset );
        JAXBContext importSummaryContext = JAXBContext.newInstance( ImportSummary.class );

        Marshaller jaxbmarshaller = importSummaryContext.createMarshaller();
        jaxbmarshaller.marshal( summary, System.out);
     }
    
    @Test
    public void dataElementDAOTest()
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        
        DataElement de = new DataElement();
        de.setName( "Malaria cases");
        de.setCode( "DE1");
        
        DataElement de2 = service.saveDataElement( de );
        assertEquals(de.getName(), de2.getName());
    }
}
