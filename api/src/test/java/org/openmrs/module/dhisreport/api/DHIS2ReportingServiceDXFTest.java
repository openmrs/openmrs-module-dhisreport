/**
 *  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHIS2 Reporting module.
 *
 *  DHIS2 Reporting module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  DHIS2 Reporting module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with DHIS2 Reporting module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/
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
