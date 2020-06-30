package org.openmrs.module.dhisreport.scheduler;

/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Implementation of a task that writes "Hello World" to a log file.
 *
 */
public class PostToDhisTask extends AbstractTask {

	private static Log log = LogFactory.getLog(PostToDhisTask.class);

	/**
	 * Public constructor.
	 */
	public PostToDhisTask() {
		log.debug("hello world task created at " + new Date());
	}

	@Override
	public void execute() {

	}

	public void shutdown() {
		log.debug("shutting down hello world task");
		this.stopExecuting();
	}

	private int sendPost(String urlParameters) throws Exception {

		String url = "http://localhost:8081/openmrs18/module/dhisreport/executeReport.form";
		url = Context.getAdministrationService().getGlobalProperty(
				"dhisreport.SchedulerURL");

		final String USER_AGENT = "Mozilla/5.0";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Host", "localhost:8081");

		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);
		String dhisurl = Context.getAdministrationService().getGlobalProperty(
				"dhisreport.dhis2URL");
		String dhisusername = Context.getAdministrationService()
				.getGlobalProperty("dhisreport.dhis2UserName");
		String dhispassword = Context.getAdministrationService()
				.getGlobalProperty("dhisreport.dhis2Password");
		HttpDhis2Server server = service.getDhis2Server();

		URL urltemp = null;
		try {
			urltemp = new URL(dhisurl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		server.setUrl(urltemp);
		server.setUsername(dhisusername);
		server.setPassword(dhispassword);

		// String urlParameters =
		// "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con
				.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return responseCode;

	}
}
