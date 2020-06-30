/**
 *  Copyright 2012 Society for Health Information Systems Programmes, India (HISP India)
 *
 *  This file is part of DHIS2 Reporting module.
 *
 *  DHIS2 Reporting module is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  DHIS2 Reporting module is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with DHIS2 Reporting module.  If not, see <http://www.gnu.org/licenses/>.
 *
 **/
package org.openmrs.module.dhisreport.api.model;

import java.io.Serializable;
import javax.xml.bind.annotation.*;

/**
 * 
 * @author bobj
 */
@XmlType(name = "dataValueTemplate", propOrder = {"dataelement",
		"disaggregation", "query", "defaultreportquery",
		"mappeddefinitionlabel", "mappeddefinitionuuid"})
@XmlRootElement(name = "dataValueTemplate")
public class DataValueTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5401867722816005727L;

	// Regex testing for update/delete
	private static final String SQL_SANITY_CHECK = ".*((?i)update|delete).*";

	protected Integer id;

	protected ReportDefinition reportDefinition;

	protected DataElement dataelement;

	protected Disaggregation disaggregation;

	protected String query;

	protected String default_report_query;

	protected String mapped_definition_label;

	protected String mapped_definition_uuid;

	@XmlTransient
	public ReportDefinition getReportDefinition() {
		return reportDefinition;
	}

	public void setReportDefinition(ReportDefinition reportDefinition) {
		this.reportDefinition = reportDefinition;
	}

	@XmlAttribute(name = "dataElement", required = true)
	@XmlIDREF
	public DataElement getDataelement() {
		return dataelement;
	}

	public void setDataelement(DataElement dataelement) {
		this.dataelement = dataelement;
	}

	@XmlAttribute(name = "disaggregation", required = true)
	@XmlIDREF
	public Disaggregation getDisaggregation() {
		return disaggregation;
	}

	public void setDisaggregation(Disaggregation disaggregation) {
		this.disaggregation = disaggregation;
	}

	@XmlTransient
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement(name = "annotation", required = false)
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@XmlElement(name = "defaultreportquery", required = false)
	public String getDefaultreportquery() {
		return default_report_query;
	}

	public void setDefaultreportquery(String default_report_query) {
		this.default_report_query = default_report_query;
	}

	@XmlElement(name = "mappeddefinitionlabel", required = false)
	public String getMappeddefinitionlabel() {
		return mapped_definition_label;
	}

	public void setMappeddefinitionlabel(String mapped_definition_label) {
		this.mapped_definition_label = mapped_definition_label;
	}

	@XmlElement(name = "mappeddefinitionuuid", required = false)
	public String getMappeddefinitionuuid() {
		return mapped_definition_uuid;
	}

	public void setMappeddefinitionuuid(String mapped_definition_uuid) {
		this.mapped_definition_uuid = mapped_definition_uuid;
	}

	public boolean potentialUpdateDelete() {
		return query.matches(SQL_SANITY_CHECK);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DataValueTemplate other = (DataValueTemplate) obj;

		if (this.dataelement != other.dataelement
				&& (this.dataelement == null || !this.dataelement
						.equals(other.dataelement))) {
			return false;
		}
		if (this.disaggregation != other.disaggregation
				&& (this.disaggregation == null || !this.disaggregation
						.equals(other.disaggregation))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash
				+ (this.dataelement != null ? this.dataelement.hashCode() : 0);
		hash = 53
				* hash
				+ (this.disaggregation != null
						? this.disaggregation.hashCode()
						: 0);
		return hash;
	}

	@Override
	public String toString() {
		return "DVT: " + this.getId() + " : " + this.getDataelement().getName()
				+ " : " + this.getDisaggregation().getName();
	}
}
