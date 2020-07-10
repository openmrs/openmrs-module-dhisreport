/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.model.dxf2;

import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.openmrs.module.dhisreport.api.model.DataSet;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "metadata")
public class Metadata implements Serializable {
	@XmlAttribute(name = "date")
	String date;

	@XmlElementWrapper(name = "dataSets", required = true)
	@XmlElement(name = "dataSet")
	protected Set<DataSet> dataSets;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Set<DataSet> getDataSets() {
		return dataSets;
	}

	public void setDataSets(Set<DataSet> dataSets) {
		this.dataSets = dataSets;
	}
}
