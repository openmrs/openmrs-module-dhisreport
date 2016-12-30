package org.openmrs.module.dhisreport.api.db.hibernate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.DHIS2ReportingService;
import org.openmrs.module.dhisreport.api.db.DHIS2ReportingDAO;
import org.openmrs.module.dhisreport.api.model.DataElement;
import org.openmrs.module.dhisreport.api.model.DataValueTemplate;
import org.openmrs.module.dhisreport.api.model.Disaggregation;
import org.openmrs.module.dhisreport.api.model.ReportDefinition;
import org.openmrs.module.dhisreport.api.model.ReportTemplates;
import org.openmrs.module.dhisreport.api.utils.MonthlyPeriod;
import org.openmrs.module.dhisreport.api.utils.Period;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.core.io.ClassPathResource;

public class HibernateDHIS2ReportingDAOTest extends BaseModuleContextSensitiveTest {
	protected final Log log = LogFactory.getLog(getClass());

	protected static final String INITIAL_OBS_XML = "db/ObsServiceTest-initial.xml";

	DHIS2ReportingService dhis2ReportingService = null;

	DHIS2ReportingDAO dao;

	@Before
	public void before() throws Exception {
		executeDataSet(INITIAL_OBS_XML);
		dhis2ReportingService = Context.getService(DHIS2ReportingService.class);
		dao = (DHIS2ReportingDAO) dhis2ReportingService.getDao();

	}

	@Test
	public void shouldevaluateDataValueTemplate()
			throws DHIS2ReportingException, JAXBException, IOException, ParseException {
		ClassPathResource resource = new ClassPathResource("reportDefinition.xml");
		JAXBContext jaxbContext = JAXBContext.newInstance(ReportTemplates.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ReportTemplates reportTemplates = (ReportTemplates) jaxbUnmarshaller.unmarshal(resource.getInputStream());
		List<ReportDefinition> rds = reportTemplates.getReportDefinitions();
		String timeperiod = "2016-06-28";
		Location location = new Location();
		location.setName("County General");
		location.setDescription("desc");
		location.setAddress1("address1");
		location.setId(3);

		Period period = new MonthlyPeriod(new SimpleDateFormat("yyyy-MM-dd").parse(timeperiod));
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		String start = "2016-06-28";
		String end = "2016-06-30";
		Date startDate = null;
		Date endDate = null;
		startDate = dateformat.parse(start);
		endDate = dateformat.parse(end);

		period.setStartDate(startDate);
		period.setEndDate(endDate);
		for (ReportDefinition rd : rds) {
			for (DataValueTemplate r : rd.getDataValueTemplates()) {

				String dvs = dao.evaluateDataValueTemplate(r, period, location);
				assertEquals("3", dvs);
			}
		}
	}

	@Test
	public void shouldsaveReportDefinitionObjectIfObjectPreExits() throws Exception {
		ClassPathResource resource = new ClassPathResource("reportDefinition.xml");
		dhis2ReportingService.unMarshallandSaveReportTemplates(resource.getInputStream());

		ReportTemplates reportTemplates = dhis2ReportingService.getReportTemplates();
		List<ReportDefinition> rds = reportTemplates.getReportDefinitions();
		for (ReportDefinition rd1 : rds) {
			rd1.setName("New Name");
			dao.saveReportDefinitionObject(rd1);
		}

		SessionFactory sessionFactory = dao.getSessionFactory();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReportDefinition.class);
		criteria.add(Restrictions.eq("uid", rds.get(0).getUid()));
		ReportDefinition existingObject = (ReportDefinition) criteria.uniqueResult();

		assertEquals("MNCH", existingObject.getCode());
		assertEquals("New Name", existingObject.getName());
		assertEquals("Monthly", existingObject.getPeriodType());
		assertEquals("sI82CctvS1A", existingObject.getUid());

	}

	@Test
	public void shouldsaveReportDefinitionObjectIfObjectDoesNotPreExits() throws Exception {

		ClassPathResource resource = new ClassPathResource("reportDefinition.xml");
		dhis2ReportingService.unMarshallandSaveReportTemplates(resource.getInputStream());

		ReportTemplates reportTemplates = dhis2ReportingService.getReportTemplates();
		List<ReportDefinition> rds = reportTemplates.getReportDefinitions();
		Context.clearSession();

		rds.get(0).setUid("asf123411asdf");
		dao.saveReportDefinitionObject(rds.get(0));

		SessionFactory sessionFactory = dao.getSessionFactory();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ReportDefinition.class);
		criteria.add(Restrictions.eq("uid", "asf123411asdf"));
		ReportDefinition existingObject = (ReportDefinition) criteria.uniqueResult();

		assertEquals("MNCH", existingObject.getCode());
		assertEquals("Maternal and Child Health", existingObject.getName());
		assertEquals("Monthly", existingObject.getPeriodType());
	}

	@Test
	public void shouldsaveDataElementObjectTestIfDataElementPreExist() throws Exception {

		ClassPathResource resource = new ClassPathResource("reportDefinition.xml");
		dhis2ReportingService.unMarshallandSaveReportTemplates(resource.getInputStream());

		ReportTemplates reportTemplates = dhis2ReportingService.getReportTemplates();
		DataElement de = reportTemplates.getDataElements().get(0);
		de.setName("NewName");
		de.setCode("newCode");
		dao.saveDataElementObject(de);
		SessionFactory sessionFactory = dao.getSessionFactory();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataElement.class);
		criteria.add(Restrictions.eq("uid", de.getUid()));
		DataElement existingObject = (DataElement) criteria.uniqueResult();
		assertEquals("newCode", existingObject.getCode());
		assertEquals("NewName", existingObject.getName());
	}

	@Test
	public void shouldsaveDataElementObjectTestIfDataElementDoesNotPreExist() throws Exception {

		DataElement de = new DataElement();
		de.setUid("asf123411asdf");
		de.setCode("codeisNew");
		de.setName("newName");
		dao.saveDataElementObject(de);

		SessionFactory sessionFactory = dao.getSessionFactory();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataElement.class);
		criteria.add(Restrictions.eq("uid", de.getUid()));
		DataElement existingObject = (DataElement) criteria.uniqueResult();
		assertEquals("asf123411asdf", existingObject.getUid());
		assertEquals("codeisNew", existingObject.getCode());
		assertEquals("newName", existingObject.getName());
	}

	@Test
	public void shouldSaveObjectIfDoesNotPreExists() {

		DataElement de = new DataElement();
		de.setUid("asf123411asdf");
		de.setCode("codeisNew");
		de.setName("newName");
		dao.saveObject(de);

		SessionFactory sessionFactory = dao.getSessionFactory();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataElement.class);
		criteria.add(Restrictions.eq("uid", de.getUid()));
		DataElement existingObject = (DataElement) criteria.uniqueResult();
		assertEquals("asf123411asdf", existingObject.getUid());
		assertEquals("codeisNew", existingObject.getCode());
		assertEquals("newName", existingObject.getName());
	}

	@Test
	public void shouldSaveObjectIfPreExists() throws IOException, Exception {

		ClassPathResource resource = new ClassPathResource("reportDefinition.xml");
		dhis2ReportingService.unMarshallandSaveReportTemplates(resource.getInputStream());

		ReportTemplates reportTemplates = dhis2ReportingService.getReportTemplates();
		DataElement de = reportTemplates.getDataElements().get(0);

		dao.saveObject(de);
		SessionFactory sessionFactory = dao.getSessionFactory();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DataElement.class);
		criteria.add(Restrictions.eq("uid", de.getUid()));
		DataElement existingObject = (DataElement) criteria.uniqueResult();
		assertEquals("ANC1", existingObject.getCode());
		assertEquals("ANC1", existingObject.getName());

	}

	@Test
	public void shouldSaveDataValueTemplate() throws IOException, Exception {
		ClassPathResource resource = new ClassPathResource("reportDefinition.xml");
		dhis2ReportingService.unMarshallandSaveReportTemplates(resource.getInputStream());

		ReportTemplates reportTemplates = dhis2ReportingService.getReportTemplates();
		DataValueTemplate datavaluetemplate = null;

		List<ReportDefinition> reportdef = reportTemplates.getReportDefinitions();

		for (ReportDefinition rd : reportdef) {

			Set<DataValueTemplate> datavaluetemplates = rd.getDataValueTemplates();

			for (DataValueTemplate dvt : datavaluetemplates) {
				datavaluetemplate = dvt;
				dao.saveDataValueTemplateTest(dvt);
			}

		}

		ReportDefinition rd1 = dao.getReportDefinitionByUid(datavaluetemplate.getReportDefinition().getUid());
		DataElement de1 = dao.getDataElementByUid(datavaluetemplate.getDataelement().getUid());
		Disaggregation dis1 = dao.getDisaggregationByUid(datavaluetemplate.getDisaggregation().getUid());
		Criteria criteria = dao.getSessionFactory().getCurrentSession().createCriteria(DataValueTemplate.class);
		criteria.add(Restrictions.eq("reportDefinition", rd1)).add(Restrictions.eq("dataelement", de1))
				.add(Restrictions.eq("disaggregation", dis1));
		DataValueTemplate dvt_db = (DataValueTemplate) criteria.uniqueResult();

		DataElement de = dvt_db.getDataelement();
		assertEquals("ANC4", de.getCode());
		assertEquals("ANC4", de.getName());
		assertEquals("OWeOBFxrvrv", de.getUid());

		Disaggregation diss = dvt_db.getDisaggregation();
		assertEquals("YtbnZipIBx3", diss.getCode());
		assertEquals("YtbnZipIBx3", diss.getUid());

		ReportDefinition rd = dvt_db.getReportDefinition();
		assertEquals("MNCH", rd.getCode());
		assertEquals("Maternal and Child Health", rd.getName());
		assertEquals("Monthly", rd.getPeriodType());
		assertEquals("sI82CctvS1A", rd.getUid());

	}

}
