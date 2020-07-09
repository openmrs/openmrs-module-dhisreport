package org.openmrs.module.dhisreport.api.dfx2;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "organisationUnits")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationUnits {
	@XmlElement(name = "organisationUnit", type = OrganizationUnit.class)
	private List<OrganizationUnit> organizationUnits;

	public List<OrganizationUnit> getOrganizationUnits() {
		return organizationUnits;
	}

	public void setOrganizationUnits(List<OrganizationUnit> organizationUnits) {
		this.organizationUnits = organizationUnits;
	}
}
