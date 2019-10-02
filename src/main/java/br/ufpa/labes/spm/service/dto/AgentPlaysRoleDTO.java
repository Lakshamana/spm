package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AgentPlaysRole} entity. */
public class AgentPlaysRoleDTO implements Serializable {

  private Long id;

  private LocalDate sinceDate;

  private Long theAgentId;

  private Long theRoleId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getSinceDate() {
    return sinceDate;
  }

  public void setSinceDate(LocalDate sinceDate) {
    this.sinceDate = sinceDate;
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

    AgentPlaysRoleDTO agentPlaysRoleDTO = (AgentPlaysRoleDTO) o;
    if (agentPlaysRoleDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), agentPlaysRoleDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AgentPlaysRoleDTO{"
        + "id="
        + getId()
        + ", sinceDate='"
        + getSinceDate()
        + "'"
        + ", theAgent="
        + getTheAgentId()
        + ", theRole="
        + getTheRoleId()
        + "}";
  }
}
