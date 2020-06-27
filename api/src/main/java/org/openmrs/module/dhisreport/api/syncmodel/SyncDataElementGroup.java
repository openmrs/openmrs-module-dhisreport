package org.openmrs.module.dhisreport.api.syncmodel;

public class SyncDataElementGroup {

	private String id;

	private String code;

	private String href;

	private String name;

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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DEG [name=" + name + ", code=" + code + ", id=" + id
				+ ", href=" + href + "]";
	}

}
