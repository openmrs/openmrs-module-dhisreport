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
import java.net.URL;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.dhisreport.api.dhis.DhisException;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
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
     
    void setDHISParams(URL url, String user, String password );
    
    public ReportDefinition getReportTemplates();
    
    public ImportSummary postDataValueSet(DataValueSet dvset);
    
    @Transactional(readOnly=true)
    DataElement getDataElement(Integer id);

    @Transactional
    DataElement saveDataElement(DataElement de);
}