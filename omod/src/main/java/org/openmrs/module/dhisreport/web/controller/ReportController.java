/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.dhisreport.web.controller;


import java.text.ParseException;
import java.io.IOException;
import javax.xml.bind.JAXBException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.Dhis2Server;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
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

    @RequestMapping(value = "/module/dhisreport/manage", method = RequestMethod.GET)
    public void manage( ModelMap model )
    {
        model.addAttribute( "user", Context.getAuthenticatedUser() );
    }

    @RequestMapping(value = "/module/dhisreport/listDhis2Reports", method = RequestMethod.GET)
    public void listReports( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinitions", service.getAllReportDefinitions() );
    }

    @RequestMapping(value = "/module/dhisreport/setupReport", method = RequestMethod.GET)
    public void setupReport( ModelMap model, @RequestParam(value = "reportDefinition_id", required = false) Integer reportDefinition_id )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinition", service.getReportDefinition( reportDefinition_id ) );
        model.addAttribute( "locations", Context.getLocationService().getAllLocations() );
        Dhis2Server server = service.getDhis2Server();

        if ( ( server != null ) & ( server.isConfigured() ) )
        {
            model.addAttribute( "dhis2Server", server );
        }
    }

    @RequestMapping(value = "/module/dhisreport/executeReport", method = RequestMethod.POST)
    public void executeReport( ModelMap model,
        @RequestParam(value = "reportDefinition_id", required = true) Integer reportDefinition_id,
        @RequestParam(value = "location", required = true) Integer location_id,
        @RequestParam(value = "resultDestination", required = true) String destination,
        @RequestParam(value = "date", required = true) String dateStr,
        HttpServletResponse response )
        throws ParseException, IOException, JAXBException, DHIS2ReportingException
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        MonthlyPeriod period = new MonthlyPeriod( new SimpleDateFormat( "yyyy-MM-dd" ).parse( dateStr ) );
        Location location = Context.getLocationService().getLocation( location_id );

        DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinition( reportDefinition_id ), period, location );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "dataValueSet", dvs );

        /*if ( destination.equals( "post" ) )
        {
            ImportSummary importSummary = Context.getService( DHIS2ReportingService.class ).postDataValueSet( dvs );
            model.addAttribute( "importSummary", importSummary );
        } else
        {
            if ( destination.equals( "save" ) )
            {
                response.setContentType( "application/xml" );
                response.setCharacterEncoding( "UTF-8" );
                response.addHeader( "Content-Disposition", "attachment; filename=report.xml" );
                dvs.marshall( response.getOutputStream() );
            } else
            {
                model.addAttribute( "user", Context.getAuthenticatedUser() );
                model.addAttribute( "dataValueSet", dvs );
            }
        }*/
    }
}
