package org.openmrs.module.dhisreport.api.adx;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.xml.sax.InputSource;

public class AdxTypeTest {

	@Test
	public void test() throws JAXBException, FileNotFoundException, XPathExpressionException {
		AdxType adxt = new AdxType();
		XMLGregorianCalendar date3 = null;
		try {
			date3 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2016, 06, 14));
		} catch (DatatypeConfigurationException e) {
			System.out.println("hello exception in date");
			e.printStackTrace();
		}
		adxt.setExported(date3);
		GroupType group = new GroupType();
		group.setDataSet("(TB/HIV)VCCT");
		group.setOrgUnit("OU_559");
		group.setPeriod("2015-06-01/P1M");

		List<GroupType> groups = adxt.getGroup();

		List<DataValueType> dvt = group.getDataValue();
		DataValueType dv = new DataValueType();
		dv.setDataElement("VCCT_0");
		BigDecimal bd = new BigDecimal(32.0);
		dv.setValue(bd);
		dvt.add(dv);
		groups.add(group);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		adxt.marshall(outStream);
		String xm = outStream.toString();
        assertEquals( "1", xpathTest( "count(//d:group)", xm ) );

		
		Writer xmlWriter = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(AdxType.class);

		Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
		jaxbmarshaller.marshal(adxt, xmlWriter);
		String xml = xmlWriter.toString();

		System.out.println(xml);

	}

	protected String xpathTest(String xpathString, String xml) throws XPathExpressionException {
		InputSource source = new InputSource(new StringReader(xml));
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new Dxf2NamespaceResolver());

		return (String) xpath.evaluate(xpathString, source);
	}

}

class Dxf2NamespaceResolver implements NamespaceContext {
	@Override
	public String getNamespaceURI(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("No prefix provided!");
		} else {
			if (prefix.equals("d")) {
				return "http://dhis2.org/schema/dxf/2.0";
			} else {
				return XMLConstants.NULL_NS_URI;
			}
		}
	}

	@Override
	public String getPrefix(String namespaceURI) {
		// Not needed in this context.
		return null;
	}

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		// Not needed in this context.
		return null;
	}
}