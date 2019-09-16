package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.MultipleCon} entity. */
public class MultipleConDTO implements Serializable {

  private Long id;

  private Boolean fired;

  private Long theJoinConSubId;

  private Long theBranchConSubId;

  private Long theDependencyId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isFired() {
    return fired;
  }

  public void setFired(Boolean fired) {
    this.fired = fired;
  }

  public Long getTheJoinConSubId() {
    return theJoinConSubId;
  }

  public void setTheJoinConSubId(Long joinConId) {
    this.theJoinConSubId = joinConId;
  }

  public Long getTheBranchConSubId() {
    return theBranchConSubId;
  }

  public void setTheBranchConSubId(Long branchConId) {
    this.theBranchConSubId = branchConId;
  }

  public Long getTheDependencyId() {
    return theDependencyId;
  }

  public void setTheDependencyId(Long dependencyId) {
    this.theDependencyId = dependencyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MultipleConDTO multipleConDTO = (MultipleConDTO) o;
    if (multipleConDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), multipleConDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "MultipleConDTO{"
        + "id="
        + getId()
        + ", fired='"
        + isFired()
        + "'"
        + ", theJoinConSub="
        + getTheJoinConSubId()
        + ", theBranchConSub="
        + getTheBranchConSubId()
        + ", theDependency="
        + getTheDependencyId()
        + "}";
  }
}
