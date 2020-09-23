package org.openmrs.module.dhisreport.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.adx.importsummary.AdxImportSummary;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.reporting.definition.DefinitionSummary;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DataSetController {

  @RequestMapping(value = "/module/dhisreport/datasets", method = RequestMethod.GET)
  public void listDataSets(ModelMap modelMap) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    // Get all the available Data Sets
    List<DataSet> dataSets = service.getAllDataSets();
    modelMap.addAttribute("datasets", dataSets);
    modelMap.addAttribute("user", Context.getAuthenticatedUser());
  }

  @RequestMapping(value = "/module/dhisreport/mapDataset", method = RequestMethod.GET)
  public void mapDataSet(ModelMap modelMap,
      @RequestParam String uuid) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    // Get all the available Data Sets
    DataSet dataSet = service.getDataSetByUuid(uuid);
    PeriodIndicatorReportDefinition reportDefinition = null;
    if (dataSet.getReportDefinitionUuid() != null) {
      reportDefinition = (PeriodIndicatorReportDefinition) Context
          .getService(ReportDefinitionService.class).getDefinitionByUuid(
              dataSet.getReportDefinitionUuid());
    }
    List<DefinitionSummary> periodicIndicatorReports = Context.getService(
        ReportDefinitionService.class).getAllDefinitionSummaries(false)
        .stream()
        .filter(definitionSummary -> definitionSummary.getType()
            .equals(
                "org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition"))
        .collect(
            Collectors.toList());
    List<DataValueTemplate> dataValueTemplates = service
        .getDataValueTemplatesByDataSet(dataSet);
    // Sort disaggregation objects
    dataValueTemplates.forEach(dataValueTemplate -> {
      List<Disaggregation> disaggregations = new ArrayList<>(dataValueTemplate.getDisaggregations());
      Collections.sort(disaggregations);
      dataValueTemplate.setDisaggregations(new LinkedHashSet<>(disaggregations));
    });
    // Add relevant attributes to the Model Map
    modelMap.addAttribute("dataset", dataSet);
    modelMap.addAttribute("currentReport", reportDefinition);
    modelMap.addAttribute("reports", periodicIndicatorReports);
    modelMap.addAttribute("dataValueTemplates", dataValueTemplates);
    modelMap.addAttribute("user", Context.getAuthenticatedUser());
  }

  @RequestMapping(value = "/module/dhisreport/dataset/{uuid}/updateReport", method = RequestMethod.POST)
  public @ResponseBody
  void updateReportOfADataSet(@PathVariable String uuid, @RequestParam String reportUuid) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    DataSet dataSet = service.getDataSetByUuid(uuid);
    // Updates the Report if it is different from the current report
    // Todo: check getRepotUuid is null
    if (!reportUuid.equals(dataSet.getReportDefinitionUuid())) {
      service.updateReportOfADataSet(dataSet, reportUuid);
    }
  }

  @RequestMapping(value = "/module/dhisreport/dataValueTemplates/{id}/updateReportIndicator",
      method = RequestMethod.POST)
  public @ResponseBody
  void updateReportIndicatorOfDataValueTemplate(@PathVariable Integer id,
      @RequestParam String reportIndicatorUuid) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    service.updateReportIndicatorOfDataValueTemplate(id, reportIndicatorUuid);
  }

  @RequestMapping(value = "/module/dhisreport/prepareDatasetToPost",
      method = RequestMethod.GET)
  public void prepareDatasetToPost(ModelMap modelMap, @RequestParam String uuid) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    DataSet dataSet = service.getDataSetByUuid(uuid);
    boolean isAllDataValuesTemplatesMapped = service.getDataValueTemplatesByDataSet(dataSet)
        .stream().allMatch(dataValueTemplate -> dataValueTemplate.getReportIndicatorLabel() != null);
    List<Location> locations = Context.getLocationService().getAllLocations();
        List<Location> mappedLocations = new ArrayList<>();
    Optional<LocationAttributeType> maybeLocationAttributeType = getCodeAttributeType();

    if(maybeLocationAttributeType.isPresent()){
      LocationAttributeType locationAttributeType = maybeLocationAttributeType.get();
      mappedLocations = locations.stream().filter(location -> {
        List<LocationAttribute> activeAttributes = location
            .getActiveAttributes(locationAttributeType);
        return activeAttributes.size() > 0;
      }).collect(Collectors.toList());
    }

    // Add relevant attributes to the Model Map
    modelMap.addAttribute("dataset", dataSet);
    modelMap.addAttribute("isAllDataValuesTemplatesMapped", isAllDataValuesTemplatesMapped);
    modelMap.addAttribute("mappedLocations", mappedLocations);
    modelMap.addAttribute("user", Context.getAuthenticatedUser());
  }

  @RequestMapping(value = "/module/dhisreport/postDataSet",
      method = RequestMethod.POST)
  public void postDataSet(ModelMap modelMap, @RequestParam String uuid,
      @RequestParam String locationUuid, @RequestParam(value = "startDate") String startDate) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    try {
      Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
      AdxImportSummary importSummary = service.postDataSetToDHIS2(uuid, locationUuid, date);
      modelMap.addAttribute("importSummary", importSummary);
      modelMap.addAttribute("isError", false);
    } catch (DHIS2ReportingException e) {
      modelMap.addAttribute("isError", true);
      modelMap.addAttribute("errorMessage", e.getMessage());
    } catch (ParseException e){
      modelMap.addAttribute("isError", true);
      modelMap.addAttribute("errorMessage", "Unparsable date: "+ startDate);
    }
  }

  /**
   * Gets the Attribute Type that stores dhis2 Organisation Unit code
   *
   * @return the Optional instance that contains the Attribute type
   */
  private Optional<LocationAttributeType> getCodeAttributeType() {
    return Context.getLocationService()
        .getAllLocationAttributeTypes().stream()
        .filter(locationAttributeType -> locationAttributeType.getName().equals("DHIS2_ORG_UNIT"))
        .findFirst();
  }
}
