package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AgentHasAbility} entity. */
public class AgentHasAbilityDTO implements Serializable {

  private Long id;

  private Integer degree;

  private Long theAgentId;

  private Long theAbilityId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getDegree() {
    return degree;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public Long getTheAgentId() {
    return theAgentId;
  }

  public void setTheAgentId(Long agentId) {
    this.theAgentId = agentId;
  }

  public Long getTheAbilityId() {
    return theAbilityId;
  }

  public void setTheAbilityId(Long abilityId) {
    this.theAbilityId = abilityId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AgentHasAbilityDTO agentHasAbilityDTO = (AgentHasAbilityDTO) o;
    if (agentHasAbilityDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), agentHasAbilityDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AgentHasAbilityDTO{"
        + "id="
        + getId()
        + ", degree="
        + getDegree()
        + ", theAgent="
        + getTheAgentId()
        + ", theAbility="
        + getTheAbilityId()
        + "}";
  }
}
