/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.dhisreport.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DataValueTemplate implements Serializable {

  private Integer id;
  private DataSet dataSet;
  private DataElement dataElement;
  private String reportIndicatorUuid;
  private Set<Disaggregation> disaggregations = new HashSet<Disaggregation>(0);

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public DataSet getDataSet() {
    return dataSet;
  }

  public void setDataSet(DataSet dataSet) {
    this.dataSet = dataSet;
  }

  public DataElement getDataElement() {
    return dataElement;
  }

  public void setDataElement(DataElement dataElement) {
    this.dataElement = dataElement;
  }

  public String getReportIndicatorUuid() {
    return reportIndicatorUuid;
  }

  public void setReportIndicatorUuid(String reportIndicatorUuid) {
    this.reportIndicatorUuid = reportIndicatorUuid;
  }

  public Set<Disaggregation> getDisaggregations() {
    return disaggregations;
  }

  public void setDisaggregations(Set<Disaggregation> disaggregations) {
    this.disaggregations = disaggregations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataValueTemplate that = (DataValueTemplate) o;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(getDataSet(), that.getDataSet())
        && Objects.equals(getDataElement(), that.getDataElement())
        && Objects.equals(getReportIndicatorUuid(), that
        .getReportIndicatorUuid())
        && getDisaggregations().equals(that.getDisaggregations());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getDataSet(), getDataElement(),
        getReportIndicatorUuid(),
        getDisaggregations());
  }
}
