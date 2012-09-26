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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.core.io.ClassPathResource;

/**
 * Tests {@link ${DHIS2ReportingService}}.
 */
public class DHIS2ReportingServiceDXFTest extends BaseModuleContextSensitiveTest
{

    private DHIS2ReportingService  service;
	
	@Before
	public void before() throws Exception {
		// executeDataSet(XML_DATASET_PATH + new TestUtil().getTestDatasetFilename(XML_HTML_FORM_ENTRY_SERVICE_DATASET));
		service = Context.getService( DHIS2ReportingService.class );
	}

    @Test
    public void setServerParamsTest() throws MalformedURLException
    {
        HttpDhis2Server server = new HttpDhis2Server();
        server.setUsername( "admin" );
        server.setPassword( "district" );
        server.setUrl( new URL( "http://apps.dhis2.org/dev" ) );

        service.setDhis2Server( server );
    }

    @Ignore
    @Test
    public void postDhisReportTest() throws Exception
    {
        HttpDhis2Server server = new HttpDhis2Server();
        server.setUsername( "admin" );
        server.setPassword( "district" );
        server.setUrl( new URL( "http://apps.dhis2.org/dev" ) );

        service.setDhis2Server( server );
        ClassPathResource resource = new ClassPathResource( "dvset.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( DataValueSet.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        DataValueSet dvset = (DataValueSet) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        ImportSummary summary = service.postDataValueSet( dvset );
        JAXBContext importSummaryContext = JAXBContext.newInstance( ImportSummary.class );

        Marshaller jaxbmarshaller = importSummaryContext.createMarshaller();
        jaxbmarshaller.marshal( summary, System.out);
     }
}
