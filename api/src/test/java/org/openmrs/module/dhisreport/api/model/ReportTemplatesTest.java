/**
 *  Copyright 2009 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHISReporting module.
 *
 *  Billing module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Billing module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Billing module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/

package org.openmrs.module.dhisreport.api.model;

import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author bobj
 */
public class ReportTemplatesTest {

    @Test
    public void unMarshallReportTemplates() throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( ReportTemplates.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ReportTemplates reportTemplates = (ReportTemplates) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        assertNotNull(reportTemplates);
        List<ReportDefinition> reportDefinitions = reportTemplates.getReportTemplates();
        for (ReportDefinition rd : reportDefinitions)
        {
            for (DataValueTemplate dvt : rd.getDataValueTemplates()) {
                // System.out.println(dvt.getDataelement().toString());
            }
        }
    }

    @Test
    public void marshallReportTemplates() throws Exception
    {
        ClassPathResource resource = new ClassPathResource( "templates_ethiopia.xml" );
        JAXBContext jaxbContext = JAXBContext.newInstance( ReportTemplates.class );

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ReportTemplates reportTemplates = (ReportTemplates) jaxbUnmarshaller.unmarshal( resource.getInputStream() );
        
        Marshaller jaxbmarshaller = jaxbContext.createMarshaller();
        jaxbmarshaller.marshal( reportTemplates, System.out);
    }

}
