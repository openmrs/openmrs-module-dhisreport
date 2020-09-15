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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.JAXBException;
import org.openmrs.Location;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dhisreport.api.adx.importsummary.AdxImportSummary;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
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

	/**
	 * Finds a dataset by UUID
	 *
	 * @return the DataSet
	 */
	public DataSet getDataSetByUuid(String uuid);

	/**
	 * Updates the Report mapping of a DataSet
	 *
	 * @param dataSet the DataSet which needs to be updated
	 * @param reportUuid the UUID of a Period indicator Report Definition
	 */
	public void updateReportOfADataSet(DataSet dataSet, String reportUuid);

	/**
	 * Finds Data Value Templates by given DataSet
	 *
	 * @param dataSet the Category which should be included in the Data Value Templates
	 * @return a list of Data Value Templates
	 */
	public List<DataValueTemplate> getDataValueTemplatesByDataSet(DataSet dataSet);

	/**
	 * Updates the Report Indicator of a DataValueTemplate
	 *
	 * @param dataValueTemplateId the ID of the DataValueTemplate
	 * @param reportIndicatorUuid the new reportIndicatorUuid
	 */
	public void updateReportIndicatorOfDataValueTemplate(Integer dataValueTemplateId, String reportIndicatorUuid);

	/**
	 * Post DataSet to the connected DHIS2 Instance
	 * @param uid UID of DataSet
	 * @param locationUuid UUID of OpenMRS location
	 * @param startDate starting day of the period
	 * @return the ADX Import Summary returned by DHIS2
	 * @throws DHIS2ReportingException if failed to post data
	 */
	public AdxImportSummary postDataSetToDHIS2(String uid, String locationUuid, Date startDate) throws DHIS2ReportingException;

	/**
	 * Gets the Attribute Type that stores dhis2 Organisation Unit code.
	 *
	 * @return the Optional instance that contains the Attribute type
	 */
	public Optional<LocationAttributeType> getDhis2OrgUnitLocationAttributeType();

	/**
	 * Maps a given location with a DHIS2 Organisation Unit
	 *
	 * @param locationUuid UUID of the location
	 * @param dhis2OrgUnitUid UID of the DHIS2 Organisation Unit
	 * @throws DHIS2ReportingException if unable to map the location
	 */
	public void mapLocationWithDhis2OrgUnit(String locationUuid, String dhis2OrgUnitUid) throws DHIS2ReportingException;
}
