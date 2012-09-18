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
package org.openmrs.module.dhisreport.api;

import java.net.URL;
import java.util.Collection;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.springframework.transaction.annotation.Transactional;
import utils.MonthlyPeriod;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(DHIS2ReportingService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface DHIS2ReportingService extends OpenmrsService {
     
    // -----------------------------------------------------------------------
    // DHIS Rest API calls
    // -----------------------------------------------------------------------
    
    /**
     * Initialize credentials for DHIS server
     * 
     * @param url
     * @param user
     * @param password 
     */
    void setDHISParams(URL url, String user, String password );
    
    /**
     * Fetches report templates from DHIS
     * 
     * @return 
     */
    public ReportDefinition getReportTemplates();
    
    /**
     * Post a datavalueset to DHIS
     * 
     * @param dvset
     * @return 
     */
    public ImportSummary postDataValueSet(DataValueSet dvset);
    
    // -----------------------------------------------------------------------
    // Data access methods
    // -----------------------------------------------------------------------
    
    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly=true)
    DataElement getDataElement(Integer id);

    /**
     * 
     * @param de
     * @return 
     */
    @Transactional
    DataElement saveDataElement(DataElement de);

    /**
     * 
     * @param de 
     */
    @Transactional
    void purgeDataElement(DataElement de);
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly=true)
    Collection<DataElement> getAllDataElements();
    
    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly=true)
    Disaggregation getDisaggregation(Integer id);

    /**
     * 
     * @param disagg
     * @return 
     */
    @Transactional
    Disaggregation saveDisaggregation(Disaggregation disagg);

    /**
     * 
     * @param disagg 
     */
    @Transactional
    void purgeDisaggregation(Disaggregation disagg);
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly=true)
    Collection<Disaggregation> getAllDisaggregations();

    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly=true)
    ReportDefinition getReportDefinition(Integer id);

    /**
     * 
     * @param reportDefinition
     * @return 
     */
    @Transactional
    ReportDefinition saveReportDefinition(ReportDefinition reportDefinition);

    /**
     * 
     * @param rd 
     */
    @Transactional
    void purgeReportDefinition(ReportDefinition rd);
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly=true)
    Collection<ReportDefinition> getAllReportDefinitions();

    // -----------------------------------------------------------------------
    // ReportEvaluation
    // -----------------------------------------------------------------------
    
    /**
     * 
     * @param dv
     * @param period
     * @param location
     * @return 
     */
    String evaluateDataValueTemplate(DataValueTemplate dv, MonthlyPeriod period, Location location);

    /**
     * 
     * @param reportDefinition
     * @param period
     * @param location
     * @return 
     */
    DataValueSet evaluateReportDefinition(ReportDefinition reportDefinition, MonthlyPeriod period, Location location);
}