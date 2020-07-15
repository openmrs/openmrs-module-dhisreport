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
package org.openmrs.module.dhisreport.api.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.model.Category;
import org.openmrs.module.dhisreport.api.model.CategoryOption;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata.DataSets;
import org.openmrs.module.dhisreport.api.model.Disaggregation;

/**
 * It is a default implementation of {@link DHIS2ReportingService}.
 */
public class DHIS2ReportingServiceImpl extends BaseOpenmrsService
		implements
			DHIS2ReportingService {

	protected final Log log = LogFactory.getLog(this.getClass());

	private DHIS2ReportingDAO dao;

	private HttpDhis2Server dhis2Server;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(DHIS2ReportingDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dao
	 */
	public DHIS2ReportingDAO getDao() {
		return dao;
	}

	@Override
	public HttpDhis2Server getDhis2Server() {
		return dhis2Server;
	}

	@Override
	public void setDhis2Server(HttpDhis2Server dhis2Server) {
		this.dhis2Server = dhis2Server;
	}

	@Override
	public Location getLocationByOU_Code(String OU_Code) {
		return dao.getLocationByOU_Code(OU_Code);
	}

	@Override
	public Location getLocationByOrgUnitCode(String orgUnitCode) {
		List<Location> locationList = new ArrayList<Location>();
		locationList.addAll(Context.getLocationService().getAllLocations());
		for (Location l : locationList) {
			for (LocationAttribute la : l.getActiveAttributes()) {
				if (la.getAttributeType().getName().equals("CODE")) {
					if ((la.getValue().toString()).equals(orgUnitCode)) {
						return l;
					}
				}

			}
		}
		return null;
	}

	@Override
	public void importDataSet(InputStream inputStream) throws JAXBException {
		// Unmarshal the XML file
		JAXBContext jaxbContext = JAXBContext
				.newInstance(Metadata.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Metadata metadata = (Metadata) jaxbUnmarshaller.unmarshal(inputStream);
		// Extract Category Options, Categories, Disaggregations, Data Elements and Data Sets
		Map<String, CategoryOption> categoryOptionMap = extractCategoryOptions(metadata);
		Map<String, Category> categoryMap = extractCategories(metadata);
		Map<String, DataElement> dataElementMap = extractDataElements(metadata);
		DataSet dataSet = extractDataset(metadata, dataElementMap);
		extractDisgarigations(metadata, categoryMap, categoryOptionMap);
	}

	/**
	 * Extract and save Data Elements from the metadata
	 *
	 * @param metadata the Metadata object
	 * @return a Map that contains the Data Element objects paired with uuid as the key
	 */
	private Map<String, DataElement> extractDataElements(Metadata metadata) {
		Map<String, DataElement> dataElementMap = new HashMap<>();
		for (Metadata.DataElements.DataElement de : metadata.getDataElements().getDataElement()) {
			DataElement dataElement = new DataElement();
			dataElement.setUid(de.getId());
			dataElement.setCode(de.getCode());
			dataElement.setName(de.getName());
			dao.saveObject(dataElement);
			dataElementMap.put(de.getId(), dataElement);
		}
		return dataElementMap;
	}

	/**
	 * Extract and save Categories from the metadata
	 *
	 * @param metadata the Metadata object
	 * @return a Map that contains the Category objects paired with uuid as the key
	 */
	private Map<String, Category> extractCategories(Metadata metadata) {
		Map<String, Category> categoryMap = new HashMap<>();
		for (Metadata.Categories.Category categoryMeta : metadata.getCategories().getCategory()) {
			Category category = new Category();
			category.setUid(categoryMeta.getId());
			category.setCode(categoryMeta.getCode());
			category.setName(categoryMeta.getName());
			dao.saveObject(category);
			categoryMap.put(categoryMeta.getId(), category);
		}
		return categoryMap;
	}

	/**
	 * Extract and save Category Options from the metadata
	 *
	 * @param metadata the Metadata object
	 * @return a Map that contains the Category Option objects paired with uuid as the key
	 */
	private Map<String, CategoryOption> extractCategoryOptions(Metadata metadata) {
		Map<String, CategoryOption> categoryOptionMap = new HashMap<>();
		for (Metadata.CategoryOptions.CategoryOption categoryOptionMeta :
				metadata.getCategoryOptions().getCategoryOption()) {
			CategoryOption categoryOption = new CategoryOption();
			categoryOption.setUid(categoryOptionMeta.getId());
			categoryOption.setCode(categoryOptionMeta.getCode());
			categoryOption.setName(categoryOptionMeta.getName());
			dao.saveObject(categoryOption);
			categoryOptionMap.put(categoryOptionMeta.getId(), categoryOption);
		}
		return categoryOptionMap;
	}

	/**
	 * Extract and save a Dataset from the metadata
	 *
	 * @param metadata the Metadata object
	 * @return a Map that contains the Category objects paired with uuid as the key
	 */
	private DataSet extractDataset(Metadata metadata,
			Map<String, DataElement> dataElementMap) {
		DataSets.DataSet dataSetMeta = metadata.getDataSets().getDataSet();
		DataSet dataSet = new DataSet();
		dataSet.setUid(dataSetMeta.getId());
		dataSet.setCode(dataSetMeta.getCode());
		dataSet.setName(dataSetMeta.getName());
		dataSet.setPeriodType(dataSetMeta.getPeriodType());
		Set<DataElement> dataElements = dataSet.getDataElements();
		// Add the saved data elements to the Dataset
		for (DataSets.DataSet.DataSetElements.DataSetElement dataElementMeta : dataSetMeta
				.getDataSetElements().getDataSetElement()) {
			dataElements.add(dataElementMap.get(dataElementMeta.getDataElement().getId()));
		}
		dao.saveObject(dataSet);
		return dataSet;
	}

	/**
	 * Extract and save Disaggregations from the metadata
	 *
	 * @param metadata the Metadata object
	 */
	private void extractDisgarigations(Metadata metadata, Map<String, Category> categoryMap,
			Map<String, CategoryOption> categoryOptionMap) {
		for (Metadata.Categories.Category categoryMeta : metadata.getCategories().getCategory()) {
			Category category = categoryMap.get(categoryMeta.getId());
			for (Metadata.Categories.Category.CategoryOptions.CategoryOption categoryOptionMeta : categoryMeta
					.getCategoryOptions().getCategoryOption()) {
				CategoryOption categoryOption = categoryOptionMap.get(categoryOptionMeta.getId());
				Disaggregation disaggregation = new Disaggregation();
				disaggregation.setCategory(category);
				disaggregation.setCategoryOption(categoryOption);
				dao.saveDisaggregation(disaggregation);
			}
		}
	}
}
