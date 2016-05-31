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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.dxf2.Metadata;
import org.openmrs.module.dhisreport.api.dxf2.OrganizationUnit;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
public class LocationMappingController {
	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/module/dhisreport/mapLocations", method = RequestMethod.GET)
	public void showConfigForm(ModelMap model, WebRequest webRequest) {
		DHIS2ReportingService service = Context.getService(DHIS2ReportingService.class);

		HttpDhis2Server server = service.getDhis2Server();
		String dhisurl = Context.getAdministrationService().getGlobalProperty("dhisreport.dhis2URL");
		String dhisusername = Context.getAdministrationService().getGlobalProperty("dhisreport.dhis2UserName");
		String dhispassword = Context.getAdministrationService().getGlobalProperty("dhisreport.dhis2Password");

		URL url = null;
		try {
			url = new URL(dhisurl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("locationList", Context.getLocationService().getAllLocations());
		Metadata metadata = null;
		List<OrganizationUnit> ou = null;
		Dhis2ServerController connection = new Dhis2ServerController();
		boolean val = connection.testConnection(url, dhisusername, dhispassword, server, webRequest, model);
		if (val == true) {
			try {
				metadata = getDHIS2OrganizationUnits();
			} catch (Exception e) {
				log.debug("Error in Unmarshalling");
				e.printStackTrace();
			}
			if (metadata != null) {
				ou = metadata.getOrganizationUnits().getOrganizationUnits();
				model.addAttribute("orgunits", ou);
				return;
			}

		}
		webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
				Context.getMessageSourceService().getMessage("dhisreport.currentConnectionFail"),
				WebRequest.SCOPE_SESSION);
		model.addAttribute("orgunits", ou);
	}

	@RequestMapping(value = "/module/dhisreport/mapLocations", method = RequestMethod.POST)
	public String mapLocations(ModelMap model,
			@RequestParam(value = "DHIS2OrgUnits", required = true) String dhis2OrgUnitCode,
			@RequestParam(value = "openmrsLocations", required = true) String openmrsLocationName,
			WebRequest webRequest) {
		System.out.println("org unit does not  exits and it is : " + dhis2OrgUnitCode);
		String referer = webRequest.getHeader("Referer");

		if (dhis2OrgUnitCode.equals("")) {
			System.out.println("org unit does not  exits");
			webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
					Context.getMessageSourceService().getMessage("dhisreport.orgUnitCodeDoesNotExist"),
					WebRequest.SCOPE_SESSION);
			return "redirect:" + referer;
		}
		List<Location> locationList = new ArrayList<Location>();
		locationList.addAll(Context.getLocationService().getAllLocations());
		Location loc = Context.getLocationService().getLocation(openmrsLocationName);
		if (loc == null) {
			webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
					Context.getMessageSourceService().getMessage("dhisreport.openMRSLocationDoesNotExist"),
					WebRequest.SCOPE_SESSION);
			return "redirect:" + referer;
		}

		List<LocationAttributeType> attributeTypes = Context.getLocationService().getAllLocationAttributeTypes();
		for (LocationAttributeType lat : attributeTypes) {
			if (lat.getName().equals("CODE")) {
				LocationAttribute locationAttribute = new LocationAttribute();
				locationAttribute.setAttributeType(lat);
				locationAttribute.setValue(dhis2OrgUnitCode);
				loc.setAttribute(locationAttribute);
				Context.getLocationService().saveLocation(loc);
				webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
						Context.getMessageSourceService().getMessage("dhisreport.openMRSLocationMapped"),
						WebRequest.SCOPE_SESSION);
				return "redirect:" + referer;
			}
		}
		LocationAttributeType attributetype = new LocationAttributeType();
		attributetype.setName("CODE");
		attributetype.setDescription("Corresponding Value of ORG UNITS for DHIS");
		attributetype.setMinOccurs(0);
		attributetype.setMaxOccurs(1);
		attributetype.setDatatypeClassname("org.openmrs.customdatatype.datatype.FreeTextDatatype");
		Context.getLocationService().saveLocationAttributeType(attributetype);

		LocationAttribute locationAttribute = new LocationAttribute();
		locationAttribute.setAttributeType(attributetype);
		locationAttribute.setValue(dhis2OrgUnitCode);
		loc.setAttribute(locationAttribute);
		Context.getLocationService().saveLocation(loc);
		webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR,
				Context.getMessageSourceService().getMessage("dhisreport.openMRSLocationMapped"),
				WebRequest.SCOPE_SESSION);
		return "redirect:" + referer;

	}

	public Metadata getDHIS2OrganizationUnits() throws Exception {
		String username = Context.getAdministrationService().getGlobalProperty("dhisreport.dhis2UserName");
		String password = Context.getAdministrationService().getGlobalProperty("dhisreport.dhis2Password");
		String dhisurl = Context.getAdministrationService().getGlobalProperty("dhisreport.dhis2URL");
		String url = dhisurl + "/api/organisationUnits.xml?fields=name,code&paging=false";
		// String url = "https://play.dhis2.org/demo/api/dataSets";
		// String referer = webRequest.getHeader( "Referer" );

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);
		getRequest.addHeader("accept", "application/xml");
		getRequest.addHeader(
				BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "UTF-8", false));
		HttpResponse response;
		InputStream is = null;
		Metadata metadata = null;
		try {
			response = httpClient.execute(getRequest);
			is = response.getEntity().getContent();
			// String result = getStringFromInputStream( is );
			// System.out.println( result + "\n" );
			JAXBContext jaxbContext = JAXBContext.newInstance(Metadata.class);
			javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			metadata = (Metadata) jaxbUnmarshaller.unmarshal(is);

			return metadata;
		} catch (ClientProtocolException e) {
			log.debug("ClientProtocolException occured : " + e.toString());
			e.printStackTrace();
		} finally {
			is.close();
		}
		return metadata;

	}

}
