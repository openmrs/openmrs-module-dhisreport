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
package org.openmrs.module.dhisreport.api.dhis;

import java.util.List;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.adx.importsummary.AdxImportSummary;
import org.openmrs.module.dhisreport.api.dfx2.OrganisationUnit;

/**
 *
 * @author bobj
 */
public interface Dhis2Server {

	boolean isConfigured();

	/**
	 * Posts provided ADX template to DHIS2
	 *
	 * @param adxTemplate And ADX Template which contains data
	 * @return Adx Import Summary returned by DHIS2
	 */
	public AdxImportSummary postAdxData(AdxType adxTemplate)
			throws DHIS2ReportingException;

	/**
	 * Gets the Organisation Units of the connected DHIS2 instance
	 *
	 * @return list of Organisation Units
	 * @throws Dhis2Exception if unable to fetch the Organisation Unit list
	 */
	public List<OrganisationUnit> getDHIS2OrganisationUnits() throws Dhis2Exception;

}
