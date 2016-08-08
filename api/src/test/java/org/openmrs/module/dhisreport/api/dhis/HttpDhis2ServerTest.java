package org.openmrs.module.dhisreport.api.dhis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.junit.Test;
import org.openmrs.module.dhisreport.api.DHIS2ReportingException;
import org.openmrs.module.dhisreport.api.adx.AdxType;
import org.openmrs.module.dhisreport.api.adx.DataValueType;
import org.openmrs.module.dhisreport.api.adx.GroupType;
import org.openmrs.module.dhisreport.api.adx.ObjectFactory;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummaries;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummary;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public class HttpDhis2ServerTest extends BaseModuleContextSensitiveTest {

	@Test
	public void postAdxReportTest()
			throws DatatypeConfigurationException, MalformedURLException, DHIS2ReportingException {
		HttpDhis2Server server = mock(HttpDhis2Server.class);
		ObjectFactory of = new ObjectFactory();

		AdxType adxt = of.createAdxType();

		XMLGregorianCalendar date3 = null;
		date3 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2016, 06, 14));
		adxt.setExported(date3);
		GroupType group = of.createGroupType();
		group.setOrgUnit("OU_559");
		group.setPeriod("2015-06-01/P1M");
		Map<QName, String> hm1 = group.getOtherAttributes();
		QName qn2 = new QName("idScheme");
		hm1.put(qn2, "Code");

		List<GroupType> groups = adxt.getGroup();

		List<DataValueType> dvt = group.getDataValue();
		DataValueType dv = new DataValueType();
		dv.setDataElement("DE_98454");
		BigDecimal bd = new BigDecimal(32.0);
		dv.setValue(bd);

		dvt.add(dv);
		groups.add(group);

		URL url = new URL("http://localhost:8089/dhis");
		server.setUrl(url);
		server.setPassword("district");
		server.setUsername("admin2");

		ImportSummaries summary = new ImportSummaries();
		List<ImportSummary> importSummaryList = new ArrayList<ImportSummary>();
		ImportSummary is = new ImportSummary();
		is.setStatus("SUCCESS");
		importSummaryList.add(is);
		summary.setImportSummaryList(importSummaryList);
		when(server.postAdxReport(adxt)).thenReturn(summary);
		ImportSummaries summaries = server.postAdxReport(adxt);

		assertEquals(summaries.getImportSummaryList().get(0).getStatus(), "SUCCESS");
	}

}
