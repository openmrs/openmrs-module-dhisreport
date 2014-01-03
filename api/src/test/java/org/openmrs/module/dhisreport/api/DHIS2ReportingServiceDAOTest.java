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

import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.model.*;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.core.io.ClassPathResource;

/**
 * Tests {@link $ DHIS2ReportingService} .
 */
public class DHIS2ReportingServiceDAOTest
    extends BaseModuleContextSensitiveTest
{

    private DHIS2ReportingService service;

    // uids of sample data
    private static final String DE_ANC1_UID = "nvVDDkfbbhf";

    private static final String DE_ANC4_UID = "OWeOBFxrvrv";

    private static final String RT_POPULATION_UID = "Az7yGdS293Y";

    private static final String DE_POP_UID = "xX6RDH6AZTK";

    private Disaggregation _default;

    private ReportDefinition rd;

    @Before
    public void before()
        throws Exception
    {
        _default = new Disaggregation();
        _default.setName( "default" );
        _default.setCode( "default" );

        service = Context.getService( DHIS2ReportingService.class );
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        service.unMarshallandSaveReportTemplates( resource.getInputStream() );
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
        de1.setName( "Something else" );
        service.saveDataElement( de2 );

        Collection<DataElement> des = service.getAllDataElements();
        assertEquals( 5, des.size() );

    }

    @Test
    public void disAggregationDAOTest()
    {
        assertEquals( 3, service.getAllDataElements().size() );
        assertEquals( 3, service.getAllDisaggregations().size() );
        assertEquals( 2, service.getAllReportDefinitions().size() );
    }

    @Test
    public void dataValueSetDAOTest()
    {
        DataElement popDe = service.getDataElementByUid( DE_POP_UID );
        // should fail
        // service.purgeDataElement( de1 );
        assertNotNull( service.getAllReportDefinitions() );

        rd = service.getReportDefinitionByUId( RT_POPULATION_UID );
        Collection<DataValueTemplate> dvTemplates = rd.getDataValueTemplates();
        assertEquals( 2, dvTemplates.size() );
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
    public void unMarshallandSaveReportTemplates()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        service.unMarshallandSaveReportTemplates( resource.getInputStream() );
    }

    @Test
    public void marshallerTest()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        service.unMarshallandSaveReportTemplates( resource.getInputStream() );

        ReportTemplates rds = service.getReportTemplates();
        assertEquals( 3, rds.getDataElements().size() );
        assertEquals( 3, rds.getDisaggregations().size() );
        assertEquals( 2, rds.getReportDefinitions().size() );

        service.marshallReportTemplates( System.out, rds );
    }
}
