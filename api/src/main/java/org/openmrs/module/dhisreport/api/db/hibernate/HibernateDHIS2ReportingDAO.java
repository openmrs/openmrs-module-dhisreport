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
package org.openmrs.module.dhisreport.api.db.hibernate;

import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Location;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.dfx2.metadata.dataset.Metadata.DataSets;
import org.openmrs.module.dhisreport.api.model.Category;
import org.openmrs.module.dhisreport.api.model.CategoryOption;
import org.openmrs.module.dhisreport.api.model.DataSet;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.Identifiable;
import org.springframework.transaction.annotation.Transactional;

/**
 * It is a default implementation of {@link DHIS2ReportingDAO}.
 */

public class HibernateDHIS2ReportingDAO implements DHIS2ReportingDAO {

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

	// --------------------------------------------------------------------------------------------------------------
	// Generic methods for DHIS2 identifiable objects
	// --------------------------------------------------------------------------------------------------------------
	public Identifiable getObjectByUid(String uid, Class<?> clazz) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		criteria.add(Restrictions.eq("uid", uid));
		return (Identifiable) criteria.uniqueResult();
	}

	@Override
	@Transactional
	public Identifiable saveObject(Identifiable object) {
		Session session = getCurrentSession();
		// Check if the object is exists in the DB
		Identifiable existingObject = getObjectByUid(object.getUid(), object
				.getClass());
		if (existingObject != null) {
			// Remove existing object from the session
			session.evict(existingObject);
			object.setId(existingObject.getId());
		}
		session.saveOrUpdate(object);
		return object;
	}

	@Override
	@Transactional
	public Disaggregation saveDisaggregation(Disaggregation disaggregation) {
		Session session = getCurrentSession();
		Disaggregation existingDisaggregation = this
				.getDisaggregationByCategoryAndCategoryOption(disaggregation.getCategory(),
						disaggregation.getCategoryOption());
		if (existingDisaggregation != null) {
			return existingDisaggregation;
		}
		session.save(disaggregation);
		return disaggregation;
	}

	@Override
	@Transactional
	public Disaggregation getDisaggregationByCategoryAndCategoryOption(Category category,
			CategoryOption categoryOption) {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(Disaggregation.class);
		criteria.add(Restrictions.eq("category", category));
		criteria.add(Restrictions.eq("categoryOption", categoryOption));
		return (Disaggregation) criteria.uniqueResult();
	}

	@Override
	public Location getLocationByOU_Code(String OU_Code) {
		Criteria criteria = getCurrentSession().createCriteria(Location.class);
		criteria.add(Restrictions.like("name", OU_Code));
		return (Location) criteria.uniqueResult();
	}

	@Override
	@Transactional
	public List<Disaggregation> getDisaggregationsByCategory(Category category) {
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(Disaggregation.class);
		criteria.add(Restrictions.eq("category", category));
		return criteria.list();
	}

	@Override
	@Transactional
	public DataValueTemplate getDataValueTemplateById(Integer id){
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(DataValueTemplate.class);
		criteria.add(Restrictions.eq("id", id));
		return (DataValueTemplate) criteria.uniqueResult();
	}

	@Override
	@Transactional
	public DataValueTemplate saveDataValueTemplate(DataValueTemplate dataValueTemplate) {
		Session session = this.getCurrentSession();
		session.saveOrUpdate(dataValueTemplate);
		return dataValueTemplate;
	}

	@Override
	@Transactional
	public void removeDataValueTemplatesByDataSet(
			DataSet dataSet){
		Session session = getCurrentSession();
		Query query = session.createQuery("delete DataValueTemplate where dataset_id= :DataSetId");
		query.setParameter("DataSetId", dataSet.getId());
		query.executeUpdate();
	}

	@Override
	@Transactional
	public List<DataSet> getAllDataSets(){
		Session session = getCurrentSession();
		Query query = session.createQuery("from DataSet");
		return (List<DataSet>) query.list();
	}

	@Override
	@Transactional
	public DataSet getDataSetByUid(String uid){
		return (DataSet) getObjectByUid(uid, DataSet.class);
	}

	@Override
	@Transactional
	public List<DataValueTemplate> getDataValueTemplatesByDataSet(DataSet dataSet){
		Session session = this.getCurrentSession();
		Criteria criteria = session.createCriteria(DataValueTemplate.class);
		criteria.add(Restrictions.eq("dataSet", dataSet));
		return criteria.list();
	}

	/**
	 * Gets the current hibernate session while taking care of the hibernate 3 and 4 differences.
	 *
	 * @return the current hibernate session.
	 */
	private org.hibernate.Session getCurrentSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (NoSuchMethodError ex) {
			try {
				Method method = sessionFactory.getClass().getMethod(
						"getCurrentSession", null);
				return (org.hibernate.Session) method.invoke(sessionFactory,
						null);
			} catch (Exception e) {
				throw new RuntimeException(
						"Failed to get the current hibernate session", e);
			}
		}
	}
}
