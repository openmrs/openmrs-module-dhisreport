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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class Dhis2ServerController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping(value = "/module/dhisreport/configureDhis2", method = RequestMethod.GET)
    public void showConfigForm( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        HttpDhis2Server server = service.getDhis2Server();

        if ( server == null )
        {
            server = new HttpDhis2Server();
        }

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "dhis2Server", server );

    }
    
    @RequestMapping(value = "/module/dhisreport/configureDhis2", method = RequestMethod.POST)
    public void saveConfig( ModelMap model,
        @RequestParam(value = "url", required = true) String urlString,
        @RequestParam(value = "username", required = true) String username,
        @RequestParam(value = "password", required = true) String password ) throws ParseException, MalformedURLException
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        HttpDhis2Server server = service.getDhis2Server();

        if ( server == null )
        {
            server = new HttpDhis2Server();
        }
        
        URL url = new URL (urlString);
        server.setUrl( url );
        server.setUsername( username );
        server.setPassword( password );
        
        service.setDhis2Server( server );
        
        log.debug( "Dhis2 server configured: "  + username + ":xxxxxx  " + url.toExternalForm() );
        
        model.addAttribute( "dhis2Server", server );
        model.addAttribute( "user", Context.getAuthenticatedUser() );
    }
}
