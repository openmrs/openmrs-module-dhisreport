package org.openmrs.module.dhisreport.api.model;

import org.openmrs.OpenmrsObject;

public class Disaggregation implements OpenmrsObject {
  private Integer id;
  private String uuid;
  private Category category;
  private CategoryOption categoryOption;

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
}
