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

import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;

/**
 *
 * @author bobj
 */
public interface Dhis2Server
{

    /**
     * low level method to access dhis2 resources directly
     * @param path
     * @return
     * @throws Dhis2Exception 
     */
    // public InputStream fetchDhisResource( String path ) throws Dhis2Exception;

    boolean isConfigured();

    ReportDefinition fetchReportTemplates()
        throws DHIS2ReportingException;

    ImportSummary postReport( DataValueSet report )
        throws DHIS2ReportingException;
}
