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
package org.openmrs.module.dhisreport.api.db;

import java.util.List;
import org.hibernate.SessionFactory;
import org.openmrs.Location;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Identifiable;

/**
 * Database methods for {@link DHIS2ReportingService}.
 */
public interface DHIS2ReportingDAO {

	public Location getLocationByOU_Code(String OU_Code);

	public SessionFactory getSessionFactory();

	public Identifiable saveObject(Identifiable object);

	/**
	 * Finds DataValueTemplate by id
	 *
	 * @param id the ID of the DAtaValueTemplate
	 * @return the corresponding DataValueTemplate or null
	 */
	public DataValueTemplate getDataValueTemplateById(Integer id);

	/**
	 * Finds Data Value Templates by given DataSet
	 *
	 * @param dataSet the Category which should be included in the Data Value Templates
	 * @return a list of Data Value Templates
	 */
	public List<DataValueTemplate> getDataValueTemplatesByDataSet(DataSet dataSet);

	/**
	 * Saves a given Data Value Template Object to the database
	 *
	 * @param dataValueTemplate a Data Value Template object
	 * @return the saved Data Value Template object
	 */
	public DataValueTemplate saveDataValueTemplate(DataValueTemplate dataValueTemplate);

	/**
	 * Removes Data Value Templates by a given DataSet
	 *
	 * @param dataSet a Dataset object
	 */
	public void removeDataValueTemplatesByDataSet(DataSet dataSet);

	/**
	 * Finds all imported DataSets from the database.
	 *
	 * @return a List of Datasets
	 */
	public List<DataSet> getAllDataSets();

	/**
	 * Finds a dataset by UUID
	 *
	 * @return the DataSet
	 */
	public DataSet getDataSetByUid(String uid);

}
