package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.BranchConCond} entity. */
public class BranchConCondDTO implements Serializable {

  private Long id;

  private String kindBranch;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getKindBranch() {
    return kindBranch;
  }

  public void setKindBranch(String kindBranch) {
    this.kindBranch = kindBranch;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BranchConCondDTO branchConCondDTO = (BranchConCondDTO) o;
    if (branchConCondDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), branchConCondDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "BranchConCondDTO{" + "id=" + getId() + ", kindBranch='" + getKindBranch() + "'" + "}";
  }
}
