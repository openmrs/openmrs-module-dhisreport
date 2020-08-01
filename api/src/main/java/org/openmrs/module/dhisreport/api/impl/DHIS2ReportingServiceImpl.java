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
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.adx.DataValueType;
import org.openmrs.module.dhisreport.api.adx.GroupType;
import org.openmrs.module.dhisreport.api.adx.importsummary.AdxImportSummary;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.model.Category;
import org.openmrs.module.dhisreport.api.model.CategoryOption;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata.DataSets;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.module.dhisreport.api.utils.WeeklyPeriod;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition.CohortIndicatorAndDimensionColumn;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.Report;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.openmrs.module.reporting.report.renderer.RenderingMode;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.module.reporting.web.renderers.DefaultWebRenderer;

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
		//
		generateDataValueTemplates(metadata, dataSet, categoryMap);
	}

	@Override
	public List<DataSet> getAllDataSets(){
		return dao.getAllDataSets();
	}

	@Override
	public DataSet getDataSetByUid(String uid){
		return dao.getDataSetByUid(uid);
	}

	@Override
	public void updateReportOfADataSet(DataSet dataSet, String reportUuid){
		List<DataValueTemplate> dataValueTemplates = dao.getDataValueTemplatesByDataSet(dataSet);
		// Remove existing report definition mappings of the filtered Data Value Templates
		dataValueTemplates.forEach(dataValueTemplate -> {
			if(dataValueTemplate.getReportIndicatorUuid() != null){
				dataValueTemplate.setReportIndicatorUuid(null);
				dao.saveDataValueTemplate(dataValueTemplate);
			}
		});
		// Set new report Uuid
		dataSet.setReportUuid(reportUuid);
		dao.saveObject(dataSet);
	}

	@Override
	public List<DataValueTemplate> getDataValueTemplatesByDataSet(DataSet dataSet) {
			return dao.getDataValueTemplatesByDataSet(dataSet);
	}

	@Override
	public void updateReportIndicatorOfDataValueTemplate(Integer dataValueTemplateId, String reportIndicatorUuid){
		DataValueTemplate dataValueTemplate =  dao.getDataValueTemplateById(dataValueTemplateId);
		// Todo: Check the existence of the dataValueTemplate and the reportIndicator
		dataValueTemplate.setReportIndicatorUuid(reportIndicatorUuid);
		dao.saveDataValueTemplate(dataValueTemplate);
	}

	@Override
	public AdxImportSummary postDataSetToDHIS2(String uid, String locationUuid, Date startDate) throws DHIS2ReportingException {
		DataSet dataSet = getDataSetByUid(uid);
		String organizationUnit = getOrganizationUnit(locationUuid);
		Period period = generatePeriod(startDate, dataSet.getPeriodType());
		Report report = executeReportDefinition(dataSet.getReportUuid(), period);
		Map<DataValueTemplate, String> mappedDataValueTemplates = mapReportWithDataSet(report, dataSet);
		AdxType adxTemplate = generateAdxTemplate(mappedDataValueTemplates, dataSet, organizationUnit, period.getAdxPeriod());
		return dhis2Server.postAdxData(adxTemplate);
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
	 * @return the extracted Data Set
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
	 * @param categoryMap a map which contains Categories paired with UUIDs
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

	/**
	 * Generates Data Value Templates from the metadata. This method removes the previously generated
	 * Data value Templates if the DataSet has been imported before.
	 *
	 * @param metadata    the Metadata Object
	 * @param dataSet     extracted DataSet
	 * @param categoryMap a map which contains Categories paired with UUIDs
	 */
	private void generateDataValueTemplates(Metadata metadata, DataSet dataSet,
			Map<String, Category> categoryMap) {
		// Remove previously generated Data Value Templates for the given DataSet
		dao.removeDataValueTemplatesByDataSet(dataSet);
		for (DataElement dataElement : dataSet.getDataElements()) {
			// Get the corresponding DataElement meta object from the metadata
			Optional<Metadata.DataElements.DataElement> dataElementMeta =
					metadata.getDataElements().getDataElement()
							.stream()
							.filter(dataElementMetaObject -> dataElementMetaObject.getId()
									.equals(dataElement.getUid()))
							.findFirst();
			// Get the corresponding Category Combo from the metadata using the DataElement meta object
			Optional<Metadata.CategoryCombos.CategoryCombo> categoryComboMeta = metadata
					.getCategoryCombos().getCategoryCombo()
					.stream()
					.filter(categoryComboMetaObject -> categoryComboMetaObject.getId()
							.equals(dataElementMeta.get().getCategoryCombo().getId()))
					.findFirst();
			// Create a list of Categories of the DataElement using the Category Combo
			List<Category> categories = categoryComboMeta.get().getCategories().getCategory()
					.stream()
					.map(category -> categoryMap.get(category.getId()))
					.collect(Collectors.toList());
			// Get Disaggregations of each Category and group them by Category
			List<List<Disaggregation>> groupedDisaggregationList = categories.stream()
					.map(category -> dao.getDisaggregationsByCategory(category))
					.collect(Collectors.toList());
			// Generate disaggregation combinations for the current DataElement
			List<List<Disaggregation>> combinations = getDisaggregationCombinations(
					groupedDisaggregationList, 0);
			// Save Data Value Templates for each generated combinations
			for (List<Disaggregation> combination : combinations
			) {
				DataValueTemplate dataValueTemplate = new DataValueTemplate();
				dataValueTemplate.setDataSet(dataSet);
				dataValueTemplate.setDataElement(dataElement);
				dataValueTemplate.setDisaggregations(new HashSet<>(combination));
				dao.saveDataValueTemplate(dataValueTemplate);
			}
		}
	}

	/**
	 * Generates Disaggregation combinations recursively.
	 *
	 * @param groupedDisaggregationList a list of lists of Disaggregations grouped by Categories
	 * @param position                  the current position of the groupedDisaggregationList
	 * @return a list that contains Disaggregation combinations
	 */
	private List<List<Disaggregation>> getDisaggregationCombinations(
			List<List<Disaggregation>> groupedDisaggregationList, int position) {
		List<List<Disaggregation>> combinations = new ArrayList<>();
		// Get current group from the groupedDisaggregationList
		List<Disaggregation> currentDisaggregationGroup = groupedDisaggregationList.get(position);
		// Iterate each disaggregation and add combinations to the combinations list
		for (Disaggregation disaggregation :
				currentDisaggregationGroup) {
			if (position < groupedDisaggregationList.size() - 1) {
				// Recall the method to get combinations from the bottom level
				List<List<Disaggregation>> depthCombinations = getDisaggregationCombinations(
						groupedDisaggregationList,
						position + 1);
				// Append the current Disaggregation to each retrieved combination
				for (List<Disaggregation> combination : depthCombinations
				) {
					combination.add(disaggregation);
					combinations.add(combination);
				}
			} else {
				// Create a new combination and add it to the combinations list if the recursion reached
				// to the bottom level
				List<Disaggregation> tempCombination = new ArrayList<>();
				tempCombination.add(disaggregation);
				combinations.add(tempCombination);
			}
		}
		return combinations;
	}

	/**
	 * Executes a provided Report Definition.
	 *
	 * @param reportDefinitionUuid UUID of the Report Definition
	 * @param period period object
	 * @return the executed report
	 * @throws DHIS2ReportingException if the mapperd Report Definition doesn't exists
	 */
	private Report executeReportDefinition(String reportDefinitionUuid, Period period)
			throws DHIS2ReportingException {
		ReportDefinition reportDefinition = Context.getService(ReportDefinitionService.class).getDefinitionByUuid(reportDefinitionUuid);
		if(reportDefinition == null){
			throw new DHIS2ReportingException("The mapped report Definition doesn't exists.");
		}
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("startDate", period.getStartDate());
		parameters.put("endDate", period.getEndDate());
		ReportRequest reportRequest = new ReportRequest();
		reportRequest.setReportDefinition(new Mapped<>(
				reportDefinition, parameters));
		reportRequest.setRenderingMode(new RenderingMode(new DefaultWebRenderer(),
				"Web", null, 100));
		return Context.getService(ReportService.class).runReport(reportRequest);
	}

	/**
	 * Generates a period object with ADX Period type.
	 *
	 * @param startDate starting Date of the period
	 * @param periodType period type
	 * @return a period object
	 * @throws DHIS2ReportingException if the period type is not supported by the module
	 */
	private Period generatePeriod(Date startDate, String periodType)
			throws DHIS2ReportingException {
		switch (periodType){
			case "Weekly":
				return new WeeklyPeriod(startDate);
			case "Monthly":
				return new MonthlyPeriod(startDate);
			default:
				throw new DHIS2ReportingException("Unsupported Period Type: "+ periodType);
		}
	}

	/**
	 * Gets the mapped DHIS2 Organization code of an OpenMRS location.
	 *
	 * @param locationUuid UUID of OpenMRS location
	 * @return the Code of the mapped DHIS2 Organization Unit
	 * @throws DHIS2ReportingException if the provided location is invalid or not mapped with a DHIS2
	 *                                 Organization Unit
	 */
	private String getOrganizationUnit(String locationUuid) throws DHIS2ReportingException {
		Optional<LocationAttributeType> maybeLocationAttributeType = getCodeAttributeType();
		if (!maybeLocationAttributeType.isPresent()) {
			throw new DHIS2ReportingException("Location is not mapped with an organisation");
		}
		Location location = Context.getLocationService().getLocationByUuid(locationUuid);
		if (location == null) {
			throw new DHIS2ReportingException("Invalid Location");
		}
		return (String) location
				.getActiveAttributes(maybeLocationAttributeType.get()).get(0).getValue();
	}

	/**
	 * Gets the Attribute Type that stores dhis2 Organization Unit code.
	 *
	 * @return the Optional instance that contains the Attribute type
	 */
	private Optional<LocationAttributeType> getCodeAttributeType() {
		return Context.getLocationService()
				.getAllLocationAttributeTypes().stream()
				.filter(locationAttributeType -> locationAttributeType.getName().equals("CODE"))
				.findFirst();
	}

	/**
	 * Maps a Report's value with a DataSet's Data Value Templates.
	 *
	 * @param report executed OpenMRS report
	 * @param dataSet DataSet to be mapped with
	 * @return mapped DataValues
	 */
	private Map<DataValueTemplate, String> mapReportWithDataSet(Report report, DataSet dataSet) {
		Map<DataValueTemplate, String> mappedDataValueTemplates = new HashMap<>();
		Map<String, String> indicatorValues = new HashMap<>();
		report.getReportData().getDataSets().forEach((key, reportingDataSet) -> {
			reportingDataSet.forEach(dataSetRow -> {
				dataSetRow.getColumnValues().forEach((dataSetColumn, value) -> {
					if (dataSetColumn instanceof CohortIndicatorAndDimensionColumn) {
						String uuid = ((CohortIndicatorAndDimensionColumn) dataSetColumn).getIndicator()
								.getParameterizable().getCohortDefinition().getParameterizable().getUuid();
						indicatorValues.put(uuid, value.toString());
					}
				});
			});
		});

		List<DataValueTemplate> dataValueTemplates = dao.getDataValueTemplatesByDataSet(dataSet);
		dataValueTemplates.forEach(dataValueTemplate -> {
			String reportIndicatorUuid = dataValueTemplate.getReportIndicatorUuid();
			if (indicatorValues.containsKey(reportIndicatorUuid)) {
				mappedDataValueTemplates.put(dataValueTemplate, indicatorValues.get(reportIndicatorUuid));
			}
		});

		return mappedDataValueTemplates;
	}

	/**
	 * Generates a new ADX Template for a given DataSet that is ready to be sent to DHIS2.
	 *
	 * @param mappedDataValueTemplates a map contains Data Value Templates mapped with corresponding values
	 * @param dataSet the parent DataSet of Data Value Templates
	 * @param organizationUnitCode code of DHIS2 Organization Unit
	 * @param adxPeriod ADX period string for the period of data
	 * @return generated ADX template
	 * @throws DHIS2ReportingException if unable to set exported time
	 */
	private AdxType generateAdxTemplate(Map<DataValueTemplate, String> mappedDataValueTemplates, DataSet dataSet, String organizationUnitCode, String adxPeriod)
			throws DHIS2ReportingException {
		AdxType adxTemplate = new AdxType();
		try {
			Instant currentTime = Instant.now();
			adxTemplate.setExported(DatatypeFactory.newInstance().newXMLGregorianCalendar(currentTime.toString()));
		} catch (DatatypeConfigurationException e) {
			throw new DHIS2ReportingException("Error occurred while setting the date for ADX template");
		}
		GroupType groupType = new GroupType();
		groupType.setDataSet(dataSet.getUid());
		groupType.setOrgUnit(organizationUnitCode);
		groupType.setPeriod(adxPeriod);
		adxTemplate.getGroup().add(groupType);
		List<DataValueType> dataValueTypes = groupType.getDataValue();
		mappedDataValueTemplates.forEach((dataValueTemplate, value) -> {
			DataValueType dataValueType = new DataValueType();
			dataValueType.setDataElement(dataValueTemplate.getDataElement().getUid());
			dataValueType.setValue(new BigDecimal(value));
			Map<QName, String> otherAttributes = dataValueType.getOtherAttributes();
			dataValueTemplate.getDisaggregations().forEach(disaggregation -> {
					String categoryCode = disaggregation.getCategory().getCode();
					String categoryOptionUid = disaggregation.getCategoryOption().getUid();
					otherAttributes.put(new QName(categoryCode), categoryOptionUid);
			});
			dataValueTypes.add(dataValueType);
		});
		return adxTemplate;
	}

}
