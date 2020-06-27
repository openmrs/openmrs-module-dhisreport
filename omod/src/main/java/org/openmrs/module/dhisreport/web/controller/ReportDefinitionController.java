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
package org.openmrs.module.dhisreport.web.controller;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * The main controller.
 */
@Controller
public class ReportDefinitionController {

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/module/dhisreport/loadReportDefinitions", method = RequestMethod.GET)
	public void uploadForm(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
	}

	@RequestMapping(value = "/module/dhisreport/loadReportDefinitions", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		model.addAttribute("user", Context.getAuthenticatedUser());

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("datafile");

		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);
		InputStream is = multipartFile.getInputStream();
		try {
			service.unMarshallandSaveReportTemplates(is);
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context
					.getMessageSourceService().getMessage(
							"dhisreport.uploadSuccess"));
		} catch (Exception ex) {
			log.error("Error loading file: " + ex);
			session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context
					.getMessageSourceService().getMessage(
							"dhisreport.uploadError"));
		} finally {
			is.close();
		}
	}

	@RequestMapping(value = "/module/dhisreport/exportReportDefinitions", method = RequestMethod.GET)
	public void export(ModelMap model, HttpServletResponse response)
			throws Exception {
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		response.addHeader("Content-Disposition",
				"attachment; filename=reportDefinition.xml");
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);
		ReportTemplates templates = service.getReportTemplates();
		service.marshallReportTemplates(response.getOutputStream(), templates);
	}

	@RequestMapping(value = "/module/dhisreport/editReportDefinition", method = RequestMethod.GET)
	public void editReportDefinition(
			ModelMap model,
			@RequestParam(value = "reportDefinition_id", required = false) Integer reportDefinition_id) {
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);

		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("reportDefinition", service
				.getReportDefinition(reportDefinition_id));
	}

	@RequestMapping(value = "/module/dhisreport/deleteReportDefinition", method = RequestMethod.GET)
	public String deleteReportDefinition(
			ModelMap model,
			@RequestParam(value = "reportDefinition_id", required = false) Integer reportDefinition_id,
			WebRequest webRequest) {
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);

		model.addAttribute("user", Context.getAuthenticatedUser());

		ReportDefinition rd = service.getReportDefinition(reportDefinition_id);

		service.purgeReportDefinition(rd);
		webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context
				.getMessageSourceService().getMessage(
						"dhisreport.deleteSuccess"), WebRequest.SCOPE_SESSION);
		return "redirect:/module/dhisreport/listDhis2Reports.form";
	}

	@RequestMapping(value = "/module/dhisreport/editDataValueTemplate.htm", method = RequestMethod.GET)
	public String editDataValueTemplate(
			ModelMap model,
			@RequestParam(value = "dataValueTemplate_id", required = false) Integer dataValueTemplate_id,
			@RequestParam(value = "dataValueTemplate_query", required = false) String dataValueTemplate_query) {
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);

		DataValueTemplate dvt = service
				.getDataValueTemplate(dataValueTemplate_id);
		if (dataValueTemplate_query.trim().equals("")) {
			dvt.setDefaultreportquery(dvt.getQuery());
			dvt.setMappeddefinitionlabel(null);
			dvt.setMappeddefinitionuuid(null);
			dataValueTemplate_query = null;
		}
		dvt.setQuery(dataValueTemplate_query);

		service.saveDataValueTemplate(dvt);

		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("dataValueTemplate", dvt);
		return "redirect:/module/dhisreport/editReportDefinition.form?reportDefinition_id="
				+ dvt.getReportDefinition().getId();
	}

	@RequestMapping(value = "/module/dhisreport/getReportDefinitions", method = RequestMethod.POST)
	public String getReportDefinitions(WebRequest webRequest,
			HttpServletRequest request) {
		String username = Context.getAdministrationService().getGlobalProperty(
				"dhisreport.dhis2UserName");
		String password = Context.getAdministrationService().getGlobalProperty(
				"dhisreport.dhis2Password");
		String dhisurl = Context.getAdministrationService().getGlobalProperty(
				"dhisreport.dhis2URL");
		String url = dhisurl + "/api/dataSets";
		//String url = "https://play.dhis2.org/demo/api/dataSets";
		String referer = webRequest.getHeader("Referer");
		HttpSession session = request.getSession();

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", "application/dsd+xml");
			getRequest.addHeader(BasicScheme.authenticate(
					new UsernamePasswordCredentials(username, password),
					"UTF-8", false));
			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.error("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			InputStream is = response.getEntity().getContent();
			try {
				DHIS2ReportingService service = Context
						.getService(DHIS2ReportingService.class);
				service.unMarshallandSaveReportTemplates(is);
				session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context
						.getMessageSourceService().getMessage(
								"dhisreport.uploadSuccess"));
			} catch (Exception ex) {
				log.error("Error loading file: " + ex);
				session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context
						.getMessageSourceService().getMessage(
								"dhisreport.uploadError"));
			} finally {
				is.close();
			}
			httpClient.getConnectionManager().shutdown();
			return "redirect:" + referer;
		} catch (ClientProtocolException ee) {
			log.debug("An error occured in the HTTP protocol." + ee.toString());
			ee.printStackTrace();
		} catch (IOException ee) {
			log.debug("Problem accessing DHIS2 server: " + ee.toString());
			session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context
					.getMessageSourceService().getMessage(
							"dhisreport.checkConnectionWithDHIS2"));
		}
		return "redirect:" + referer;
	}
}
