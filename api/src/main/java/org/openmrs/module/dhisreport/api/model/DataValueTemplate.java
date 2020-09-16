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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.openmrs.OpenmrsObject;

public class DataValueTemplate implements Serializable, OpenmrsObject {

  private Integer id;
  private String uuid;
  private DataSet dataSet;
  private DataElement dataElement;
  private String reportIndicatorLabel;
  private Set<Disaggregation> disaggregations;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getUuid() {
    return uuid;
  }

  @Override
  public void setUuid(String uuid) {
    this.uuid = uuid;
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

  public String getReportIndicatorLabel() {
    return reportIndicatorLabel;
  }

  public void setReportIndicatorLabel(String reportIndicatorLabel) {
    this.reportIndicatorLabel = reportIndicatorLabel;
  }

  public Set<Disaggregation> getDisaggregations() {
    return disaggregations;
  }

  public void setDisaggregations(
      Set<Disaggregation> disaggregations) {
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
        && Objects.equals(getReportIndicatorLabel(), that
        .getReportIndicatorLabel());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getDataSet(), getDataElement(),
        getReportIndicatorLabel());
  }
}
