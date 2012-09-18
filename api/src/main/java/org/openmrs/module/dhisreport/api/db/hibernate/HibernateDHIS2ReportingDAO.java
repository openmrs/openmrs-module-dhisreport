/**
 * The contents of this file are subject to the OpenMRS Public License Version 1.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either
 * express or implied. See the License for the specific language governing rights and limitations under the License.
 *
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.dhisreport.api.db.hibernate;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;

/**
 * It is a default implementation of  {@link DHIS2ReportingDAO}.
 */
public class HibernateDHIS2ReportingDAO implements DHIS2ReportingDAO
{
    protected final Log log = LogFactory.getLog( this.getClass() );

    private SessionFactory sessionFactory;

    /**
     * @param sessionFactory the sessionFactory to set
     */
    public void setSessionFactory( SessionFactory sessionFactory )
    {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    @Override
    public DataElement getDataElement( Integer id )
    {
        return (DataElement) sessionFactory.getCurrentSession().get( DataElement.class, id );
    }

    @Override
    public DataElement saveDataElement( DataElement de )
    {
        sessionFactory.getCurrentSession().saveOrUpdate(de);
        return de;
    }

    @Override
    public Disaggregation getDisaggregation( Integer id )
    {
        return (Disaggregation) sessionFactory.getCurrentSession().get( Disaggregation.class, id );
    }

    @Override
    public Disaggregation saveDisaggregation( Disaggregation disagg )
    {
        sessionFactory.getCurrentSession().saveOrUpdate(disagg);
        return disagg;
    }

    @Override
    public DataValueTemplate getDataDataValueTemplate( Integer id )
    {
        return (DataValueTemplate) sessionFactory.getCurrentSession().get( DataValueTemplate.class, id );
    }

    @Override
    public DataValueTemplate saveDataValueTemplate( DataValueTemplate dvt )
    {
        sessionFactory.getCurrentSession().saveOrUpdate(dvt);
        return dvt;
    }

    @Override
    public ReportDefinition getReportDefinition( Integer id )
    {
        return (ReportDefinition) sessionFactory.getCurrentSession().get( ReportDefinition.class, id );
    }

    @Override
    public ReportDefinition saveReportDefinition( ReportDefinition rd )
    {
        sessionFactory.getCurrentSession().saveOrUpdate(rd);
        return rd;
    }
}