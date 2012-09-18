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


import java.io.Serializable;
import javax.xml.bind.annotation.*;
import org.openmrs.BaseOpenmrsMetadata;

/**
 *
 * @author bobj
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "dataValueTemplate")
public class DataValueTemplate extends BaseOpenmrsMetadata implements Serializable{
    
    protected Integer id;
    
    protected ReportDefinition reportDefinition;
    
    @XmlAttribute(required = true)
    protected DataElement dataelement;
    
    @XmlAttribute(required = true)
    protected Disaggregation disaggregation;
    
    @XmlElement(name="annotation",required = false)
    protected String query;

    public ReportDefinition getReportDefinition()
    {
        return reportDefinition;
    }

    public void setReportDefinition( ReportDefinition reportDefinition )
    {
        this.reportDefinition = reportDefinition;
    }
    
    public DataElement getDataelement()
    {
        return dataelement;
    }

    public void setDataelement( DataElement dataelement )
    {
        this.dataelement = dataelement;
    }

    public Disaggregation getDisaggregation()
    {
        return disaggregation;
    }

    public void setDisaggregation( Disaggregation disaggregation )
    {
        this.disaggregation = disaggregation;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId( Integer id )
    {
        this.id = id;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery( String query )
    {
        this.query = query;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final DataValueTemplate other = (DataValueTemplate) obj;
        
        if ( this.dataelement != other.dataelement && ( this.dataelement == null || !this.dataelement.equals( other.dataelement ) ) )
        {
            return false;
        }
        if ( this.disaggregation != other.disaggregation && ( this.disaggregation == null || !this.disaggregation.equals( other.disaggregation ) ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + ( this.dataelement != null ? this.dataelement.hashCode() : 0 );
        hash = 53 * hash + ( this.disaggregation != null ? this.disaggregation.hashCode() : 0 );
        return hash;
    }
    
    public String toString()
    {
        return "DVT: "+ this.getId() + " : " + this.getDataelement().getName() + " : " + this.getDisaggregation().getName();
    }
}
