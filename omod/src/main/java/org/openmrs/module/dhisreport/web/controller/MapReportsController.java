package org.openmrs.module.dhisreport.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.ReportService;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.reporting.definition.DefinitionSummary;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MapReportsController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping( value = "/module/dhisreport/mapReports", method = RequestMethod.GET )
    public void mapReports( ModelMap model, @RequestParam( value = "reportDefinition_id", required = true )
    Integer reportDefinition_id )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        List<DefinitionSummary> definitionSummaries = Context.getService( ReportDefinitionService.class )
            .getAllDefinitionSummaries( false );
        model.put( "definitionSummaries", definitionSummaries );
        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "reportDefinition", service.getReportDefinition( reportDefinition_id ) );
        model.addAttribute( "locations", Context.getLocationService().getAllLocations() );
    }
}
