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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.dfx2.OrganisationUnit;
import org.openmrs.module.dhisreport.api.dhis.Dhis2Exception;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
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
	public void showConfigForm(ModelMap modelMap, WebRequest webRequest) {
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);
		HttpDhis2Server server = service.getDhis2Server();
		Map<String, OrganisationUnit> organisationUnits;
		try {
			organisationUnits = server.getDHIS2OrganisationUnits().stream().collect(Collectors.toMap(
					OrganisationUnit::getId, Function
					.identity()));
		} catch (Dhis2Exception e) {
			webRequest.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context
							.getMessageSourceService().getMessage(
							"dhisreport.currentConnectionFail"),
					WebRequest.SCOPE_SESSION);
			return;
		}
		List<Location> locations = Context.getLocationService().getAllLocations();
		Optional<LocationAttributeType> maybeLocationAttributeType = service
				.getDhis2OrgUnitLocationAttributeType();
		Map<Location, OrganisationUnit> mappedOrganisationUnits = new HashMap<>();
		if(maybeLocationAttributeType.isPresent()){
			LocationAttributeType locationAttributeType = maybeLocationAttributeType.get();
			locations.forEach(location -> {
				List<LocationAttribute> activeAttributes = location
						.getActiveAttributes(locationAttributeType);
				if(activeAttributes.size() > 0){
					String orgUnitUid = (String) activeAttributes.get(0).getValue();
					if(organisationUnits.containsKey(orgUnitUid)){
						mappedOrganisationUnits.put(location, organisationUnits.get(orgUnitUid));
					}
				}
			});
		}
		modelMap.addAttribute("organisationUnits", organisationUnits);
		modelMap.addAttribute("mappedOrganisationUnits", mappedOrganisationUnits);
		modelMap.addAttribute("locations", locations);
	}

	@RequestMapping(value = "/module/dhisreport/mapLocations", method = RequestMethod.POST)
	public String mapLocations(
			@RequestParam String dhis2OrgUnitUid,
			@RequestParam String openmrsLocationUuid,
			WebRequest webRequest) {
		String referer = webRequest.getHeader("Referer");
		DHIS2ReportingService service = Context
				.getService(DHIS2ReportingService.class);
		try {
			service.mapLocationWithDhis2OrgUnit(openmrsLocationUuid, dhis2OrgUnitUid);
			webRequest.setAttribute(WebConstants.OPENMRS_MSG_ATTR, Context
							.getMessageSourceService().getMessage(
							"dhisreport.openMRSLocationMapped"),
					WebRequest.SCOPE_SESSION);
		} catch (DHIS2ReportingException e) {
			webRequest.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context
							.getMessageSourceService().getMessage(
							"dhisreport.openMRSLocationDoesNotExist"),
					WebRequest.SCOPE_SESSION);
		}
		return "redirect:" + referer;
	}

}
