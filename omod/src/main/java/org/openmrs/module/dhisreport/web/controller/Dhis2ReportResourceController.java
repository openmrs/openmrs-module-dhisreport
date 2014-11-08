package org.openmrs.module.dhisreport.web.controller;

import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.Dxf2Exception;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
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
        //response.setContentType( "text/xml" );
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        Period period = null;
        System.out.println( "dasdasssssssssssssssssssss" + timeperiod );

        if ( !timeperiod.contains( "W" ) )
        {
            //if ( timeperiod.length() > 7 )
            //timeperiod = replacedateStrMonth( timeperiod );
            timeperiod = timeperiod.substring( 0, 4 ) + "-" + timeperiod.substring( 4, timeperiod.length() );
            ;
            timeperiod = timeperiod.concat( "-01" );
            try
            {
                System.out.println( "helloooooooooo1=====" + timeperiod );
                period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
                System.out.println( "helloooooooooo2=====" + period );
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
        System.out.println( "helloooooooooo3=====" + period );

        Location l = service.getLocationByOrgUnitCode( location );

        if ( l == null )
        {
            return "No Location with the Specified Org Unit";
        }

        period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( timeperiod ) );
        DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinitionByCode( reportId ), period, l );

        for ( LocationAttribute la : l.getActiveAttributes() )
        {
            if ( la.getAttributeType().getName().equals( "FOSAID" ) )
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
        String result = xmlReport.toString();
        System.out.println( result );
        return result;
    }
}
