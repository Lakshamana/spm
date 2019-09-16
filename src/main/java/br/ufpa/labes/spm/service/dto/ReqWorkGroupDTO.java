package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ReqWorkGroup} entity. */
public class ReqWorkGroupDTO implements Serializable {

  private Long id;

  private Long theWorkGroupTypeId;

  private Long theWorkGroupId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheWorkGroupTypeId() {
    return theWorkGroupTypeId;
  }

  public void setTheWorkGroupTypeId(Long workGroupTypeId) {
    this.theWorkGroupTypeId = workGroupTypeId;
  }

  public Long getTheWorkGroupId() {
    return theWorkGroupId;
  }

  public void setTheWorkGroupId(Long workGroupId) {
    this.theWorkGroupId = workGroupId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReqWorkGroupDTO reqWorkGroupDTO = (ReqWorkGroupDTO) o;
    if (reqWorkGroupDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), reqWorkGroupDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ReqWorkGroupDTO{"
        + "id="
        + getId()
        + ", theWorkGroupType="
        + getTheWorkGroupTypeId()
        + ", theWorkGroup="
        + getTheWorkGroupId()
        + "}";
  }
}
