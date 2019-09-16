package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.BranchCon} entity. */
public class BranchConDTO implements Serializable {

  private Long id;

  private Long theBranchANDConSubId;

  private Long fromMultipleConId;

  private Long fromActivityId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheBranchANDConSubId() {
    return theBranchANDConSubId;
  }

  public void setTheBranchANDConSubId(Long branchANDConId) {
    this.theBranchANDConSubId = branchANDConId;
  }

  public Long getFromMultipleConId() {
    return fromMultipleConId;
  }

  public void setFromMultipleConId(Long multipleConId) {
    this.fromMultipleConId = multipleConId;
  }

  public Long getFromActivityId() {
    return fromActivityId;
  }

  public void setFromActivityId(Long activityId) {
    this.fromActivityId = activityId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BranchConDTO branchConDTO = (BranchConDTO) o;
    if (branchConDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), branchConDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "BranchConDTO{"
        + "id="
        + getId()
        + ", theBranchANDConSub="
        + getTheBranchANDConSubId()
        + ", fromMultipleCon="
        + getFromMultipleConId()
        + ", fromActivity="
        + getFromActivityId()
        + "}";
  }
}
