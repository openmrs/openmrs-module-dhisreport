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
package org.hisp.dhis.dxf2.importsummary;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author bobj
 */
public class ImportSummaryTest
{

    @Test
    public void marshallImportSummary()
        throws Exception
    {
        ImportSummary summary = new ImportSummary();
        summary.setDescription( "Testing" );
        summary.setStatus( ImportStatus.SUCCESS );
        summary.setDataValueCount( new ImportCount( 2, 1, 4 ) );

        JAXBContext jaxbContext = JAXBContext.newInstance( ImportSummary.class );

        Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
        jaxbmarshaller.marshal( summary, System.out );
    }

    @Test
    public void unMarshallImportSummary()
        throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "importSummary.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( ImportSummary.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ImportSummary importSummary = (ImportSummary) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        assertEquals( 3, importSummary.getDataValueCount().getImported() );
        assertEquals( 0, importSummary.getDataValueCount().getUpdated() );
        assertEquals( 1, importSummary.getDataValueCount().getIgnored() );
    }
}
