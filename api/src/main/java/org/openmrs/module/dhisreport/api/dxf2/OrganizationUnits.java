package org.openmrs.module.dhisreport.api.dxf2;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "organizationUnits" )
@XmlAccessorType( XmlAccessType.FIELD )
public class OrganizationUnits
{

    @XmlElement( name = "organizationUnit" )
    private List<OrganizationUnit> organizationUnits = null;

    public List<OrganizationUnit> getOrganizationUnits()
    {
        return organizationUnits;
    }

    public void setOrganizationUnits( List<OrganizationUnit> organizationUnits )
    {
        this.organizationUnits = organizationUnits;
    }
}
