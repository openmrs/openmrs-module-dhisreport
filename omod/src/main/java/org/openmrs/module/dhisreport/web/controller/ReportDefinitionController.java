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


import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.Dhis2Server;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * The main controller.
 */
@Controller
public class ReportDefinitionController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping(value = "/module/dhisreport/loadReportDefinitions", method = RequestMethod.GET)
    public void uploadForm( ModelMap model )
    {
        model.addAttribute( "user", Context.getAuthenticatedUser() );
    }

    @RequestMapping(value = "/module/dhisreport/loadReportDefinitions", method = RequestMethod.POST)
    public void upload( ModelMap model, HttpServletRequest request ) throws Exception
    {
        model.addAttribute( "user", Context.getAuthenticatedUser() );
        
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    	MultipartFile multipartFile = multipartRequest.getFile("datafile");

        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        InputStream is = multipartFile.getInputStream();
        try {
            service.unMarshallandSaveReportTemplates( is );
        } catch (Exception ex) {
            log.error( "Error loading file: " + ex );
        } finally {
            is.close();
        }
    }

    @RequestMapping(value = "/module/dhisreport/exportReportDefinitions", method = RequestMethod.GET)
    public void export( ModelMap model, HttpServletResponse response ) throws Exception
    {
        response.setContentType( "application/xml");
        response.setCharacterEncoding( "UTF-8");
        response.addHeader( "Content-Disposition", "attachment; filename=reportDefinition.xml");
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        ReportTemplates templates = service.getReportTemplates();
        service.marshallReportTemplates( response.getOutputStream(), templates);        
    }

}
