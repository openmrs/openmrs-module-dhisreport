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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.Dhis2Server;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    Integer reportDefinition_id )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinition", service.getReportDefinition( reportDefinition_id ) );
        model.addAttribute( "locations", Context.getLocationService().getAllLocations() );

        Dhis2Server server = service.getDhis2Server();

        if ( (server != null) & (server.isConfigured()) )
        {
            model.addAttribute( "dhis2Server", server );
        }
    }

    @RequestMapping( value = "/module/dhisreport/executeReport", method = RequestMethod.POST )
    public void executeReport( ModelMap model, @RequestParam( value = "reportDefinition_id", required = true )
    Integer reportDefinition_id, @RequestParam( value = "location", required = true )
    String OU_Code, @RequestParam( value = "resultDestination", required = true )
    String destination, @RequestParam( value = "date", required = true )
    String dateStr )
        throws ParseException, DHIS2ReportingException
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        MonthlyPeriod period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( dateStr ) );

        // Get Location by OrgUnit Code
        Location location = service.getLocationByOU_Code( OU_Code );

        DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinition( reportDefinition_id ),
            period, location );
        // Set OrgUnit code into DataValueSet
        dvs.setOrgUnit( OU_Code );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "dataValueSet", dvs );

        if ( destination.equals( "post" ) )
        {
            ImportSummary importSummary = Context.getService( DHIS2ReportingService.class ).postDataValueSet( dvs );
            model.addAttribute( "importSummary", importSummary );
        }
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
}
