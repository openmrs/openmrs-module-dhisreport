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
import org.openmrs.OpenmrsObject;

public class DataSet implements Serializable, Identifiable, OpenmrsObject {

  private Integer id;
  private String uuid;
  private String code;
  private String name;
  private String periodType;
  private String reportDefinitionUuid;
  private Set<DataElement> dataElements = new HashSet<DataElement>();

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

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPeriodType() {
    return periodType;
  }

  public void setPeriodType(String periodType) {
    this.periodType = periodType;
  }

  public String getReportDefinitionUuid() {
    return reportDefinitionUuid;
  }

  public void setReportDefinitionUuid(String reportDefinitionUuid) {
    this.reportDefinitionUuid = reportDefinitionUuid;
  }

  public Set<DataElement> getDataElements() {
    return dataElements;
  }

  public void setDataElements(Set<DataElement> dataElements) {
    this.dataElements = dataElements;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataSet dataSet = (DataSet) o;
    return Objects.equals(getCode(), dataSet.getCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCode());
  }
}
