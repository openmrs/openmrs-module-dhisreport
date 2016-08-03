package org.openmrs.module.dhisreport.api.adx;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPathExpressionException;
import org.junit.Test;

public class AdxTypeTest {

	@Test
	public void shouldHaveProperValuesInAllAttributesOfAdxType() throws JAXBException, FileNotFoundException,
			XPathExpressionException, UnsupportedEncodingException, DatatypeConfigurationException {
		ObjectFactory of = new ObjectFactory();
		AdxType adxt = of.createAdxType();
		Map<QName, String> hm2 = adxt.getOtherAttributes();
		QName qn3 = new QName("adxattri");
		hm2.put(qn3, "adx");
		XMLGregorianCalendar date3 = null;

		date3 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2016, 06, 14));
		adxt.setExported(date3);
		GroupType group = of.createGroupType();
		group.setDataSet("(TB/HIV)VCCT");
		group.setOrgUnit("OU_559");
		group.setPeriod("2015-06-01/P1M");
		Map<QName, String> hm1 = group.getOtherAttributes();
		QName qn2 = new QName("grpattri");
		hm1.put(qn2, "abcde");

		List<GroupType> groups = adxt.getGroup();

		List<DataValueType> dvt = group.getDataValue();
		DataValueType dv = new DataValueType();
		dv.setDataElement("VCCT_0");
		BigDecimal bd = new BigDecimal(32.0);
		dv.setValue(bd);
		Map<QName, String> hm = dv.getOtherAttributes();
		QName qn1 = new QName("dvtattri");
		hm.put(qn1, "abc");

		dvt.add(dv);
		groups.add(group);

		Writer xmlWriter = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(AdxType.class);
		Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
		jaxbmarshaller.marshal(adxt, xmlWriter);
		String xml = xmlWriter.toString();
		InputStream is = null;

		// Convert the String into InputStream
		is = new ByteArrayInputStream(xml.getBytes("UTF-8"));

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		AdxType adxType = (AdxType) jaxbUnmarshaller.unmarshal(is);

		for (QName qn : adxType.getOtherAttributes().keySet()) {
			if (adxType.getOtherAttributes().containsKey(qn)) {
				assertEquals("adx", adxType.getOtherAttributes().get(qn));
			}
		}
		assertEquals("2016-07-14T00:00:00.000+05:30", adxType.getExported().toString());
		List<GroupType> gts = adxType.getGroup();
		for (GroupType gt : gts) {
			assertEquals("(TB/HIV)VCCT", gt.getDataSet());
			assertEquals("OU_559", gt.getOrgUnit());
			assertEquals("2015-06-01/P1M", gt.getPeriod());
			for (QName qn : gt.getOtherAttributes().keySet()) {
				if (gt.getOtherAttributes().containsKey(qn)) {
					assertEquals("abcde", gt.getOtherAttributes().get(qn));

				}
			}

			List<DataValueType> dts = gt.getDataValue();
			for (DataValueType dt : dts) {
				assertEquals("VCCT_0", dt.getDataElement());
				assertEquals(bd, dt.getValue());
				for (QName qn : dt.getOtherAttributes().keySet()) {
					if (dt.getOtherAttributes().containsKey(qn)) {
						assertEquals("abc", dt.getOtherAttributes().get(qn));
					}
				}
			}
		}

	}

}