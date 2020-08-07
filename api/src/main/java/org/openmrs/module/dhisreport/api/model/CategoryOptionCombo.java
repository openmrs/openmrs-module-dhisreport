package org.openmrs.module.dhisreport.api.model;

import java.util.Objects;

public class CategoryOptionCombo implements Identifiable{
  private Integer id;
  private String uid;
  private String name;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getUid() {
    return uid;
  }

  @Override
  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoryOptionCombo that = (CategoryOptionCombo) o;
    return getUid().equals(that.getUid());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUid());
  }
}
