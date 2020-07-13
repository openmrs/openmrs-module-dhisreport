package org.openmrs.module.dhisreport.web.controller;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class DataSetMetadataController {

  private final Log log = LogFactory.getLog(getClass());

  @RequestMapping(value = "/module/dhisreport/metadata", method = RequestMethod.POST)
  public void upload(ModelMap model, HttpServletRequest request) {
    HttpSession session = request.getSession();
    model.addAttribute("user", Context.getAuthenticatedUser());

    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    MultipartFile multipartFile = multipartRequest.getFile("datafile");
    DHIS2ReportingService service = Context
        .getService(DHIS2ReportingService.class);
    try (InputStream is = multipartFile.getInputStream()) {
      service.importDataSet(is);
      session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context
          .getMessageSourceService().getMessage(
              "dhisreport.uploadSuccess"));
    } catch (Exception e) {
      log.error("Error loading file: " + e);
      session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context
          .getMessageSourceService().getMessage(
              "dhisreport.uploadError"));
    }
  }

  @RequestMapping(value = "/module/dhisreport/metadata", method = RequestMethod.GET)
  public void test(ModelMap model) {
    model.addAttribute("user", Context.getAuthenticatedUser());
  }
}
