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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportCount {
	@XmlAttribute(required = true)
	private int imported;

	@XmlAttribute(required = true)
	private int updated;

	@XmlAttribute(required = true)
	private int ignored;

	public int getImported() {
		return imported;
	}

	public void setImported(int imported) {
		this.imported = imported;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}

	public int getIgnored() {
		return ignored;
	}

	public void setIgnored(int ignored) {
		this.ignored = ignored;
	}

	@Override
	public String toString() {
		return "[imports=" + imported + ", updates=" + updated + ", ignores="
				+ ignored + "]";
	}

	public void incrementImported() {
		imported++;
	}

	public void incrementUpdated() {
		updated++;
	}

	public void incrementIgnored() {
		ignored++;
	}

	public void incrementImported(int n) {
		imported += n;
	}

	public void incrementUpdated(int n) {
		updated += n;
	}

	public void incrementIgnored(int n) {
		ignored += n;
	}
}
