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
package org.openmrs.module.dhisreport.api.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hisp.dhis.dxf2.datavalueset.DataValueSet;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.openmrs.Location;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dhis.DhisException;
import org.openmrs.module.dhisreport.api.dhis.HttpDhis2Server;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;

/**
 * It is a default implementation of {@link DHIS2ReportingService}.
 */
public class DHIS2ReportingServiceImpl extends BaseOpenmrsService implements DHIS2ReportingService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private DHIS2ReportingDAO dao;
    
    private HttpDhis2Server dhis2Server;

    public HttpDhis2Server getDhis2Server()
    {
        return dhis2Server;
    }

    public void setDhis2Server( HttpDhis2Server dhis2Server )
    {
        this.dhis2Server = dhis2Server;
    }
	
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
    public void setDHISParams( URL url, String user, String password )
    {
        dhis2Server.setUrl( url);
        dhis2Server.setUsername( user );
        dhis2Server.setPassword( password );
        
    }

    @Override
    public ReportDefinition getReportTemplates()
    {
        ReportDefinition reportTemplate = null;
        try
        {
            reportTemplate = dhis2Server.fetchReportTemplates();
        } catch ( DhisException ex )
        {
            Logger.getLogger( DHIS2ReportingServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return reportTemplate;
    }

    @Override
    public ImportSummary postDataValueSet(DataValueSet dvset)
    {
        ImportSummary summary = null;
        try
        {
            summary = dhis2Server.postReport( dvset );
        } catch ( DhisException ex )
        {
            Logger.getLogger( DHIS2ReportingServiceImpl.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return summary;
    }

    @Override
    public DataElement getDataElement( Integer id )
    {
        return dao.getDataElement( id );
    }

    @Override
    public DataElement saveDataElement( DataElement de )
    {
        return dao.saveDataElement( de );
    }

    
    @Override
    public void purgeDataElement( DataElement de )
    {
        dao.deleteDataElement( de );
    }

    @Override
    public Disaggregation getDisaggregation( Integer id )
    {
        return dao.getDisaggregation( id );
    }

    @Override
    public Disaggregation saveDisaggregation( Disaggregation disagg )
    {
        return dao.saveDisaggregation( disagg );
    }

    @Override
    public ReportDefinition getReportDefinition( Integer id )
    {
        return dao.getReportDefinition( id );
    }

    @Override
    public ReportDefinition saveReportDefinition( ReportDefinition reportDefinition )
    {
        return dao.saveReportDefinition( reportDefinition );
    }

    @Override
    public Collection<DataElement> getAllDataElements()
    {
        return dao.getAllDataElements();
    }

    @Override
    public void purgeDisaggregation( Disaggregation disagg )
    {
        dao.deleteDisaggregation( disagg );
    }

    @Override
    public Collection<Disaggregation> getAllDisaggregations()
    {
        return dao.getAllDisaggregations();
    }

    @Override
    public void purgeReportDefinition( ReportDefinition rd )
    {
        dao.deleteReportDefinition( rd );
    }

    @Override
    public Collection<ReportDefinition> getAllReportDefinitions()
    {
        return dao.getAllReportDefinitions();
    }

    @Override
    public String evaluateDataValueTemplate( DataValueTemplate dv, MonthlyPeriod period, Location location )
    {
        return dao.evaluateDataValueTemplate( dv, period, location );
    }

    @Override
    public DataValueSet evaluateReportDefinition( ReportDefinition reportDefinition, MonthlyPeriod period, Location location )
    {
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}