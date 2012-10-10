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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.model.*;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;

/**
 * It is a default implementation of {@link DHIS2ReportingDAO}.
 */
public class HibernateDHIS2ReportingDAO implements DHIS2ReportingDAO {
	
	// query parameters
	
	private static final String LOCATION = "locationId";
	
	private static final String START = "startDate";
	
	private static final String END = "endDate";
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public DataElement getDataElement(Integer id) {
		return (DataElement) sessionFactory.getCurrentSession().get(DataElement.class, id);
	}
	
	@Override
	public DataElement saveDataElement(DataElement de) {
		return (DataElement) saveObject(de);
	}
	
	@Override
	public void deleteDataElement(DataElement de) {
		sessionFactory.getCurrentSession().delete(de);
	}
	
	@Override
	public Disaggregation getDisaggregation(Integer id) {
		return (Disaggregation) sessionFactory.getCurrentSession().get(Disaggregation.class, id);
	}
	
	@Override
	public Disaggregation saveDisaggregation(Disaggregation disagg) {
		return (Disaggregation) saveObject(disagg);
	}
	
	@Override
	public ReportDefinition getReportDefinition(Integer id) {
		return (ReportDefinition) sessionFactory.getCurrentSession().get(ReportDefinition.class, id);
	}
	
	@Override
	public ReportDefinition saveReportDefinition(ReportDefinition rd) {
		return (ReportDefinition) saveObject(rd);
	}
	
	@Override
	public Collection<DataElement> getAllDataElements() {
		Query query = sessionFactory.getCurrentSession().createQuery("from DataElement order by name asc");
		return (List<DataElement>) query.list();
	}
	
	@Override
	public Collection<Disaggregation> getAllDisaggregations() {
		Query query = sessionFactory.getCurrentSession().createQuery("from Disaggregation");
		return (List<Disaggregation>) query.list();
	}
	
	@Override
	public void deleteDisaggregation(Disaggregation disagg) {
		sessionFactory.getCurrentSession().delete(disagg);
	}
	
	@Override
	public Collection<ReportDefinition> getAllReportDefinitions() {
		Query query = sessionFactory.getCurrentSession().createQuery("from ReportDefinition order by name asc");
		return (List<ReportDefinition>) query.list();
	}
	
	@Override
	public void deleteReportDefinition(ReportDefinition rd) {
		sessionFactory.getCurrentSession().delete(rd);
	}
	
	@Override
	public String evaluateDataValueTemplate(DataValueTemplate dvt, MonthlyPeriod period, Location location)
	                                                                                                       throws DHIS2ReportingException {
		String queryString = dvt.getQuery();
		if (queryString == null || queryString.isEmpty()) {
			log.debug("Empty query for " + dvt.getDataelement().getName() + " : " + dvt.getDisaggregation().getName());
			return null;
		}
		
		if (dvt.potentialUpdateDelete()) {
			throw new DHIS2ReportingException("Attempt to execute potential update/delete query for "
			        + dvt.getDataelement().getName() + " : " + dvt.getDisaggregation().getName());
		}
		
		Query query = sessionFactory.getCurrentSession().createSQLQuery(queryString);
		
		List<String> parameters = new ArrayList<String>(Arrays.asList(query.getNamedParameters()));
		// loactionId is optional
		if (parameters.contains("locationId")) {
			query.setParameter("locationId", location.getId().toString());
		}
		query.setParameter("startOfPeriod", period.getStart());
		query.setParameter("endOfPeriod", period.getEnd());
		
		return query.uniqueResult().toString();
	}
	
	//--------------------------------------------------------------------------------------------------------------
	// Generic methods for DHIS2 identifiable objects
	//--------------------------------------------------------------------------------------------------------------
	public Identifiable getObjectByUid(String uid, Class<?> clazz) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("uid", uid));
		return (Identifiable) criteria.uniqueResult();
	}
	
	public Identifiable saveObject(Identifiable object) {
		Session session = sessionFactory.getCurrentSession();
		// force merge if uid already exists
		Identifiable existingObject = getObjectByUid(object.getUid(), object.getClass());
		if (existingObject != null) {
			session.evict(existingObject);
			object.setId(existingObject.getId());
			session.load(object, object.getId());
		}
		sessionFactory.getCurrentSession().saveOrUpdate(object);
		return object;
	}
	
	@Override
	public DataElement getDataElementByUid(String uid) {
		return (DataElement) getObjectByUid(uid, DataElement.class);
	}
	
	@Override
	public Disaggregation getDisaggregationByUid(String uid) {
		return (Disaggregation) getObjectByUid(uid, Disaggregation.class);
	}
	
	@Override
	public ReportDefinition getReportDefinitionByUid(String uid) {
		return (ReportDefinition) getObjectByUid(uid, ReportDefinition.class);
	}
	
	@Override
	public DataValueTemplate getDataValueTemplate(Integer id) {
		return (DataValueTemplate) sessionFactory.getCurrentSession().get(DataValueTemplate.class, id);
	}
	
	@Override
	public DataValueTemplate saveDataValueTemplate(DataValueTemplate dvt) {
		sessionFactory.getCurrentSession().saveOrUpdate(dvt);
		return dvt;
	}
	
	@Override
	public Location getLocationByOU_Code(String OU_Code) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Location.class);
		criteria.add(Restrictions.like("name", "%" + OU_Code + "%"));
		return (Location) criteria.uniqueResult();
	}
}
