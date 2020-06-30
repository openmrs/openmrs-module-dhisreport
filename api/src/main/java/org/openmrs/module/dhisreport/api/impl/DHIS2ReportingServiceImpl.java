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

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;

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
	public Location getLocationByOU_Code(String OU_Code) {
		return dao.getLocationByOU_Code(OU_Code);
	}

	@Override
	public Location getLocationByOrgUnitCode(String orgUnitCode) {
		List<Location> locationList = new ArrayList<Location>();
		locationList.addAll(Context.getLocationService().getAllLocations());
		for (Location l : locationList) {
			for (LocationAttribute la : l.getActiveAttributes()) {
				if (la.getAttributeType().getName().equals("CODE")) {
					if ((la.getValue().toString()).equals(orgUnitCode)) {
						return l;
					}
				}

			}
		}
		return null;
	}
}
