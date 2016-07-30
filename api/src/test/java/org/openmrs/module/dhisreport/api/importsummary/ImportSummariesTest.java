package org.openmrs.module.dhisreport.api.importsummary;


import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.openmrs.module.dhisreport.api.importsummary.ImportConflict;
import org.openmrs.module.dhisreport.api.importsummary.ImportCount;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummaries;
import org.openmrs.module.dhisreport.api.importsummary.ImportSummary;

public class ImportSummariesTest {

	@Test
	public void shouldHaveProperValuesInAllAttributesOfImportSummaries()
			throws JAXBException, UnsupportedEncodingException {
		ImportConflict imconfl = new ImportConflict("object1", "value1");
		ImportCount imco = new ImportCount();
		int imported = 1;
		int ignored = 2;
		int updated = 3;

		imco.setImported(imported);
		imco.setIgnored(ignored);
		imco.setUpdated(updated);

		ImportSummary is = new ImportSummary();
		is.setDataSetComplete("dataSetComplete");
		is.setDescription("description");
		is.setImportCount(imco);
		is.setStatus("status");
		List<ImportConflict> ic = new ArrayList<ImportConflict>();
		ic.add(imconfl);
		is.setConflicts(ic);

		ImportSummaries iss = new ImportSummaries();
		int deleted = 4;

		iss.setDeleted(deleted);
		iss.setIgnored(ignored);
		iss.setImported(imported);
		iss.setUpdated(updated);
		List<ImportSummary> isum = new ArrayList<ImportSummary>();
		isum.add(is);
		iss.setImportSummaryList(isum);

		Writer xmlWriter = new StringWriter();
		JAXBContext jaxbContext = null;
		jaxbContext = JAXBContext.newInstance(ImportSummaries.class);
		Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
		jaxbmarshaller.marshal(iss, xmlWriter);
		String xml = xmlWriter.toString();

		InputStream is1 = null;

		// Convert the String into InputStream
		is1 = new ByteArrayInputStream(xml.getBytes("UTF-8"));

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		ImportSummaries importSummaries = (ImportSummaries) jaxbUnmarshaller.unmarshal(is1);
		assertEquals(deleted, importSummaries.getDeleted());
		assertEquals(ignored, importSummaries.getIgnored());
		assertEquals(imported, importSummaries.getImported());
		assertEquals(updated, importSummaries.getUpdated());
		List<ImportSummary> isl = importSummaries.getImportSummaryList();
		for (ImportSummary is2 : isl) {
			assertEquals("dataSetComplete", is2.getDataSetComplete());
			assertEquals("description", is2.getDescription());
			assertEquals("status", is2.getStatus());
			List<ImportConflict> icc = is2.getConflicts();
			for (ImportConflict icount : icc) {
				assertEquals("object1", icount.getObject());
				assertEquals("value1", icount.getValue());
			}
			ImportCount icount = is2.getImportCount();
			assertEquals(ignored, icount.getIgnored());
			assertEquals(imported, icount.getImported());
			assertEquals(updated, icount.getUpdated());

		}

	}

}
