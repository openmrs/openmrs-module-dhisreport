package org.openmrs.module.dhisreport.api.dxf2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "metadata")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadata {

	@XmlElement(name = "organisationUnits", type = OrganizationUnits.class)
	private OrganizationUnits organizationUnits;

	public OrganizationUnits getOrganizationUnits() {
		return organizationUnits;
	}

	public void setOrganizationUnits(OrganizationUnits organizationUnits) {
		this.organizationUnits = organizationUnits;
	}

}
