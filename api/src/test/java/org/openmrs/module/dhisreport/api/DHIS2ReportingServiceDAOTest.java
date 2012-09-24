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

    // uids of sample data
    private static final String DE_ANC1_UID="nvVDDkfbbhf";
    private static final String DE_ANC4_UID="OWeOBFxrvrv";
    private static final String RT_POPULATION_UID="Az7yGdS293Y";
    private static final String DE_POP_UID = "xX6RDH6AZTK";
    
    private Disaggregation _default;
    private ReportDefinition rd;
    
    @Before
    public void before() throws Exception
    {
        _default = new Disaggregation();
        _default.setName( "default" );
        _default.setCode( "default" );

        service = Context.getService( DHIS2ReportingService.class );
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        service.unMarshallandSaveReportTemplates( resource.getInputStream());
    }

    @Test
    public void dataElementDAOTest()
    {
        DataElement de1 = new DataElement();
        de1.setName( "Malaria cases" );
        de1.setCode( "DE1" );
        de1.setUid( "ghggyugugug" );

        DataElement de2 = new DataElement();
        de2.setName( "STI cases" );
        de2.setCode( "DE2" );
        de2.setUid( "ghrwyugugpo" );
        
        DataElement de = service.saveDataElement( de1 );
        assertEquals( de1.getName(), de.getName() );
        service.saveDataElement( de2 );
        de1.setName( "Something else");
        service.saveDataElement( de2 );

        Collection<DataElement> des = service.getAllDataElements();
        assertEquals( 5, des.size() );
        
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
        DataElement popDe = service.getDataElementByUid( DE_POP_UID);
        // should fail
        //service.purgeDataElement( de1 );

        rd = service.getReportDefinitionByUId( DE_POP_UID );
        Set<DataValueTemplate> dvTemplates = rd.getDataValueTemplates();
        assertEquals( 1, dvTemplates.size() );
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
    }

    @Test
    public void unMarshallandSaveReportTemplates() throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        service.unMarshallandSaveReportTemplates( resource.getInputStream());
    }
}
