package org.openmrs.module.dhisreport.api.dxf2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "organizationUnit" )
@XmlAccessorType( XmlAccessType.FIELD )
public class OrganizationUnit
{

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String code;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
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
}
