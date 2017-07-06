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
package org.openmrs.module.dhisreport.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dxf2.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummaries;
import org.openmrs.module.dhisreport.api.model.*;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean
 * which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(DHIS2ReportingService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface DHIS2ReportingService
    extends OpenmrsService
{

    // -----------------------------------------------------------------------
    // DHIS Rest API calls
    // -----------------------------------------------------------------------
    /**
     * Initialize url and credentials for DHIS server
     * 
     * @param server
     */
    public void setDhis2Server( HttpDhis2Server server );

    /**
     * @return the Dhis2 server
     */
    public HttpDhis2Server getDhis2Server();

    /**
     * @return @throws DHIS2ReportingException
     */
    public ReportDefinition fetchReportTemplates()
        throws DHIS2ReportingException;

    /**
     * @param dvset
     * @return
     * @throws DHIS2ReportingException
     */
    public ImportSummary postDataValueSet( DataValueSet dvset )
        throws DHIS2ReportingException;

    public ImportSummaries postAdxReport( AdxType adxReport )
        throws DHIS2ReportingException;

    // -----------------------------------------------------------------------
    // Data access methods
    // -----------------------------------------------------------------------
    /**
     * @param id
     * @return
     */
    @Transactional( readOnly = true )
    public DataElement getDataElement( Integer id );

    /**
     * @param uid
     * @return
     */
    @Transactional( readOnly = true )
    public DataElement getDataElementByUid( String uid );

    /**
     * @param code
     * @return
     */
    @Transactional( readOnly = true )
    public DataElement getDataElementByCode( String code );

    /**
     * @param de
     * @return
     */
    @Transactional
    public DataElement saveDataElement( DataElement de );

    /**
     * @param de
     */
    @Transactional
    public void purgeDataElement( DataElement de );

    /**
     * @return
     */
    @Transactional( readOnly = true )
    public Collection<DataElement> getAllDataElements();

    /**
     * @param id
     * @return
     */
    @Transactional( readOnly = true )
    public Disaggregation getDisaggregation( Integer id );

    /**
     * @param disagg
     * @return
     */
    @Transactional
    public Disaggregation saveDisaggregation( Disaggregation disagg );

    /**
     * @param disagg
     */
    @Transactional
    public void purgeDisaggregation( Disaggregation disagg );

    /**
     * @return
     */
    @Transactional( readOnly = true )
    public Collection<Disaggregation> getAllDisaggregations();

    /**
     * @param id
     * @return
     */
    @Transactional( readOnly = true )
    public ReportDefinition getReportDefinition( Integer id );

    @Transactional( readOnly = true )
    public ReportDefinition getReportDefinitionByUId( String uid );

    @Transactional( readOnly = true )
    public ReportDefinition getReportDefinitionByCode( String code );

    /**
     * @param reportDefinition
     * @return
     */
    @Transactional
    public ReportDefinition saveReportDefinition( ReportDefinition reportDefinition );

    /**
     * @param rd
     */
    @Transactional
    public void purgeReportDefinition( ReportDefinition rd );

    /**
     * @return
     */
    @Transactional( readOnly = true )
    public Collection<ReportDefinition> getAllReportDefinitions();

    // -----------------------------------------------------------------------
    // ReportTemplates (DHIS2 Data Structure Definition)
    // -----------------------------------------------------------------------
    /**
     * @param rt
     */
    @Transactional
    public void saveReportTemplates( ReportTemplates rt );

    /**
     * @param is
     * @throws Exception
     */
    public void unMarshallandSaveReportTemplates( InputStream is )
        throws Exception;

    @Transactional
    public ReportTemplates getReportTemplates();

    /**
     * @param is
     * @throws Exception
     */
    public void marshallReportTemplates( OutputStream os, ReportTemplates rt )
        throws Exception;

    // -----------------------------------------------------------------------
    // ReportEvaluation
    // -----------------------------------------------------------------------
    /**
     * @param dv
     * @param period
     * @param location
     * @return
     */
    String evaluateDataValueTemplate( DataValueTemplate dv, Period period, Location location )
        throws DHIS2ReportingException;

    /**
     * @param reportDefinition
     * @param period
     * @param location
     * @return
     */
    DataValueSet evaluateReportDefinition( ReportDefinition reportDefinition, Period period, Location location );

    DataValueSet generateReportingReportDefinition( ReportDefinition reportDefinition, Period period, Location location )
        throws Exception;

    /**
     * @param id
     * @return
     */
    @Transactional
    public DataValueTemplate getDataValueTemplate( Integer id );

    /**
     * @param dvt
     */
    @Transactional
    public void saveDataValueTemplate( DataValueTemplate dvt );

    /**
     * @param OU_Code
     * @return
     */
    public Location getLocationByOU_Code( String OU_Code );

    public void saveDataValueTemplateTest( DataValueTemplate dvt );

    public Location getLocationByOrgUnitCode( String orgUnitCode );

    public DHIS2ReportingDAO getDao();

}
