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
package org.openmrs.module.dhisreport.api.db;

import java.util.Collection;
import org.openmrs.Location;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.utils.Period;

/**
 * Database methods for {@link DHIS2ReportingService}.
 */
public interface DHIS2ReportingDAO
{

    public DataElement getDataElement( Integer id );

    public DataElement getDataElementByUid( String uid );

    public DataElement getDataElementByCode( String code );

    public DataElement saveDataElement( DataElement de );

    public void deleteDataElement( DataElement de );

    public Collection<DataElement> getAllDataElements();

    public Disaggregation getDisaggregation( Integer id );

    public Disaggregation getDisaggregationByUid( String uid );

    public Disaggregation saveDisaggregation( Disaggregation disagg );

    public Collection<Disaggregation> getAllDisaggregations();

    public void deleteDisaggregation( Disaggregation disagg );

    public ReportDefinition getReportDefinition( Integer id );

    public ReportDefinition getReportDefinitionByUid( String uid );

    public ReportDefinition saveReportDefinition( ReportDefinition rd );

    public Collection<ReportDefinition> getAllReportDefinitions();

    public void deleteReportDefinition( ReportDefinition rd );

    public String evaluateDataValueTemplate( DataValueTemplate dvt, Period period, Location location )
        throws DHIS2ReportingException;

    public DataValueTemplate getDataValueTemplate( Integer id );

    public DataValueTemplate saveDataValueTemplate( DataValueTemplate dvt );

    public Location getLocationByOU_Code( String OU_Code );

    public DataValueTemplate saveDataValueTemplateTest( DataValueTemplate dvt );
}
