package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ReqAgent} entity. */
public class ReqAgentDTO implements Serializable {

  private Long id;

  private Long theAgentId;

  private Long theRoleId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheAgentId() {
    return theAgentId;
  }

  public void setTheAgentId(Long agentId) {
    this.theAgentId = agentId;
  }

  public Long getTheRoleId() {
    return theRoleId;
  }

  public void setTheRoleId(Long roleId) {
    this.theRoleId = roleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ReqAgentDTO reqAgentDTO = (ReqAgentDTO) o;
    if (reqAgentDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), reqAgentDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ReqAgentDTO{"
        + "id="
        + getId()
        + ", theAgent="
        + getTheAgentId()
        + ", theRole="
        + getTheRoleId()
        + "}";
  }
}
