package org.openmrs.module.dhisreport.api.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dxf2.DataValue;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.SimplePatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.core.io.ClassPathResource;

/**
 * Tests {@link $DHIS2ReportingService} .
 */
public class DHIS2ReportingServiceImplTest
    extends
    BaseModuleContextSensitiveTest
{

    protected static final String INITIAL_OBS_XML = "db/ObsServiceTest-initial.xml";

    protected final Log log = LogFactory.getLog( getClass() );

    DHIS2ReportingService dhis2ReportingService = null;

    @Before
    public void before()
    {
        BasicConfigurator.configure();
        dhis2ReportingService = Context.getService( DHIS2ReportingService.class );
    }

    /**
     * @see DHIS2ReportingServiceImpl#evaluateReportDefinition(ReportDefinition,Period,Location)
     * @verifies evaluate Report Definition
     */
    @Test
    public void evaluateReportDefinition_shouldEvaluateReportDefinition()
        throws Exception
    {

        ClassPathResource resource = new ClassPathResource( "reportDefinition.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( ReportTemplates.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ReportTemplates reportTemplates = (ReportTemplates) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        List<ReportDefinition> rds = reportTemplates.getReportDefinitions();
        String timeperiod = "2016-06-28";
        Location location = new Location();
        location.setName( "County General" );
        location.setDescription( "desc" );
        location.setAddress1( "address1" );
        location.setId( 3 );
        Period period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
        SimpleDateFormat dateformat = new SimpleDateFormat( "yyyy-MM-dd" );
        String start = "2016-06-28";
        String end = "2016-06-30";
        Date startDate = null;
        Date endDate = null;

        startDate = dateformat.parse( start );
        endDate = dateformat.parse( end );

        period.setStartDate( startDate );
        period.setEndDate( endDate );
        for ( ReportDefinition rd : rds )
        {
            DataValueSet dvs = dhis2ReportingService.evaluateReportDefinition( rd, period, location, false );
            List<DataValue> datavalues = dvs.getDataValues();
            for ( DataValue dv : datavalues )
            {
                assertEquals( "0", dv.getValue() );
            }

        }

        executeDataSet( INITIAL_OBS_XML );

        for ( ReportDefinition rd : rds )
        {
            DataValueSet dvs = dhis2ReportingService.evaluateReportDefinition( rd, period, location, false );
            List<DataValue> datavalues = dvs.getDataValues();
            for ( DataValue dv : datavalues )
            {
                assertEquals( "3", dv.getValue() );
            }

        }

    }

    @Test
    public void shouldGenerateReportingReportDefinition()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "reportDefinition.xml" );
        dhis2ReportingService.unMarshallandSaveReportTemplates( resource.getInputStream() );
        ReportTemplates rt = dhis2ReportingService.getReportTemplates();
        List<ReportDefinition> lt = rt.getReportDefinitions();
        for ( ReportDefinition l : lt )
        {
            l.setReportingReportId( "rt123" );
        }
        dhis2ReportingService.saveReportTemplates( rt );
        String timeperiod = "2016-06-28";
        Location location = new Location();
        location.setName( "County General" );
        location.setDescription( "desc" );
        location.setAddress1( "address1" );
        location.setId( 123 );
        Period period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
        SimpleDateFormat dateformat = new SimpleDateFormat( "yyyy-MM-dd" );
        String start = "2016-06-28";
        String end = "2016-06-30";
        Date startDate = null;
        Date endDate = null;

        startDate = dateformat.parse( start );
        endDate = dateformat.parse( end );

        Map<String, Mapped<? extends DataSetDefinition>> mp = new HashMap<String, Mapped<? extends DataSetDefinition>>();
        SimplePatientDataSetDefinition maleView = new SimplePatientDataSetDefinition();
        maleView.addPatientProperty( "patientId" );
        mp.put( "1", new Mapped<DataSetDefinition>( maleView, null ) );
        SimplePatientDataSetDefinition femaleView = new SimplePatientDataSetDefinition();
        femaleView.addPatientProperty( "patientId" );
        mp.put( "2", new Mapped<DataSetDefinition>( femaleView, null ) );

        period.setStartDate( startDate );
        period.setEndDate( endDate );
        for ( ReportDefinition rd : lt )
        {

            ReportDefinitionService service = Context.getService( ReportDefinitionService.class );
            PeriodIndicatorReportDefinition report = new PeriodIndicatorReportDefinition();
            report.setName( rd.getName() );
            report.setId( rd.getId() );
            report.setUuid( rd.getReportingReportId() );
            report.setDataSetDefinitions( mp );
            org.openmrs.module.reporting.report.definition.ReportDefinition savedReportDefinition = service
                .saveDefinition( report );

            DataValueSet dvs = dhis2ReportingService.generateReportingReportDefinition( rd, period, location );
            assertEquals( dvs.getDataSet(), "MNCH" );
            for ( DataValue dv : dvs.getDataValues() )
            {
                assertEquals( dv.getCategoryOptionCombo(), "YtbnZipIBx3" );
                assertEquals( dv.getDataElement(), "ANC4" );
                assertEquals( dv.getValue(), "2" );

            }
        }
    }

    @Test
    public void shouldunMarshallandSaveReportTemplates()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "reportDefinition.xml" );
        dhis2ReportingService.unMarshallandSaveReportTemplates( resource.getInputStream() );

        ReportTemplates rt = dhis2ReportingService.getReportTemplates();
        List<DataElement> de = rt.getDataElements();
        assertEquals( "ANC1", de.get( 0 ).getCode() );
        assertEquals( "ANC1", de.get( 0 ).getName() );
        assertEquals( "nvVDDkfbbhf", de.get( 0 ).getUid() );

        List<Disaggregation> diss = rt.getDisaggregations();
        assertEquals( "HgRodT2oZlq", diss.get( 1 ).getCode() );
        assertEquals( "(Male)", diss.get( 1 ).getName() );
        assertEquals( "HgRodT2oZlq", diss.get( 1 ).getUid() );

        List<ReportDefinition> rd = rt.getReportDefinitions();
        assertEquals( "MNCH", rd.get( 0 ).getCode() );
        assertEquals( "Maternal and Child Health", rd.get( 0 ).getName() );
        assertEquals( "Monthly", rd.get( 0 ).getPeriodType() );
        assertEquals( "sI82CctvS1A", rd.get( 0 ).getUid() );
    }

    @Test
    public void shouldunMarshallandSaveAdxReportTemplates()
        throws Exception
    {

        ReportTemplates rt = dhis2ReportingService.getReportTemplates();
        final int countDataElements = rt.getDataElements().size();
        final int countDisaggregations = rt.getDisaggregations().size();
        final List<ReportDefinition> initialReportDefs = rt.getReportDefinitions();
        ClassPathResource resource = new ClassPathResource( "adxReportDefinition.xml" );
        dhis2ReportingService.unMarshallAdxAndSaveReportTemplates( resource.getInputStream() );

        rt = dhis2ReportingService.getReportTemplates();
        ReportDefinition reportDef = rt.getReportDefinitions().get( 0 );
        assertEquals( "DSD_ATB_005", reportDef.getCode() );
        assertEquals( "{name {ATB - 005 ART SUMMARY}}", reportDef.getName() );
        assertEquals( countDataElements + 3, rt.getDataElements().size() );
        assertEquals( countDisaggregations + 12, rt.getDisaggregations().size() );
        List<ReportDefinition> newReportDefs = rt.getReportDefinitions();
        assertEquals( initialReportDefs.size() + 1, newReportDefs.size() );
        Collection<ReportDefinition> createdReports = CollectionUtils.subtract( newReportDefs, initialReportDefs );
        assertEquals( 1, createdReports.size() );
        ReportDefinition createdReport = createdReports.iterator().next();
        assertEquals( 14, createdReport.getDataValueTemplates().size() );

    }

    @Test
    public void shouldsaveReportTemplates()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "reportDefinition.xml" );

        dhis2ReportingService.unMarshallandSaveReportTemplates( resource.getInputStream() );
        ReportTemplates rt = dhis2ReportingService.getReportTemplates();
        List<ReportDefinition> lt = rt.getReportDefinitions();
        for ( ReportDefinition l : lt )
        {
            l.setReportingReportId( "rt123" );
        }
        dhis2ReportingService.saveReportTemplates( rt );

        ReportTemplates repTem = dhis2ReportingService.getReportTemplates();

        List<DataElement> de = repTem.getDataElements();
        assertEquals( "ANC1", de.get( 0 ).getCode() );
        assertEquals( "ANC1", de.get( 0 ).getName() );
        assertEquals( "nvVDDkfbbhf", de.get( 0 ).getUid() );

        List<Disaggregation> diss = repTem.getDisaggregations();
        assertEquals( "HgRodT2oZlq", diss.get( 1 ).getCode() );

        assertEquals( "(Male)", diss.get( 1 ).getName() );
        assertEquals( "HgRodT2oZlq", diss.get( 1 ).getUid() );

        List<ReportDefinition> rd = repTem.getReportDefinitions();
        assertEquals( "MNCH", rd.get( 0 ).getCode() );
        assertEquals( "Maternal and Child Health", rd.get( 0 ).getName() );
        assertEquals( "Monthly", rd.get( 0 ).getPeriodType() );
        assertEquals( "sI82CctvS1A", rd.get( 0 ).getUid() );

    }

    @Test
    public void shouldgetLocationByOrgUnitCode()
        throws Exception
    {
        Location loc = new Location();
        loc.setName( "testing" );
        loc.setDescription( "desc" );
        loc.setAddress1( "123" );
        loc.setAddress1( "456" );
        loc.setCityVillage( "city" );
        loc.setStateProvince( "state" );
        loc.setCountry( "country" );
        loc.setPostalCode( "post" );
        loc.setLatitude( "lat" );
        loc.setLongitude( "lon" );

        LocationAttributeType attributetype = new LocationAttributeType();
        attributetype.setName( "CODE" );
        attributetype.setDescription( "Corresponding Value of ORG UNITS for DHIS" );
        attributetype.setMinOccurs( 0 );
        attributetype.setMaxOccurs( 1 );
        attributetype.setDatatypeClassname( "org.openmrs.customdatatype.datatype.FreeTextDatatype" );
        Context.getLocationService().saveLocationAttributeType( attributetype );

        LocationAttribute locationAttribute = new LocationAttribute();
        locationAttribute.setAttributeType( attributetype );
        locationAttribute.setValue( "dhis2OrgUnitCode" );
        loc.setAttribute( locationAttribute );
        Context.getLocationService().saveLocation( loc );

        Location l = dhis2ReportingService.getLocationByOrgUnitCode( "dhis2OrgUnitCode" );
        assertEquals( loc, l );
        assertTrue( loc.equals( l ) );
        assertEquals( "testing", l.getName() );
        assertEquals( "country", l.getCountry() );
    }

}
