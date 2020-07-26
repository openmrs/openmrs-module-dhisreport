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
import org.openmrs.module.dhisreport.api.model.Category;
import org.openmrs.module.dhisreport.api.model.CategoryOption;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.Identifiable;

/**
 * Database methods for {@link DHIS2ReportingService}.
 */
public interface DHIS2ReportingDAO {

	public Location getLocationByOU_Code(String OU_Code);

	public SessionFactory getSessionFactory();

	public Identifiable saveObject(Identifiable object);

	/**
	 * Saves a given Disaggregation Object to the database If the Disaggregation exists on the
	 * database, this will return the existing object
	 *
	 * @param disaggregation a Disaggregation Object
	 * @return the saved Disaggregation Object
	 */
	public Disaggregation saveDisaggregation(Disaggregation disaggregation);

	/**
	 * Finds a Disaggregation object which has the given Category and Category Option
	 *
	 * @param category       the Category which should included in the Disaggregation
	 * @param categoryOption the Category Option which should included in the Disaggregation
	 * @return the Disaggregation Object if the a record exists on the database. Returns null if
	 * there's no record
	 */
	public Disaggregation getDisaggregationByCategoryAndCategoryOption(
			Category category, CategoryOption categoryOption);

	/**
	 * Finds Disaggregations by given Category
	 *
	 * @param category the Category which should be included in the Disaggregation
	 * @return a list of Disagregations
	 */
	public List<Disaggregation> getDisaggregationsByCategory(Category category);

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
