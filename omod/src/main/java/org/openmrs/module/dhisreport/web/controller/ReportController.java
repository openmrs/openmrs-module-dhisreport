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
package org.openmrs.module.dhisreport.web.controller;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.AggregatedResultSet;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.adx.DataValueType;
import org.openmrs.module.dhisreport.api.adx.GroupType;
import org.openmrs.module.dhisreport.api.adx2.AdxConstants;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.dxf2.DataValue;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummary;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.module.dhisreport.api.utils.WeeklyPeriod;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.Report;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.RenderingMode;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.module.reporting.web.renderers.DefaultWebRenderer;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * The main controller.
 */
@Controller
public class ReportController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping( value = "/module/dhisreport/manage", method = RequestMethod.GET )
    public void manage( ModelMap model )
    {
        model.addAttribute( "user", Context.getAuthenticatedUser() );
    }

    @RequestMapping( value = "/module/dhisreport/listDhis2Reports", method = RequestMethod.GET )
    public void listReports( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinitions", service.getAllReportDefinitions() );
    }

    @RequestMapping( value = "/module/dhisreport/setupReport", method = RequestMethod.GET )
    public void setupReport( ModelMap model, @RequestParam( value = "reportDefinition_id", required = false )
    Integer reportDefinition_id, HttpSession session )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        String errormsg = (String) session.getAttribute( "errorMessage" );
        session.removeAttribute( "errorMessage" );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinition", service.getReportDefinition( reportDefinition_id ) );
        model.addAttribute( "locations", Context.getLocationService().getAllLocations() );
        model.addAttribute( "errorMessage", errormsg );

        String dhisurl = Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2URL" );
        String dhisusername = Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2UserName" );
        String dhispassword = Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2Password" );

        HttpDhis2Server server = service.getDhis2Server();

        URL url = null;
        try
        {
            url = new URL( dhisurl );
        }
        catch ( MalformedURLException e )
        {
            e.printStackTrace();
        }

        server.setUrl( url );
        server.setUsername( dhisusername );
        server.setPassword( dhispassword );

        if ( (server != null) & (server.isConfigured()) )
        {
            model.addAttribute( "dhis2Server", server );
        }
        ReportDefinition rd = service.getReportDefinition( reportDefinition_id );
        boolean trigger = false;
        for ( DataValueTemplate dvt : rd.getDataValueTemplates() )
        {
            if ( (dvt.getQuery() == null || dvt.getQuery().equals( "" ) || dvt.getQuery().equals( " " ))
                && dvt.getMappeddefinitionlabel() == null )
            {
                trigger = true;
            }
        }
        if ( trigger )
        {
            String msg = "Report contains unmapped definitions";
            model.addAttribute( "nullPointer", msg );
        }
    }

    @RequestMapping( value = "/module/dhisreport/executeReport", method = RequestMethod.POST )
    public String executeReport( ModelMap model, @RequestParam( value = "reportDefinition_id", required = true )
    Integer reportDefinition_id, @RequestParam( value = "location", required = false )
    String OU_Code, @RequestParam( value = "resultDestination", required = true )
    String destination, @RequestParam( value = "date", required = true )
    String dateStr, @RequestParam( value = "frequency", required = true )
    String freq, @RequestParam( value = "prior", required = true )
    String prior, WebRequest webRequest, HttpServletRequest request )
        throws Exception
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        Period period = null;
        // System.out.println( "freeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + freq
        // );
        // System.out.println( "dasdasssssssssssssssssssss" + dateStr );

        if ( freq.equalsIgnoreCase( "monthly" ) )
        {
            if ( dateStr.length() > 7 )
                dateStr = replacedateStrMonth( dateStr );
            dateStr = dateStr.concat( "-01" );
            try
            {
                // System.out.println( "helloooooooooo1=====" + dateStr );
                period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( dateStr ) );
                // System.out.println( "helloooooooooo2=====" + period );
            }
            catch ( ParseException pex )
            {
                log.error( "Cannot convert passed string to date... Please check dateFormat", pex );
                webRequest.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                    "Date Parsing Error" ), WebRequest.SCOPE_SESSION );
                return null;
            }
        }
        if ( freq.equalsIgnoreCase( "weekly" ) )
        {
            try
            {
                String finalweek = "";
                String[] modify_week = dateStr.split( "W" );
                Integer weekvalue = Integer.parseInt( dateStr.substring( dateStr.indexOf( 'W' ) + 1 ) ) + 1;
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
                webRequest.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                    "Date Parsing Error" ), WebRequest.SCOPE_SESSION );
                return null;
            }
        }
        if ( freq.equalsIgnoreCase( "daily" ) )
        {

            webRequest.setAttribute( WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage(
                "dhisreport.dateFormatError" ), WebRequest.SCOPE_SESSION );
            return null;

        }

        // Get Location by OrgUnit Code
        // Location location = service.getLocationByOU_Code( OU_Code );
        // System.out.println( "helloooooooooo3=====" + period );
        List<DataValueSet> dvsList = new ArrayList<DataValueSet>();
        List<Location> locationList = new ArrayList<Location>();
        List<Location> locationListFinal = new ArrayList<Location>();
        // locationList.add( location );
        // locationList.add( service.getLocationByOU_Code( "Gahombo" ) );
        locationList.addAll( Context.getLocationService().getAllLocations() );

        // remove locations without Organization Unit codes
        for ( Location l : locationList )
        {
            for ( LocationAttribute la : l.getActiveAttributes() )
            {
                if ( la.getAttributeType().getName().equals( "CODE" ) )
                {
                    // System.out.println( "Name-----" + la.getAttributeType().getName() +
                    // "Value---" + la.getValue() );
                    if ( !la.getValue().toString().isEmpty() && la.getValue().toString() != null )
                    {
                        locationListFinal.add( l );
                        break;
                    }

                }

            }
        }

        Map<String, Map> desetList = new HashMap<String, Map>();
        List<AggregatedResultSet> aggregatedList = new ArrayList<AggregatedResultSet>();
        if ( locationListFinal.isEmpty() && !locationList.isEmpty() )
        {
            log.error( "Location attribute CODE not set" );
            request.getSession().setAttribute( "errorMessage",
                "Please set location attribute CODE to generate results." );
            String referer = webRequest.getHeader( "Referer" );
            return "redirect:" + referer;
        }

        boolean priority = false;
        if ( prior.equals( "report" ) )
        {
            priority = true;
        }

        for ( Location l : locationListFinal )
        {
            AggregatedResultSet agrs = new AggregatedResultSet();
            DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinition( reportDefinition_id ),
                period, l, priority );
            for ( LocationAttribute la : l.getActiveAttributes() )
            {
                if ( la.getAttributeType().getName().equals( "CODE" ) )
                    dvs.setOrgUnit( la.getValue().toString() );
            }
            // Set OrgUnit code into DataValueSet

            List<DataValue> datavalue = dvs.getDataValues();
            Set<DataElement> dataElements = new HashSet<DataElement>();
            for ( DataValue dv : datavalue )
            {

                DataElement detrmp = service.getDataElementByCode( dv.getDataElement() );
                // System.out.println( detrmp.getName() + detrmp.getCode() );
                dataElements.add( detrmp );
            }
            agrs.setDataValueSet( dvs );
            agrs.setDataElements( dataElements );
            AdxType adxType = getAdxType( dvs, dateStr );
            JAXBContext jxb = JAXBContext.newInstance( AdxType.class );
            Marshaller m = jxb.createMarshaller();
            StringWriter writer = new StringWriter();
            m.marshal( adxType, writer );
            if ( destination.equals( "post" ) )
            {
                ImportSummary importSummary = Context.getService( DHIS2ReportingService.class ).postAdxReport( adxType );
                agrs.setImportSummary( importSummary );
            }
            aggregatedList.add( agrs );
        }

        org.openmrs.module.reporting.report.definition.ReportDefinition rrd = Context.getService(
            ReportDefinitionService.class ).getDefinitionByUuid(
            service.getReportDefinition( reportDefinition_id ).getReportingReportId() );

        Map<String, Object> param = new HashMap<String, Object>();
        param.put( "startDate", period.getStartDate() );
        param.put( "endDate", period.getEndDate() );
        if ( rrd != null )
        {
            ReportRequest rq = new ReportRequest();
            rq.setReportDefinition( new Mapped<org.openmrs.module.reporting.report.definition.ReportDefinition>( rrd,
                param ) );
            rq.setRenderingMode( new RenderingMode( new DefaultWebRenderer(), "Web", null, 100 ) );
            Report report = Context.getService( ReportService.class ).runReport( rq );
            model.addAttribute( "resultUuid", rq.getUuid() );
        }
        else
        {
            model.addAttribute( "resultUuid", null );
        }
        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "aggregatedList", aggregatedList );
        return null;
    }

    // @RequestMapping(value = "/module/dhisreport/executeReport", method =
    // RequestMethod.POST)
    // public void saveReport( ModelMap model,
    // @RequestParam(value = "reportDefinition_id", required = true) Integer
    // reportDefinition_id,
    // @RequestParam(value = "location", required = true) Integer location_id,
    // @RequestParam(value = "resultDestination", required = true) String
    // destination,
    // @RequestParam(value = "date", required = true) String dateStr,
    // HttpServletResponse response )
    // throws ParseException, IOException, JAXBException,
    // DHIS2ReportingException
    // {
    // DHIS2ReportingService service = Context.getService(
    // DHIS2ReportingService.class );
    //
    // MonthlyPeriod period = new MonthlyPeriod( new SimpleDateFormat(
    // "yyyy-MM-dd" ).parse( dateStr ) );
    // Location location = Context.getLocationService().getLocation( location_id
    // );
    //
    // DataValueSet dvs = service.evaluateReportDefinition(
    // service.getReportDefinition( reportDefinition_id ), period, location );
    //
    // response.setContentType( "application/xml" );
    // response.setCharacterEncoding( "UTF-8" );
    // response.addHeader( "Content-Disposition",
    // "attachment; filename=report.xml" );
    //
    // dvs.marshall( response.getOutputStream());
    // }

    @RequestMapping( value = "/module/dhisreport/syncReports", method = RequestMethod.GET )
    public void syncReports( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinitions", service.getAllReportDefinitions() );
    }

    private String replacedateStrMonth( String dateStr )
    {

        String str = "";
        // System.out.println( dateStr.substring( 5, 8 ) );

        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Jan" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Jan", "01" );
            // System.out.println( "converting date" + str );
        }
        else if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Feb" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Feb", "02" );
            // System.out.println( "converting date" + str );
        }
        else if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Mar" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Mar", "03" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Apr" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Apr", "04" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "May" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "May", "05" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Jun" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Jun", "06" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Jul" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Jul", "07" );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Aug" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Aug", "08" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Sep" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Sep", "09" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Oct" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Oct", "10" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Nov" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Nov", "11" );
            // System.out.println( "converting date" + str );
        }
        if ( dateStr.substring( 5, 8 ).equalsIgnoreCase( "Dec" ) )
        {
            // System.out.println( "converting date" );
            str = dateStr.replaceFirst( "Dec", "12" );
            // System.out.println( "converting date" + str );
        }

        return str;
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
            String[] aggs = StringUtils.split( dv.getCategoryOptionCombo(), AdxConstants.DISSAGGREGATION_SEPARATOR );
            for ( String agg : aggs )
            {
                String[] nameAndValue = StringUtils.split( agg, AdxConstants.DISSAGGREGATION_OPTION_SEPARATOR );
                dvtype.getOtherAttributes().put( new QName( nameAndValue[0] ), nameAndValue[1] );
            }
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
