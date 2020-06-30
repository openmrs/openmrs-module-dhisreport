package org.openmrs.module.dhisreport.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.SerializedObject;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.reporting.definition.DefinitionSummary;
import org.openmrs.module.reporting.definition.service.SerializedDefinitionService;
import org.openmrs.module.reporting.evaluation.parameter.Parameterizable;
import org.openmrs.module.reporting.evaluation.parameter.ParameterizableUtil;
import org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition;
import org.openmrs.module.reporting.report.definition.service.ReportDefinitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;

@Controller
public class MapReportsController {

	private ReportDefinition rd;

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/module/dhisreport/mapReports", method = RequestMethod.GET)
	public void mapReports(
			ModelMap model,
			@RequestParam(value = "reportDefinition_id", required = true) Integer reportDefinition_id) {
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);

		List<DefinitionSummary> definitionSummaries = Context.getService(
				ReportDefinitionService.class).getAllDefinitionSummaries(false);
		List<DefinitionSummary> periodicIndicatorReportDefinitionSummaries = new ArrayList<DefinitionSummary>();
		for (DefinitionSummary ds : definitionSummaries) {
			if (ds
					.getType()
					.equals(
							"org.openmrs.module.reporting.report.definition.PeriodIndicatorReportDefinition"))
				periodicIndicatorReportDefinitionSummaries.add(ds);
		}
		rd = service.getReportDefinition(reportDefinition_id);
		SerializedObject so = Context.getService(
				SerializedDefinitionService.class)
				.getSerializedDefinitionByUuid(rd.getReportingReportId());

		org.openmrs.module.reporting.report.definition.ReportDefinition rrd = Context
				.getService(ReportDefinitionService.class).getDefinitionByUuid(
						rd.getReportingReportId());

		model.put("definitionSummaries",
				periodicIndicatorReportDefinitionSummaries);
		model.addAttribute("user", Context.getAuthenticatedUser());
		model.addAttribute("reportDefinition", rd);

		if (rd.getReportingReportId() != null
				&& rrd instanceof PeriodIndicatorReportDefinition) {
			model.addAttribute("correspondingReportDefinition", so);
			Parameterizable parameterizable = ParameterizableUtil
					.getParameterizable(rd.getReportingReportId(),
							PeriodIndicatorReportDefinition.class);
			model.addAttribute("reportingReportDefinitionReport",
					(PeriodIndicatorReportDefinition) rrd);
		}
	}

	@RequestMapping(value = "/module/dhisreport/mapReports", method = RequestMethod.POST)
	public void mapReportsSubmit(
			ModelMap model,
			@RequestParam(value = "reportDefinition_id", required = true) Integer reportDefinition_id,
			@RequestParam(value = "reportmoduledefinitions", required = true) String reportmoduleDefinitionID) {
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);

		ReportDefinition reportDefinition = service
				.getReportDefinition(reportDefinition_id);
		reportDefinition.setReportingReportId(reportmoduleDefinitionID);
		service.saveReportDefinition(reportDefinition);
	}

	@RequestMapping(value = "/module/dhisreport/confirmReports", method = RequestMethod.POST)
	@SuppressWarnings("null")
	public void confirmReports(
			ModelMap model,
			HttpServletResponse response,
			@RequestParam(value = "reporting_report", required = true) int reportIndex,
			@RequestParam(value = "dhis_report_id", required = true) int dhisReportId) {
		try {
			DHIS2ReportingService service = Context
					.getService(DHIS2ReportingService.class);

			DataValueTemplate selected = service
					.getDataValueTemplate(dhisReportId);

			org.openmrs.module.reporting.report.definition.ReportDefinition rrd = Context
					.getService(ReportDefinitionService.class)
					.getDefinitionByUuid(rd.getReportingReportId());
			PeriodIndicatorReportDefinition pird = (PeriodIndicatorReportDefinition) rrd;
			CohortIndicatorDataSetDefinition cidsd = pird
					.getIndicatorDataSetDefinition();

			if (reportIndex == 0) {
				selected.setQuery(selected.getDefaultreportquery());
				selected.setDefaultreportquery(null);
				selected.setMappeddefinitionlabel(null);
				selected.setMappeddefinitionuuid(null);
				service.saveDataValueTemplate(selected);
			} else if (cidsd.getColumns().get(reportIndex - 1).getIndicator()
					.getParameterizable().getCohortDefinition()
					.getParameterizable().getClass().equals(
							SqlCohortDefinition.class)) {
				SqlCohortDefinition scd = scd = (SqlCohortDefinition) cidsd
						.getColumns().get(reportIndex - 1).getIndicator()
						.getParameterizable().getCohortDefinition()
						.getParameterizable();

				if (selected.getDefaultreportquery() == null) {
					selected.setDefaultreportquery(selected.getQuery());
				}
				selected.setQuery(scd.getQuery());
				selected.setMappeddefinitionlabel(scd.getName());
				selected.setMappeddefinitionuuid(rrd.getUuid());

				service.saveDataValueTemplate(selected);
			} else {
				CohortDefinition cd = cidsd.getColumns().get(reportIndex - 1)
						.getIndicator().getParameterizable()
						.getCohortDefinition().getParameterizable();
				if (selected.getDefaultreportquery() == null) {
					selected.setDefaultreportquery(selected.getQuery());
				}
				selected.setMappeddefinitionlabel(cd.getName());
				selected.setQuery(" ");
				selected.setMappeddefinitionuuid(rrd.getUuid());

				service.saveDataValueTemplate(selected);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
