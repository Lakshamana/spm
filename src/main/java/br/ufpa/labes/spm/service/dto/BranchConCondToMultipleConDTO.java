package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.BranchConCondToMultipleCon} entity. */
public class BranchConCondToMultipleConDTO implements Serializable {

  private Long id;

  private Long theMultipleConId;

  private Long theBranchConCondId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheMultipleConId() {
    return theMultipleConId;
  }

  public void setTheMultipleConId(Long multipleConId) {
    this.theMultipleConId = multipleConId;
  }

  public Long getTheBranchConCondId() {
    return theBranchConCondId;
  }

  public void setTheBranchConCondId(Long branchConCondId) {
    this.theBranchConCondId = branchConCondId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO = (BranchConCondToMultipleConDTO) o;
    if (branchConCondToMultipleConDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), branchConCondToMultipleConDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "BranchConCondToMultipleConDTO{"
        + "id="
        + getId()
        + ", theMultipleCon="
        + getTheMultipleConId()
        + ", theBranchConCond="
        + getTheBranchConCondId()
        + "}";
  }
}
