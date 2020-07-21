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
package org.openmrs.module.dhisreport.api;

import java.io.InputStream;
import java.util.List;
import javax.xml.bind.JAXBException;
import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean
 * which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(DHIS2ReportingService.class).someMethod();
 * </code>
 *
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface DHIS2ReportingService extends OpenmrsService {

	// -----------------------------------------------------------------------
	// DHIS Rest API calls
	// -----------------------------------------------------------------------
	/**
	 * Initialize url and credentials for DHIS server
	 *
	 * @param server
	 */
	public void setDhis2Server(HttpDhis2Server server);

	/**
	 * @return the Dhis2 server
	 */
	public HttpDhis2Server getDhis2Server();

	/**
	 * @param OU_Code
	 * @return
	 */
	public Location getLocationByOU_Code(String OU_Code);

	public Location getLocationByOrgUnitCode(String orgUnitCode);

	public DHIS2ReportingDAO getDao();

	/**
	 * Imports a Dataset along with metadata
	 * @param inputStream the input stream of the import file
	 * @throws JAXBException if the input stream cannot unmarshal
	 */
	public void importDataSet(InputStream inputStream) throws JAXBException;

	/**
	 * Gets all imported DataSets.
	 *
	 * @return a List of Datasets
	 */
	public List<DataSet> getAllDataSets();

}
