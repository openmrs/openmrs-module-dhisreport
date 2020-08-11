package org.openmrs.module.dhisreport.api.dfx2;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "organisationUnits")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganisationUnits {
	@XmlElement(name = "organisationUnit", type = OrganisationUnit.class)
	private List<OrganisationUnit> organisationUnits;

	public List<OrganisationUnit> getOrganisationUnits() {
		return organisationUnits;
	}

	public void setOrganisationUnits(List<OrganisationUnit> organisationUnits) {
		this.organisationUnits = organisationUnits;
	}
}
