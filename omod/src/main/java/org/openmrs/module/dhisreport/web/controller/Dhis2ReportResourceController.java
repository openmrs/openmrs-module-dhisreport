package org.openmrs.module.dhisreport.web.controller;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.Dxf2Exception;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.adx.DataValueType;
import org.openmrs.module.dhisreport.api.adx.GroupType;
import org.openmrs.module.dhisreport.api.dxf2.*;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.module.dhisreport.api.utils.WeeklyPeriod;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class Dhis2ReportResourceController
{
    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping( value = "/dhisreport/runReport/{reportId}/{location}/{timeperiod}", method = RequestMethod.GET )
    @ResponseBody
    public Object runReport( @PathVariable( "reportId" )
    String reportId, @PathVariable( "location" )
    String location, @PathVariable( "timeperiod" )
    String timeperiod, HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {
        log.info( "User Authorizing the request" + Context.getAuthenticatedUser() );
        log.info( "Request Parameters - ReportID" + reportId );
        log.info( "Request Parameters - LocationID" + location );
        log.info( "Request Parameters - Timeperiod" + timeperiod );
        if ( !Context.hasPrivilege( "View Dhisreport" ) )
        {
            return "User does not have the privilige to view the report";
        }
        //response.setContentType( "text/xml" );
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        Period period = null;
        log.debug( "Initial Date Sent by the user: " + timeperiod );

        if ( timeperiod.length() != 6
            || (Integer.parseInt( timeperiod.substring( 4, 6 ) ) > 12 || Integer
                .parseInt( timeperiod.substring( 4, 6 ) ) < 1)
            || (Integer.parseInt( timeperiod.substring( 0, 4 ) ) > 9999 || Integer.parseInt( timeperiod
                .substring( 0, 4 ) ) < 0000) )
            return "Error: Date Format not supported. The only date format supported for this webservice is YYYYMM.";

        if ( !timeperiod.contains( "W" ) )
        {

            //if ( timeperiod.length() > 7 )
            //timeperiod = replacedateStrMonth( timeperiod );
            timeperiod = timeperiod.substring( 0, 4 ) + "-" + timeperiod.substring( 4, timeperiod.length() );
            ;
            timeperiod = timeperiod.concat( "-01" );
            try
            {
                log.debug( "date before conversion to period" + timeperiod );
                period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
                log.debug( "Date after conversion to period" + period );
            }
            catch ( ParseException pex )
            {
                log.error( "Cannot convert passed string to date... Please check dateFormat", pex );
                //request.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                // "Date Parsing Error" ), WebRequest.SCOPE_SESSION );
                return "Date Format Error";
            }
        }
        if ( timeperiod.contains( "W" ) )
        {
            try
            {
                String finalweek = "";
                String[] modify_week = timeperiod.split( "W" );
                Integer weekvalue = Integer.parseInt( timeperiod.substring( timeperiod.indexOf( 'W' ) + 1 ) ) + 1;
                if ( weekvalue > 9 )
                {
                    weekvalue = weekvalue == 54 ? 53 : weekvalue;
                    finalweek = modify_week[0].concat( "W" + weekvalue.toString() );
                }
                else
                {
                    finalweek = modify_week[0].concat( "W0" + weekvalue.toString() );
                }

                period = new WeeklyPeriod( new SimpleDateFormat( "yyyy-'W'ww" ).parse( finalweek ) );
            }
            catch ( ParseException ex )
            {
                log.error( "Cannot convert passed string to date... Please check dateFormat", ex );
                //webRequest.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                //   "Date Parsing Error" ), WebRequest.SCOPE_SESSION );
                return "Date Format Error";
            }
        }

        // Get Location by OrgUnit Code
        //Location location = service.getLocationByOU_Code( OU_Code );
        log.debug( "Period =" + period );

        Location l = service.getLocationByOrgUnitCode( location );

        if ( l == null )
        {
            return "No Location with the Specified Org Unit";
        }

        period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
        DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinitionByCode( reportId ), period, l );

        for ( LocationAttribute la : l.getActiveAttributes() )
        {
            if ( la.getAttributeType().getName().equals( "CODE" ) )
                dvs.setOrgUnit( la.getValue().toString() );
        }

        StringWriter xmlReport = new StringWriter();
        try
        {
            JAXBContext jaxbDataValueSetContext = JAXBContext.newInstance( DataValueSet.class );

            Marshaller dataValueSetMarshaller = jaxbDataValueSetContext.createMarshaller();
            // output pretty printed
            dataValueSetMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            dataValueSetMarshaller.marshal( dvs, xmlReport );
        }
        catch ( JAXBException ex )
        {
            throw new Dxf2Exception( "Problem marshalling dataValueSet", ex );
        }
        //System.out.println( xmlReport.toString() );
        log.info( "Result" + xmlReport.toString() );
        String result = xmlReport.toString();
        //  System.out.println( result );
        return result;
    }

    @RequestMapping( value = "/dhisreport/getReports", method = RequestMethod.GET )
    @ResponseBody
    public Object getReports( HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        Collection<ReportDefinition> rdlist = service.getAllReportDefinitions();
        DataSets ds = new DataSets();
        List<DataSet> datasets = new ArrayList<DataSet>();
        for ( ReportDefinition rd : rdlist )
        {
            DataSet data = new DataSet();
            data.setCode( rd.getCode() );
            data.setName( rd.getName() );
            datasets.add( data );
        }
        ds.setDataSets( datasets );
        StringWriter xmlReport = new StringWriter();
        try
        {
            JAXBContext jaxbDataSetsContext = JAXBContext.newInstance( DataSets.class );

            Marshaller dataSetsMarshaller = jaxbDataSetsContext.createMarshaller();
            // output pretty printed
            dataSetsMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            dataSetsMarshaller.marshal( ds, xmlReport );
        }
        catch ( JAXBException ex )
        {
            throw new Dxf2Exception( "Problem marshalling Datasets", ex );
        }
        //System.out.println( xmlReport.toString() );
        log.info( "Result" + xmlReport.toString() );
        String result = xmlReport.toString();
        //  System.out.println( result );
        return result;
    }

    @RequestMapping( value = "/dhisreport/getLocations", method = RequestMethod.GET )
    @ResponseBody
    public Object getLocations( HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {
        List<Location> locationList = new ArrayList<Location>();
        List<Location> locationListFinal = new ArrayList<Location>();
        //locationList.add( location );
        //locationList.add( service.getLocationByOU_Code( "Gahombo" ) );
        locationList.addAll( Context.getLocationService().getAllLocations() );

        //remove locations without Organization Unit codes
        for ( Location l : locationList )
        {
            for ( LocationAttribute la : l.getActiveAttributes() )
            {
                if ( la.getAttributeType().getName().equals( "CODE" ) )
                {
                    //System.out.println( "Name-----" + la.getAttributeType().getName() + "Value---" + la.getValue() );
                    if ( !la.getValue().toString().isEmpty() && la.getValue().toString() != null )
                    {
                        locationListFinal.add( l );
                        break;
                    }

                }

            }
        }
        OrganizationUnits ous = new OrganizationUnits();
        List<OrganizationUnit> organizationUnits = new ArrayList<OrganizationUnit>();
        for ( Location l : locationListFinal )
        {
            OrganizationUnit ou = new OrganizationUnit();
            for ( LocationAttribute la : l.getActiveAttributes() )
            {
                if ( la.getAttributeType().getName().equals( "CODE" ) )
                {

                    ou.setCode( la.getValue().toString() );
                }

            }
            ou.setName( l.getName() );
            organizationUnits.add( ou );
        }
        ous.setOrganizationUnits( organizationUnits );
        StringWriter xmlReport = new StringWriter();
        try
        {
            JAXBContext jaxbOrganizationUnitsContext = JAXBContext.newInstance( OrganizationUnits.class );

            Marshaller organizationUnitsMarshaller = jaxbOrganizationUnitsContext.createMarshaller();
            // output pretty printed
            organizationUnitsMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            organizationUnitsMarshaller.marshal( ous, xmlReport );
        }
        catch ( JAXBException ex )
        {
            throw new Dxf2Exception( "Problem marshalling Organization Units", ex );
        }
        //System.out.println( xmlReport.toString() );
        log.info( "Result" + xmlReport.toString() );
        String result = xmlReport.toString();
        //  System.out.println( result );
        return result;
    }

    @RequestMapping( value = "/dhisreport/getAdxReport/{reportId}/{location}/{timeperiod}", method = RequestMethod.GET )
    @ResponseBody
    public Object getAdxReports( @PathVariable( "reportId" )
    String reportId, @PathVariable( "location" )
    String location, @PathVariable( "timeperiod" )
    String timeperiod, HttpServletRequest request, HttpServletResponse response )
        throws Exception
    {
        log.info( "User Authorizing the request" + Context.getAuthenticatedUser() );
        log.info( "Request Parameters - ReportID" + reportId );
        log.info( "Request Parameters - LocationID" + location );
        log.info( "Request Parameters - Timeperiod" + timeperiod );
        if ( !Context.hasPrivilege( "View Dhisreport" ) )
        {
            return "User does not have the privilige to view the report";
        }
        //response.setContentType( "text/xml" );
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        Period period = null;
        log.debug( "Initial Date Sent by the user: " + timeperiod );

        if ( timeperiod.length() != 6
            || (Integer.parseInt( timeperiod.substring( 4, 6 ) ) > 12 || Integer
                .parseInt( timeperiod.substring( 4, 6 ) ) < 1)
            || (Integer.parseInt( timeperiod.substring( 0, 4 ) ) > 9999 || Integer.parseInt( timeperiod
                .substring( 0, 4 ) ) < 0000) )
            return "Error: Date Format not supported. The only date format supported for this webservice is YYYYMM.";

        if ( !timeperiod.contains( "W" ) )
        {

            //if ( timeperiod.length() > 7 )
            //timeperiod = replacedateStrMonth( timeperiod );
            timeperiod = timeperiod.substring( 0, 4 ) + "-" + timeperiod.substring( 4, timeperiod.length() );
            ;
            timeperiod = timeperiod.concat( "-01" );
            try
            {
                log.debug( "date before conversion to period" + timeperiod );
                period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
                log.debug( "Date after conversion to period" + period );
            }
            catch ( ParseException pex )
            {
                log.error( "Cannot convert passed string to date... Please check dateFormat", pex );
                //request.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                // "Date Parsing Error" ), WebRequest.SCOPE_SESSION );
                return "Date Format Error";
            }
        }
        if ( timeperiod.contains( "W" ) )
        {
            try
            {
                String finalweek = "";
                String[] modify_week = timeperiod.split( "W" );
                Integer weekvalue = Integer.parseInt( timeperiod.substring( timeperiod.indexOf( 'W' ) + 1 ) ) + 1;
                if ( weekvalue > 9 )
                {
                    weekvalue = weekvalue == 54 ? 53 : weekvalue;
                    finalweek = modify_week[0].concat( "W" + weekvalue.toString() );
                }
                else
                {
                    finalweek = modify_week[0].concat( "W0" + weekvalue.toString() );
                }

                period = new WeeklyPeriod( new SimpleDateFormat( "yyyy-'W'ww" ).parse( finalweek ) );
            }
            catch ( ParseException ex )
            {
                log.error( "Cannot convert passed string to date... Please check dateFormat", ex );
                //webRequest.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                //   "Date Parsing Error" ), WebRequest.SCOPE_SESSION );
                return "Date Format Error";
            }
        }

        // Get Location by OrgUnit Code
        //Location location = service.getLocationByOU_Code( OU_Code );
        log.debug( "Period =" + period );

        Location l = service.getLocationByOrgUnitCode( location );

        if ( l == null )
        {
            return "No Location with the Specified Org Unit";
        }

        period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
        DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinitionByCode( reportId ), period, l );

        for ( LocationAttribute la : l.getActiveAttributes() )
        {
            if ( la.getAttributeType().getName().equals( "CODE" ) )
                dvs.setOrgUnit( la.getValue().toString() );
        }

        AdxType adxType = getAdxType( dvs, timeperiod );

        StringWriter xmlReport = new StringWriter();
        try
        {
            JAXBContext jaxbDataValueSetContext = JAXBContext.newInstance( AdxType.class );

            Marshaller adxTypeMarshaller = jaxbDataValueSetContext.createMarshaller();
            // output pretty printed
            adxTypeMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            adxTypeMarshaller.marshal( adxType, xmlReport );
        }
        catch ( JAXBException ex )
        {
            throw new Dxf2Exception( "Problem marshalling dataValueSet", ex );
        }
        //System.out.println( xmlReport.toString() );
        log.info( "Result" + xmlReport.toString() );
        String result = xmlReport.toString();
        //  System.out.println( result );
        return result;
    }

    AdxType getAdxType( DataValueSet dvs, String timeperiod )
    {
        AdxType adxType = new AdxType();
        adxType.setExported( dvs.getCompleteDate() );
        GroupType gt = new GroupType();
        List<DataValueType> dvTypeList = new ArrayList<DataValueType>();
        for ( DataValue dv : dvs.getDataValues() )
        {
            DataValueType dvtype = new DataValueType();
            dvtype.setDataElement( dv.getDataElement() );
            dvtype.setValue( new BigDecimal( dv.getValue() ) );
            dvTypeList.add( dvtype );
        }
        gt.getDataValue().addAll( dvTypeList );
        gt.setOrgUnit( dvs.getOrgUnit() );
        gt.setDataSet( dvs.getDataSet() );
        gt.setPeriod( timeperiod + "/P1M" );
        adxType.getGroup().add( gt );
        return adxType;
    }
}
