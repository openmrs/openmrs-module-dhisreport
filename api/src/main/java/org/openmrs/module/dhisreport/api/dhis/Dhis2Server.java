/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of Billing module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.dhisreport.api.dhis;


import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;

/**
 *
 * @author bobj
 */
public interface Dhis2Server {
    
    /**
     * low level method to access dhis2 resources directly
     * @param path
     * @return
     * @throws DhisException 
     */
    // public InputStream fetchDhisResource( String path ) throws DhisException;
    
    boolean isConfigured();
    
    ReportDefinition fetchReportTemplates() throws DhisException;

    ImportSummary postReport(DataValueSet report ) throws DhisException;
}
