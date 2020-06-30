package org.openmrs.module.dhisreport.api.dxf2;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataSets")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSets {
	@XmlElement(name = "dataSet")
	private List<DataSet> dataSets = null;

	public List<DataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(List<DataSet> dataSets) {
		this.dataSets = dataSets;
	}
}