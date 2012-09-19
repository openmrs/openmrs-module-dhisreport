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
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
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
public class ExecuteReportController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping(value = "/module/dhisreport/executeReport", method = RequestMethod.POST)
    public void executeReport( ModelMap model, 
        @RequestParam(value = "reportDefinition_id", required = true) Integer reportDefinition_id,
        @RequestParam(value = "location", required = true) Integer location_id,
        @RequestParam(value = "date", required = true) String dateStr) throws ParseException
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        
        MonthlyPeriod period = new MonthlyPeriod( new SimpleDateFormat("yyyy-MM-dd").parse( dateStr ));
        Location location = Context.getLocationService().getLocation( location_id );
        
        DataValueSet dvs = service.evaluateReportDefinition( service.getReportDefinition( reportDefinition_id), period, location );
        
        //System.out.println("DVS: " + dvs.getDataSet());
        //System.out.println("DataValuesS: " + dvs.getDataValues().size());
        
        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "dataValueSet", dvs);
    }
}
