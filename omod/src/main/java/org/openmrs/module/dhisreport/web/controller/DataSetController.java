package org.openmrs.module.dhisreport.web.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.reporting.definition.DefinitionSummary;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
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
      @RequestParam String uid) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    // Get all the available Data Sets
    DataSet dataSet = service.getDataSetByUid(uid);
    ReportDefinition reportDefinition = null;
    if(dataSet.getReportUuid() != null) {
       reportDefinition = Context
          .getService(ReportDefinitionService.class).getDefinitionByUuid(
              dataSet.getReportUuid());
    }
    List<DefinitionSummary> periodicIndicatorReports = Context.getService(
        ReportDefinitionService.class).getAllDefinitionSummaries(false)
        .stream()
        .filter(definitionSummary -> definitionSummary.getType()
            .equals(
                "org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition"))
        .collect(
            Collectors.toList());
    // Add relevant attributes to the Model Map
    modelMap.addAttribute("dataset", dataSet);
    modelMap.addAttribute("currentReport", reportDefinition);
    modelMap.addAttribute("reports", periodicIndicatorReports);
    modelMap.addAttribute("user", Context.getAuthenticatedUser());
  }

  @RequestMapping(value = "/module/dhisreport/dataset/{uid}/updateReport", method = RequestMethod.POST)
  public @ResponseBody void updateReportOfADataSet(@PathVariable String uid, @RequestParam String reportUuid){
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    DataSet dataSet = service.getDataSetByUid(uid);
    // Updates the Report if it is different from the current report
    if(!dataSet.getReportUuid().equals(reportUuid)){
      service.updateReportOfADataSet(dataSet, reportUuid);
    }
  }
}
