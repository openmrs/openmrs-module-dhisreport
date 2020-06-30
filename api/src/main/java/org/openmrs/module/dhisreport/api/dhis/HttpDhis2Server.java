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

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author bobj
 */
public class HttpDhis2Server implements Dhis2Server {

	private static Log log = LogFactory.getLog(HttpDhis2Server.class);

	public static final String REPORTS_METADATA_PATH = "/api/forms.xml";

	public static final String DATAVALUESET_PATH = "/api/dataValueSets?dataElementIdScheme=CODE&orgUnitIdScheme=CODE&idScheme=CODE";

	private URL url;

	private String username;

	private String password;

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
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
}
