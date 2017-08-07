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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.syncmodel.ReportSynchronizer;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */

@Controller
public class ReportSyncController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping( value = "/module/dhisreport/fullReportSync", method = RequestMethod.GET )
    public String fullReportSync( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        // System.out.println( "Entered Sync Controller" );

        ReportSynchronizer rs = new ReportSynchronizer();

        try
        {

            rs.fullSync();

            DateFormat df = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss" );

            // Get the date today using Calendar object.
            Date today = Calendar.getInstance().getTime();
            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            String reportDate = df.format( today );

            List<GlobalProperty> gbl = Context.getAdministrationService().getGlobalPropertiesByPrefix( "dhisreport" );

            //  System.out.println( "before"+ Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2SyncDate" ) );

            for ( GlobalProperty g : gbl )
            {
                // System.out.println( g.getProperty() );
                if ( g.getProperty().equals( "dhisreport.dhis2SyncDate" ) )
                {
                    //   System.out.println( g.getDescription() );
                    g.setPropertyValue( reportDate );
                    Context.getAdministrationService().saveGlobalProperty( g );

                }
            }

            // System.out.println( Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2SyncDate" ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinitions", service.getAllReportDefinitions() );

        return "redirect:/module/dhisreport/listDhis2Reports.form";
    }

    @RequestMapping( value = "/module/dhisreport/partReportSync", method = RequestMethod.GET )
    public void partReportSync( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinitions", service.getAllReportDefinitions() );
    }

    @RequestMapping( value = "/module/dhisreport/partReportSync.form", method = RequestMethod.POST )
    public String partReportSync( @RequestParam( "reportids" )
    String[] ids, HttpServletRequest request )
    {
        String temp = "";
        HttpSession httpSession = request.getSession();
        Integer reportId = null;
        try
        {
            DHIS2ReportingService dhisService = Context.getService( DHIS2ReportingService.class );
            if ( ids != null && ids.length > 0 )
            {
                for ( String sId : ids )
                {
                    // reportId = Integer.parseInt( sId );
                    //  System.out.println( sId );
                }
            }
        }
        catch ( Exception e )
        {
            httpSession.setAttribute( WebConstants.OPENMRS_MSG_ATTR, "Can not delete report " );
            log.error( e );
        }
        // httpSession.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
        // StringUtils.isBlank(temp) ? "sdmxhddataexport.report.deleted" :
        // temp);

        return "redirect:/module/dhisreport/partReportSync.form";
    }

}
