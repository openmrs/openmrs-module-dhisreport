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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataElements",
    "disaggregations",
    "reportDefinitions"
})
@XmlRootElement(name = "reportTemplates")
public class ReportTemplates {

    @XmlElementWrapper(name="dataElements", required=true)
    @XmlElement(name="dataElement")
    protected List<DataElement> dataElements;
    
    @XmlElementWrapper(name="disaggregations", required=true)
    @XmlElement(name="disaggregation")
    protected List<Disaggregation> disaggregations;
    
    @XmlElement(name="reportTemplate", required = true)
    protected List<ReportDefinition> reportDefinitions;

    public List<DataElement> getDataElements() {
        return dataElements;
    }

    public void setDataElements(Collection<DataElement> dataElements) {
        this.dataElements = new ArrayList<DataElement>();
        for (DataElement de : dataElements) 
        {
            this.dataElements.add( de );
        }
    }

    public List<Disaggregation> getDisaggregations() {
        return disaggregations;
    }

    public void setDisaggregations(Collection<Disaggregation> disaggregations) {
        this.disaggregations = new ArrayList<Disaggregation>();
        for (Disaggregation disagg : disaggregations) 
        {
            this.disaggregations.add( disagg );
        }
    }

    public List<ReportDefinition> getReportDefinitions() {
        if (reportDefinitions == null) {
            reportDefinitions = new ArrayList<ReportDefinition>();
        }
        return this.reportDefinitions;
    }

    public void setReportDefinitions(Collection<ReportDefinition> reportDefinitions)
    {
        this.reportDefinitions = new ArrayList<ReportDefinition>();
        for (ReportDefinition reportDefinition : reportDefinitions) 
        {
            this.reportDefinitions.add( reportDefinition );
        }
    }
}
