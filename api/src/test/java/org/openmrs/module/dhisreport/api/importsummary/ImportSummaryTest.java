/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.importsummary;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.hisp.dhis.dxf2.importsummary.ImportStatus;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ImportSummaryTest
{
    @Test
    public void marshallImportSummary()
        throws Exception
    {
        ImportSummary summary = new ImportSummary();
        summary.setDescription( "Testing" );
        summary.setStatus( ImportStatus.SUCCESS.name() );
        ImportCount ic = new ImportCount();
        ic.setImported( 4 );
        ic.setUpdated( 0 );
        ic.setIgnored( 1 );
        ic.setDeleted( 2 );
        summary.setImportCount( ic );

        JAXBContext jaxbContext = JAXBContext.newInstance( ImportSummary.class );

        Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
        jaxbmarshaller.marshal( summary, System.out );
    }

    @Test
    public void unMarshallImportSummary()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "adxImportSummary.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( ImportSummary.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ImportSummary importSummary = (ImportSummary) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        assertEquals( 4, importSummary.getImportCount().getImported() );
        assertEquals( 0, importSummary.getImportCount().getUpdated() );
        assertEquals( 1, importSummary.getImportCount().getIgnored() );
        assertEquals( 2, importSummary.getImportCount().getDeleted() );
    }
}
