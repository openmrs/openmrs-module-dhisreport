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
import java.util.Objects;

public class Disaggregation implements Serializable {

  private Integer id;
  Category category;
  CategoryOption categoryOption;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public CategoryOption getCategoryOption() {
    return categoryOption;
  }

  public void setCategoryOption(CategoryOption categoryOption) {
    this.categoryOption = categoryOption;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Disaggregation that = (Disaggregation) o;
    return Objects.equals(getCategory(), that.getCategory())
        && Objects
        .equals(getCategoryOption(), that.getCategoryOption());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCategory(), getCategoryOption());
  }
}
