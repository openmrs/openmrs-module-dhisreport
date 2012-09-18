/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dhisreport.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.*;
import org.openmrs.BaseOpenmrsMetadata;

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
    "periodType",
    "dataValueTemplates"
})
@XmlRootElement(name = "reportTemplate")
public class ReportDefinition extends BaseOpenmrsMetadata implements Serializable {

	protected Integer id;
    
    @XmlElement(required = true)
    protected String name;
	
    @XmlElement(required = true)
    protected String uid;
    
    @XmlElement(required = true)
    protected String code;

    @XmlElement(required = true)
    protected Set<DataValueTemplate> dataValueTemplates = new HashSet<DataValueTemplate>();
    
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
    
    @Override
    public String getName()
    {
        return name;
    }
    
    @Override
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

    public String getUid()
    {
        return uid;
    }

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