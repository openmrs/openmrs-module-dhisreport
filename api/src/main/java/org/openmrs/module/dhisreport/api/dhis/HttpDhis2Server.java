/**
 * Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 * This file is part of Billing module.
 *
 * Billing module is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Billing module is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Billing module. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 *
 */
package org.openmrs.module.dhisreport.api.dhis;


import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportStatus;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;

/**
 *
 * @author bobj
 */
public class HttpDhis2Server implements Dhis2Server
{

    private static Log log = LogFactory.getLog( HttpDhis2Server.class );

    public static final String REPORTS_METADATA_PATH =
        "/api/forms.xml";

    public static final String DATAVALUESET_PATH =
        "/api/dataValueSets?dataElementIdScheme=code&orgUnitIdScheme=uid";

    private URL url;

    private String username;

    private String password;

    public URL getUrl()
    {
        return url;
    }

    public void setUrl( URL url )
    {
        this.url = url;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public HttpDhis2Server()
    {
    }

    @Override
    public boolean isConfigured()
    {
        if ( username == null | password == null | url == null )
        {
            return false;
        }
        if ( username.isEmpty() | password.isEmpty() | url.getHost().isEmpty() )
        {
            return false;
        }
        
        return true;
    }

    @Override
    public ImportSummary postReport( DataValueSet report ) throws DhisException
    {
        log.debug( "Posting datavalueset report" );
        ImportSummary summary = null;
        try
        {
            StringWriter xmlReport = new StringWriter();
            JAXBContext jaxbDataValueSetContext = JAXBContext.newInstance( DataValueSet.class );
            Marshaller dataValueSetMarshaller = jaxbDataValueSetContext.createMarshaller();
            // output pretty printed
            dataValueSetMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
            dataValueSetMarshaller.marshal( report, xmlReport );

            String host = url.getHost();
            int port = url.getPort();

            HttpHost targetHost = new HttpHost( host, port, url.getProtocol() );
            DefaultHttpClient httpclient = new DefaultHttpClient();
            BasicHttpContext localcontext = new BasicHttpContext();

            try
            {
                HttpPost httpPost = new HttpPost( url.getPath() + DATAVALUESET_PATH );
                Credentials creds = new UsernamePasswordCredentials( username, password );
                Header bs = new BasicScheme().authenticate( creds, httpPost, localcontext );
                httpPost.addHeader( "Authorization", bs.getValue() );
                httpPost.addHeader( "Content-Type", "application/xml" );
                httpPost.addHeader( "Accept", "application/xml" );

                httpPost.setEntity( new StringEntity( xmlReport.toString() ) );
                HttpResponse response = httpclient.execute( targetHost, httpPost, localcontext );
                HttpEntity entity = response.getEntity();

                if ( entity != null )
                {
                    JAXBContext jaxbImportSummaryContext = JAXBContext.newInstance( ImportSummary.class );
                    Unmarshaller importSummaryUnMarshaller = jaxbImportSummaryContext.createUnmarshaller();
                    summary = (ImportSummary) importSummaryUnMarshaller.unmarshal( entity.getContent() );
                } else
                {
                    summary = new ImportSummary();
                    summary.setStatus( ImportStatus.ERROR );
                }
                // EntityUtils.consume( entity );


                // TODO: fix these catches ...
            } catch ( AuthenticationException ex )
            {
                log.debug( "Authentication error", ex );
            } catch ( IOException ex )
            {
                log.debug( "IO Exception", ex );
            } finally
            {
                httpclient.getConnectionManager().shutdown();
            }
        } catch ( JAXBException ex )
        {
            Logger.getLogger( HttpDhis2Server.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return summary;
    }

    @Override
    public ReportDefinition fetchReportTemplates() throws DhisException
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}