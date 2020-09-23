package org.openmrs.module.dhisreport.api.dfx2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "organisationUnit")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrganisationUnit {
	@XmlAttribute
	private String name;

	@XmlAttribute
	private String id;

	@XmlAttribute
	private String code;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
