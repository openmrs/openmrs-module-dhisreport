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
package org.openmrs.module.dhisreport.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.*;

/**
 * 
 * Based on a dhis2 dataset
 * 
 * @author bobj
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "uid",
    "code",
//    "periodType",
    "dataValueTemplates"
})
@XmlRootElement(name = "reportTemplate")
public class ReportDefinition implements Serializable, Identifiable {

	@XmlTransient
    protected Integer id;
    
    @XmlElement(required = true)
    protected String name;
	
    @XmlElement(required = true)
    protected String uid;
    
    @XmlElement(required = true)
    @XmlID
    protected String code;

    @XmlElementWrapper(name="dataValueTemplates")
    @XmlElement(name="dataValueTemplate")
    protected Set<DataValueTemplate> dataValueTemplates = new HashSet<DataValueTemplate>();
    
    @Override
	public Integer getId() {
		return id;
	}
	
    @Override
	public void setId(Integer id) {
		this.id = id;
	}
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    @Override
    public String getUid()
    {
        return uid;
    }

    @Override
    public void setUid( String uid )
    {
        this.uid = uid;
    }

    public Set<DataValueTemplate> getDataValueTemplates()
    {
        return dataValueTemplates;
    }

    public void setDataValueTemplates( Set<DataValueTemplate> dataValueTemplates )
    {
        this.dataValueTemplates = dataValueTemplates;
    }
    
    public void addDataValueTemplate(DataValueTemplate dataValueTemplate)
    {
        dataValueTemplate.setReportDefinition( this );
        dataValueTemplates.add( dataValueTemplate );
    }

    public void removeDataValueTemplate(DataValueTemplate dataValueTemplate)
    {
        dataValueTemplates.remove( id );
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
        final ReportDefinition other = (ReportDefinition) obj;
        if ( ( this.name == null ) ? ( other.name != null ) : !this.name.equals( other.name ) )
        {
            return false;
        }
        if ( ( this.uid == null ) ? ( other.uid != null ) : !this.uid.equals( other.uid ) )
        {
            return false;
        }
        if ( ( this.code == null ) ? ( other.code != null ) : !this.code.equals( other.code ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + ( this.name != null ? this.name.hashCode() : 0 );
        hash = 29 * hash + ( this.uid != null ? this.uid.hashCode() : 0 );
        hash = 29 * hash + ( this.code != null ? this.code.hashCode() : 0 );
        return hash;
    }

}
