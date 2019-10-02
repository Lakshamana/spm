package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ResourcePossibleUse} entity. */
public class ResourcePossibleUseDTO implements Serializable {

  private Long id;

  private LocalDate begin;

  private LocalDate end;

  private Float amountNeeded;

  private Long theResourceId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
  }

  public Float getAmountNeeded() {
    return amountNeeded;
  }

  public void setAmountNeeded(Float amountNeeded) {
    this.amountNeeded = amountNeeded;
  }

  public Long getTheResourceId() {
    return theResourceId;
  }

  public void setTheResourceId(Long resourceId) {
    this.theResourceId = resourceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResourcePossibleUseDTO resourcePossibleUseDTO = (ResourcePossibleUseDTO) o;
    if (resourcePossibleUseDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), resourcePossibleUseDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ResourcePossibleUseDTO{"
        + "id="
        + getId()
        + ", begin='"
        + getBegin()
        + "'"
        + ", end='"
        + getEnd()
        + "'"
        + ", amountNeeded="
        + getAmountNeeded()
        + ", theResource="
        + getTheResourceId()
        + "}";
  }
}
