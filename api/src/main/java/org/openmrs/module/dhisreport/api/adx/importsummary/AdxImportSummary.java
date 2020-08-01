/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.dhisreport.api.adx.importsummary;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "importSummary")
public class AdxImportSummary {
	@XmlElement(required = true)
	private String status;

	@XmlElement(required = true)
	private String description;

	@XmlElement(required = true)
	private ImportCount importCount;

	@XmlElement(required = true)
	private boolean dataSetComplete;

	@XmlElementWrapper(name = "conflicts")
	@XmlElement(name = "conflict")
	private List<ImportConflict> conflicts;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ImportCount getImportCount() {
		return importCount;
	}

	public void setImportCount(ImportCount importCount) {
		this.importCount = importCount;
	}

	public boolean isDataSetComplete() {
		return dataSetComplete;
	}

	public void setDataSetComplete(boolean dataSetComplete) {
		this.dataSetComplete = dataSetComplete;
	}

	public List<ImportConflict> getConflicts() {
		return conflicts;
	}

	public void setConflicts(List<ImportConflict> conflicts) {
		this.conflicts = conflicts;
	}
}
