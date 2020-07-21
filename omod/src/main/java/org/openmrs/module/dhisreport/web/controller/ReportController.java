package org.openmrs.module.dhisreport.web.controller;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReportController {

  @RequestMapping(value = "/module/dhisreport/datasets", method = RequestMethod.GET)
  public void listReports(ModelMap modelMap) {
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    // Get all the available Data Sets
    List<DataSet> dataSets = service.getAllDataSets();
    modelMap.addAttribute("datasets", dataSets);
  }
}
