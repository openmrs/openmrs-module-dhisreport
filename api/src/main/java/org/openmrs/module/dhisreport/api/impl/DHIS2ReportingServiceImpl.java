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
import org.openmrs.module.dhisreport.api.model.CategoryOptionCombo;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata.DataSets;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.utils.BiMonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.DailyPeriod;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.module.dhisreport.api.utils.QuarterlyPeriod;
import org.openmrs.module.dhisreport.api.utils.WeeklyPeriod;
import org.openmrs.module.dhisreport.api.utils.WeeklySaturdayPeriod;
import org.openmrs.module.dhisreport.api.utils.WeeklySundayPeriod;
import org.openmrs.module.dhisreport.api.utils.WeeklyThursdayPeriod;
import org.openmrs.module.dhisreport.api.utils.WeeklyWednesdayPeriod;
import org.openmrs.module.dhisreport.api.utils.YearlyPeriod;
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
	public void importDataSet(InputStream inputStream) throws JAXBException {
		// Unmarshal the XML file
		JAXBContext jaxbContext = JAXBContext
				.newInstance(Metadata.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Metadata metadata = (Metadata) jaxbUnmarshaller.unmarshal(inputStream);
		// Extract metadata and store in the DB
		Map<String, Set<CategoryOptionCombo>> categoryOptionComboMap = extractCategoryOptionCombos(metadata);
		Map<String, DataElement> dataElementMap = extractDataElements(metadata);
		DataSet dataSet = extractDataset(metadata, dataElementMap);
		generateDataValueTemplates(metadata,dataSet,categoryOptionComboMap);
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
		String organisationUnit = getOrganisationUnit(locationUuid);
		Period period = generatePeriod(startDate, dataSet.getPeriodType());
		Report report = executeReportDefinition(dataSet.getReportUuid(), period);
		Map<DataValueTemplate, String> mappedDataValueTemplates = mapReportWithDataSet(report, dataSet);
		AdxType adxTemplate = generateAdxTemplate(mappedDataValueTemplates, dataSet, organisationUnit, period.getAdxPeriod());
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
			dataElement.setName(de.getName());
			dao.saveObject(dataElement);
			dataElementMap.put(de.getId(), dataElement);
		}
		return dataElementMap;
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
	 * Generates a Map of CategoryOptionCombos grouped by CategoryCombo Uid
	 *
	 * @param metadata the Metadata Object
	 * @return the generated map
	 */
	private Map<String, Set<CategoryOptionCombo>> extractCategoryOptionCombos(Metadata metadata) {
		Map<String, Set<CategoryOptionCombo>> categoryOptionComboMap = new HashMap<>();
		metadata.getCategoryCombos().getCategoryCombo().forEach(
				categoryCombo -> categoryOptionComboMap.put(categoryCombo.getId(), new HashSet<>()));
		metadata.getCategoryOptionCombos().getCategoryOptionCombo().forEach(categoryOptionComboMeta -> {
			CategoryOptionCombo categoryOptionCombo = new CategoryOptionCombo();
			categoryOptionCombo.setUid(categoryOptionComboMeta.getId());
			categoryOptionCombo.setName(categoryOptionComboMeta.getName());
			dao.saveObject(categoryOptionCombo);
			categoryOptionComboMap.get(categoryOptionComboMeta.getCategoryCombo().getId())
					.add(categoryOptionCombo);
		});
		return categoryOptionComboMap;
	}

	/**
	 * Generates Data Value Templates from the metadata. This method removes the previously generated
	 * Data value Templates if the DataSet has been imported before.
	 *
	 * @param metadata    the Metadata Object
	 * @param dataSet     extracted DataSet
	 * @param categoryOptionComboMap a map of CategoryOptionCombos grouped by CategoryComboUid
	 */
	private void generateDataValueTemplates(Metadata metadata, DataSet dataSet, Map<String, Set<CategoryOptionCombo>> categoryOptionComboMap) {
		dao.removeDataValueTemplatesByDataSet(dataSet);
		dataSet.getDataElements().forEach(dataElement -> {
			Optional<Metadata.DataElements.DataElement> dataElementMeta =
					metadata.getDataElements().getDataElement()
							.stream()
							.filter(obj -> obj.getId().equals(dataElement.getUid()))
							.findFirst();
			Set<CategoryOptionCombo> categoryOptionCombos = categoryOptionComboMap.get(dataElementMeta.get().getCategoryCombo().getId());
			categoryOptionCombos.forEach(categoryOptionCombo -> {
				DataValueTemplate dataValueTemplate = new DataValueTemplate();
				dataValueTemplate.setDataSet(dataSet);
				dataValueTemplate.setDataElement(dataElement);
				dataValueTemplate.setCategoryOptionCombo(categoryOptionCombo);
				dao.saveDataValueTemplate(dataValueTemplate);
			});
		});
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
			case "Daily":
				return new DailyPeriod(startDate);
			case "Weekly":
				return new WeeklyPeriod(startDate);
			case "WeeklyWednesday":
				return new WeeklyWednesdayPeriod(startDate);
			case "WeeklyThursday":
				return new WeeklyThursdayPeriod(startDate);
			case "WeeklySaturday":
				return new WeeklySaturdayPeriod(startDate);
			case "WeeklySunday":
				return new WeeklySundayPeriod(startDate);
			case "Monthly":
				return new MonthlyPeriod(startDate);
			case "BiMonthly":
				return new BiMonthlyPeriod(startDate);
			case "Quarterly":
				return new QuarterlyPeriod(startDate);
			case "Yearly":
				return new YearlyPeriod(startDate);
			default:
				throw new DHIS2ReportingException("Unsupported Period Type: "+ periodType);
		}
	}

	/**
	 * Gets the mapped DHIS2 Organisation code of an OpenMRS location.
	 *
	 * @param locationUuid UUID of OpenMRS location
	 * @return the Code of the mapped DHIS2 Organisation Unit
	 * @throws DHIS2ReportingException if the provided location is invalid or not mapped with a DHIS2
	 *                                 Organisation Unit
	 */
	private String getOrganisationUnit(String locationUuid) throws DHIS2ReportingException {
		Optional<LocationAttributeType> maybeLocationAttributeType = getDhis2OrgUnitLocationAttributeType();
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

	public Optional<LocationAttributeType> getDhis2OrgUnitLocationAttributeType() {
		return Context.getLocationService()
				.getAllLocationAttributeTypes().stream()
				.filter(locationAttributeType -> locationAttributeType.getName().equals("DHIS2_ORG_UNIT"))
				.findFirst();
	}

	public void mapLocationWithDhis2OrgUnit(String locationUuid, String dhis2OrgUnitUid) throws DHIS2ReportingException {
		Location location = Context.getLocationService().getLocationByUuid(locationUuid);
		if (location == null) {
			throw new DHIS2ReportingException("Invalid Location");
		}
		Optional<LocationAttributeType> maybeLocationAttributeType = getDhis2OrgUnitLocationAttributeType();
		LocationAttributeType attributeType;
		if (maybeLocationAttributeType.isPresent()) {
			attributeType = maybeLocationAttributeType.get();
		} else {
			attributeType = new LocationAttributeType();
			attributeType.setName("DHIS2_ORG_UNIT");
			attributeType.setDescription("DHIS2 Organisation Unit's UID");
			attributeType.setMinOccurs(0);
			attributeType.setMaxOccurs(1);
			attributeType.setDatatypeClassname("org.openmrs.customdatatype.datatype.FreeTextDatatype");
			Context.getLocationService().saveLocationAttributeType(attributeType);
		}
		LocationAttribute locationAttribute = new LocationAttribute();
		locationAttribute.setAttributeType(attributeType);
		locationAttribute.setValue(dhis2OrgUnitUid);
		location.setAttribute(locationAttribute);
		Context.getLocationService().saveLocation(location);
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
	 * @param organisationUnitCode code of DHIS2 Organisation Unit
	 * @param adxPeriod ADX period string for the period of data
	 * @return generated ADX template
	 * @throws DHIS2ReportingException if unable to set exported time
	 */
	private AdxType generateAdxTemplate(Map<DataValueTemplate, String> mappedDataValueTemplates, DataSet dataSet, String organisationUnitCode, String adxPeriod)
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
		groupType.setOrgUnit(organisationUnitCode);
		groupType.setPeriod(adxPeriod);
		adxTemplate.getGroup().add(groupType);
		List<DataValueType> dataValueTypes = groupType.getDataValue();
		mappedDataValueTemplates.forEach((dataValueTemplate, value) -> {
			DataValueType dataValueType = new DataValueType();
			dataValueType.setDataElement(dataValueTemplate.getDataElement().getUid());
			dataValueType.setValue(new BigDecimal(value));
			dataValueType.getOtherAttributes().put(
					new QName("categoryOptionCombo"),
					dataValueTemplate.getCategoryOptionCombo().getUid());
			dataValueTypes.add(dataValueType);
		});
		return adxTemplate;
	}

}
