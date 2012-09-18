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


import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.model.*;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.core.io.ClassPathResource;

/**
 * Tests {@link ${DHIS2ReportingService}}.
 */
public class DHIS2ReportingServiceDAOTest extends BaseModuleContextSensitiveTest
{

    private DHIS2ReportingService service;

    // sample data:  to do prepare a proper dataset
    private DataElement de1, de2;

    private Disaggregation _default;

    private DataValueTemplate dvt1, dvt2;

    private ReportDefinition rd1, rd2, rd3;

    @Before
    public void before() throws Exception
    {
        // executeDataSet(XML_DATASET_PATH + new TestUtil().getTestDatasetFilename(XML_HTML_FORM_ENTRY_SERVICE_DATASET));
        rd1 = new ReportDefinition();
        rd2 = new ReportDefinition();
        rd1.setCode( "DS_MORBIDITY" );
        rd2.setName( "Monthly Morbidity Report" );

        de1 = new DataElement();
        de1.setName( "Malaria cases" );
        de1.setCode( "DE1" );

        de2 = new DataElement();
        de2.setName( "STI cases" );
        de2.setCode( "DE2" );

        _default = new Disaggregation();
        _default.setName( "default" );
        _default.setCode( "default" );

        service = Context.getService( DHIS2ReportingService.class );
    }

    @Test
    public void dataElementDAOTest()
    {
        DataElement de = service.saveDataElement( de1 );
        assertEquals( de1.getName(), de.getName() );
        service.saveDataElement( de2 );

        Collection<DataElement> des = service.getAllDataElements();
        assertEquals( 2, des.size() );
    }

    @Test
    public void disAggregationDAOTest()
    {
        _default = service.saveDisaggregation( _default );
        assertNotNull( _default.getId() );
    }

    @Test
    public void dataValueSetDAOTest()
    {
        service.saveDataElement( de1 );
        service.saveDisaggregation( _default );

        dvt1 = new DataValueTemplate();
        dvt1.setDataelement( de1 );
        dvt1.setDisaggregation( _default );

        rd1.addDataValueTemplate( dvt1 );
        rd3 = service.saveReportDefinition( rd1 );

        // should fail
        service.purgeDataElement( de1 );

        assertEquals( rd1.getName(), rd3.getName() );
        Set<DataValueTemplate> dvTemplates = rd3.getDataValueTemplates();
        assertEquals( 1, dvTemplates.size() );
        assertTrue( dvTemplates.contains( dvt1 ) );
        for ( DataValueTemplate dv : dvTemplates )
        {
            System.out.println( dv.getDataelement() );
            System.out.println( dv.toString() );
        }
    }

    /**
     * TODO: re-enable this with proper test data
     */
    @Ignore
    @Test
    public void queryTest()
    {
        String query = "select count(*) from dhisreport_dataelement";
        MonthlyPeriod period = new MonthlyPeriod( new Date() );
        Location location = new Location();
        location.setId( 21 );

        service.saveDataElement( de1 );
        service.saveDataElement( de2 );

        dvt1 = new DataValueTemplate();
        dvt1.setQuery( query );

        assertEquals( "2", service.evaluateDataValueTemplate( dvt1, period, location ) );
    }

    @Test
    public void unMarshallandSaveReportTemplates() throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( ReportTemplates.class );
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ReportTemplates reportTemplates = (ReportTemplates) jaxbUnmarshaller.unmarshal( resource.getInputStream() );

        for (DataElement de : reportTemplates.getDataElements( ) )
        {
            service.saveDataElement( de );
        }
        for (Disaggregation disagg : reportTemplates.getDisaggregations())
        {
            service.saveDisaggregation( disagg );
        }
        for ( ReportDefinition rd : reportTemplates.getReportTemplates() )
        {
            for (DataValueTemplate dvt : rd.getDataValueTemplates())
            {
                dvt.setReportDefinition( rd );
            }
            service.saveReportDefinition( rd );
        }
    }
}
