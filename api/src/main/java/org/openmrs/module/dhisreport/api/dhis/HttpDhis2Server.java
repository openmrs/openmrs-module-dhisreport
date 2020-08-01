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
package org.openmrs.module.dhisreport.api.dhis;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.hisp.dhis.dxf2.Dxf2Exception;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.adx.importsummary.AdxImportSummary;

/**
 *
 * @author bobj
 */
public class HttpDhis2Server implements Dhis2Server {

	private static Log log = LogFactory.getLog(HttpDhis2Server.class);

	public static final String DATAVALUESET_PATH = "/api/dataValueSets?orgUnitIdScheme=CODE";

	private URL url;

	private String username;

	private String password;

	public URL getUrl() throws MalformedURLException {
		if(url == null){
			url = new URL(Context.getAdministrationService()
					.getGlobalProperty("dhisreport.dhis2URL"));
		}
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getPassword() {
		if(password == null){
			password = Context.getAdministrationService()
					.getGlobalProperty("dhisreport.dhis2Password");
		}
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		if(username == null){
			username = Context.getAdministrationService()
					.getGlobalProperty("dhisreport.dhis2UserName");
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public HttpDhis2Server() {
	}

	@Override
	public boolean isConfigured() {
		if (username == null | password == null | url == null) {
			return false;
		}
		if (username.isEmpty() | password.isEmpty() | url.getHost().isEmpty()) {
			return false;
		}

		return true;
	}

	@Override
	public AdxImportSummary postAdxData(AdxType adxTemplate)
			throws DHIS2ReportingException {
		StringWriter xmlPayload = new StringWriter();
		try {
			JAXBContext jaxbDataValueSetContext = JAXBContext
					.newInstance(AdxType.class);
			Marshaller adxTypeMarshaller = jaxbDataValueSetContext
					.createMarshaller();
			adxTypeMarshaller.marshal(adxTemplate, xmlPayload);
		} catch (JAXBException ex) {
			throw new Dxf2Exception("Problem marshalling adxtype", ex);
		}
		// Todo: Post Data to DHIS2 and return the
		AdxImportSummary importSummary = null;

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String dhis2url = this.getUrl().toString();
			HttpPost httpPost = new HttpPost(dhis2url + DATAVALUESET_PATH);
			Credentials creds = new UsernamePasswordCredentials(getUsername(),
					getPassword());
			Header bs = new BasicScheme().authenticate(creds, httpPost, null);
			httpPost.addHeader("Authorization", bs.getValue());
			httpPost.addHeader("Content-Type", "application/adx+xml");
			httpPost.addHeader("Accept", "application/xml");
			httpPost.setEntity(new StringEntity(xmlPayload.toString()));

			CloseableHttpResponse response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new Dhis2Exception(this, response.getStatusLine()
						.getReasonPhrase(), null);
			}
			if (entity != null) {
				JAXBContext jaxbImportSummaryContext = JAXBContext
						.newInstance(AdxImportSummary.class);
				Unmarshaller importSummaryUnMarshaller = jaxbImportSummaryContext
						.createUnmarshaller();
				importSummary = (AdxImportSummary) importSummaryUnMarshaller
						.unmarshal(entity.getContent());
			} else {
				importSummary = new AdxImportSummary();
			}
		} catch (JAXBException ex) {
			throw new Dhis2Exception(this,
					"Problem unmarshalling AdxImportSummary", ex);
		} catch (MalformedURLException ex) {
			throw new Dhis2Exception(this,
					"The provided DHIS2 URL isn't valid", ex);
		} catch (AuthenticationException ex) {
			throw new Dhis2Exception(this,
					"Problem authenticating to DHIS2 server", ex);
		} catch (IOException ex) {
			throw new Dhis2Exception(this, "Problem accessing DHIS2 server", ex);
		}

		return importSummary;
	}
}
