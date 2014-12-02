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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
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
public class Dhis2ServerController
{

    protected final Log log = LogFactory.getLog( getClass() );

    @RequestMapping( value = "/module/dhisreport/configureDhis2", method = RequestMethod.GET )
    public void showConfigForm( ModelMap model )
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );

        HttpDhis2Server server = service.getDhis2Server();
        String dhisurl = Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2URL" );
        String dhisusername = Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2UserName" );
        String dhispassword = Context.getAdministrationService().getGlobalProperty( "dhisreport.dhis2Password" );

        URL url = null;
        try
        {
            url = new URL( dhisurl );
        }
        catch ( MalformedURLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ( server == null )
        {
            server = new HttpDhis2Server();
        }
        server.setUrl( url );
        server.setUsername( dhisusername );
        server.setPassword( dhispassword );

        model.addAttribute( "user", Context.getAuthenticatedUser() );
        model.addAttribute( "dhis2Server", server );

    }

    @RequestMapping( value = "/module/dhisreport/configureDhis2", method = RequestMethod.POST )
    public void saveConfig( ModelMap model, @RequestParam( value = "url", required = true )
    String urlString, @RequestParam( value = "username", required = true )
    String username, @RequestParam( value = "password", required = true )
    String password, WebRequest webRequest )
        throws ParseException, MalformedURLException
    {
        DHIS2ReportingService service = Context.getService( DHIS2ReportingService.class );
        HttpDhis2Server server = service.getDhis2Server();

        List<GlobalProperty> gbl = Context.getAdministrationService().getGlobalPropertiesByPrefix( "dhisreport" );

        // System.out.println( "parameters received on post request" + urlString + username + password );

        if ( server == null )
        {
            server = new HttpDhis2Server();
        }

        URL url = new URL( urlString );
        server.setUrl( url );
        server.setUsername( username );
        server.setPassword( password );

        service.setDhis2Server( server );

        boolean val = testConnection( url, username, password, server, webRequest, model );

        if ( val == true )
        {
            for ( GlobalProperty g : gbl )
            {
                if ( g.getProperty().equals( "dhisreport.dhis2URL" ) )
                {
                    //System.out.println( "Setting URL" + g.getProperty().equals( "dhisreport.dhis2URL" ) );
                    g.setPropertyValue( urlString );

                }
                if ( g.getProperty().equals( "dhisreport.dhis2UserName" ) )
                {
                    //System.out.println( " Setting  username as -" + g.getProperty().equals( "dhisreport.dhis2UserName" ) );

                    g.setPropertyValue( username );
                }
                if ( g.getProperty().equals( "dhisreport.dhis2Password" ) )
                {
                    // System.out.println( "setting password-" + g.getProperty().equals( "dhisreport.dhis2Password" ) );

                    g.setPropertyValue( password );
                }
            }
        }
    }

    /*
     * To test the http connection
     */
    public boolean testConnection( URL url, String username, String password, HttpDhis2Server server,
        WebRequest webRequest, ModelMap model )
    {
        String host = url.getHost();
        int port = url.getPort();

        HttpHost targetHost = new HttpHost( host, port, url.getProtocol() );
        DefaultHttpClient httpclient = new DefaultHttpClient();
        BasicHttpContext localcontext = new BasicHttpContext();

        try
        {
            HttpGet httpGet = new HttpGet( url.getPath() ); // +
            // DATAVALUESET_PATH
            // );
            httpGet.addHeader( BasicScheme.authenticate( new UsernamePasswordCredentials( username, password ),
                "UTF-8", false ) );

            HttpResponse response = httpclient.execute( targetHost, httpGet, localcontext );

            //System.out.println( "Http Response :" + response + ":" + response.getStatusLine().getStatusCode() );

            if ( response.getStatusLine().getStatusCode() == 200 )
            {
                log.debug( "Dhis2 server configured: " + username + ":xxxxxx  " + url.toExternalForm() );

                model.addAttribute( "dhis2Server", server );
                model.addAttribute( "user", Context.getAuthenticatedUser() );
                webRequest.setAttribute( WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
                    "dhisreport.saveConfigSuccess" ), WebRequest.SCOPE_SESSION );
                return true;
            }

            else
            {
                log.debug( "Dhis2 server not configured" );

                model.addAttribute( "dhis2Server", server );
                model.addAttribute( "user", Context.getAuthenticatedUser() );
                webRequest.setAttribute( WebConstants.OPENMRS_MSG_ATTR, Context.getMessageSourceService().getMessage(
                    "dhisreport.saveConfigFailure" ), WebRequest.SCOPE_SESSION );
                return false;
            }
        }
        catch ( IOException ex )
        {
            log.debug( "Problem accessing DHIS2 server: " + ex.toString() );
            return false;
        }
        finally
        {
            httpclient.getConnectionManager().shutdown();
        }
    }
}
