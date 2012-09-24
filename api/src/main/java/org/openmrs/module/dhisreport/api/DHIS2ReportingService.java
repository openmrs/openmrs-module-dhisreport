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

import java.io.InputStream;
import java.util.Collection;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.model.*;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.springframework.transaction.annotation.Transactional;

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
     * Initialize url and credentials for DHIS server
     * 
     * @param server
     */
    public void setDhis2Server(HttpDhis2Server server );
 
    /**
     * 
     * @return the Dhis2 server
     */
    public HttpDhis2Server getDhis2Server();
    
    /**
     * 
     * @return
     * @throws DHIS2ReportingException 
     */
    public ReportDefinition getReportTemplates()
                throws DHIS2ReportingException;
    
    /**
     * 
     * @param dvset
     * @return
     * @throws DHIS2ReportingException 
     */
    public ImportSummary postDataValueSet(DataValueSet dvset)
        throws DHIS2ReportingException;
    
    // -----------------------------------------------------------------------
    // Data access methods
    // -----------------------------------------------------------------------
    
    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly=true)
    public DataElement getDataElement(Integer id);

    /**
     * 
     * @param uid
     * @return 
     */
    @Transactional(readOnly=true)
    public DataElement getDataElementByUid(String uid);

    /**
     * 
     * @param de
     * @return 
     */
    @Transactional
    public DataElement saveDataElement(DataElement de);

    /**
     * 
     * @param de 
     */
    @Transactional
    public void purgeDataElement(DataElement de);
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly=true)
    public Collection<DataElement> getAllDataElements();
    
    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly=true)
    public Disaggregation getDisaggregation(Integer id);

    /**
     * 
     * @param disagg
     * @return 
     */
    @Transactional
    public Disaggregation saveDisaggregation(Disaggregation disagg);

    /**
     * 
     * @param disagg 
     */
    @Transactional
    public void purgeDisaggregation(Disaggregation disagg);
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly=true)
    public Collection<Disaggregation> getAllDisaggregations();

    /**
     * 
     * @param id
     * @return 
     */
    @Transactional(readOnly=true)
    public ReportDefinition getReportDefinition(Integer id);


   
    @Transactional(readOnly=true)
    public ReportDefinition getReportDefinitionByUId(String uid);

    /**
     * 
     * @param reportDefinition
     * @return 
     */
    @Transactional
    public ReportDefinition saveReportDefinition(ReportDefinition reportDefinition);

    /**
     * 
     * @param rd 
     */
    @Transactional
    public void purgeReportDefinition(ReportDefinition rd);
    
    /**
     * 
     * @return 
     */
    @Transactional(readOnly=true)
    public Collection<ReportDefinition> getAllReportDefinitions();

    // -----------------------------------------------------------------------
    // Loading ReportTemplates (DHIS2 Data Structure Definition)
    // -----------------------------------------------------------------------
    
    /**
     * 
     * @param rt 
     */
    @Transactional
    public void SaveReportTemplates(ReportTemplates rt);

    /**
     * 
     * @param is
     * @throws Exception 
     */
    public void unMarshallandSaveReportTemplates(InputStream is) throws Exception;

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