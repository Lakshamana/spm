package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AgentAffinityAgent} entity. */
public class AgentAffinityAgentDTO implements Serializable {

  private Long id;

  private Integer degree;

  private Long fromAffinityId;

  private Long toAffinityId;

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

  public Long getFromAffinityId() {
    return fromAffinityId;
  }

  public void setFromAffinityId(Long agentId) {
    this.fromAffinityId = agentId;
  }

  public Long getToAffinityId() {
    return toAffinityId;
  }

  public void setToAffinityId(Long agentId) {
    this.toAffinityId = agentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AgentAffinityAgentDTO agentAffinityAgentDTO = (AgentAffinityAgentDTO) o;
    if (agentAffinityAgentDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), agentAffinityAgentDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AgentAffinityAgentDTO{"
        + "id="
        + getId()
        + ", degree="
        + getDegree()
        + ", fromAffinity="
        + getFromAffinityId()
        + ", toAffinity="
        + getToAffinityId()
        + "}";
  }
}
